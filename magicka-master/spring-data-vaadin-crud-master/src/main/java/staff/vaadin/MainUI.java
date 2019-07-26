package staff.vaadin;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import staff.backend.model.Department;

import java.util.ArrayList;
import java.util.List;

import staff.backend.repositories.DepartmentRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.components.DisclosurePanel;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.label.RichText;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;


public class MainUI extends UI implements View {

    private static final long serialVersionUID = 1L;

    public static final String NAME = "MainUI";

    DepartmentRepo repo;
    DepartmentForm personForm;
    EventBus.UIEventBus eventBus;

    private MGrid<Department> list = new MGrid<>(Department.class)
            .withProperties("id", "name")
            .withColumnHeaders("id", "Name")
            // not yet supported by V8
            //.setSortableProperties("name", "email")
            .withFullWidth();

    private MTextField filterByName = new MTextField()
            .withPlaceholder("Filter by name");
    private Button addNew = new MButton(VaadinIcons.PLUS, this::add);
    private Button edit = new MButton(VaadinIcons.PENCIL, this::edit);
    private Button delete = new ConfirmButton(VaadinIcons.TRASH,
            "Are you sure you want to delete the entry?", this::remove);


    public MainUI(DepartmentRepo r, DepartmentForm f, EventBus.UIEventBus b) {
        this.repo = r;
        this.personForm = f;
        this.eventBus = b;

    }
    @Override
    protected void init(VaadinRequest request) {
        DisclosurePanel aboutBox = new DisclosurePanel("Departments", new RichText().withMarkDownResource("/welcome.md"));
        setContent(
                new MVerticalLayout(
                        aboutBox,
                        new MHorizontalLayout(filterByName, addNew, edit, delete),
                        list
                ).expand(list)
        );
        listEntities();

        list.asSingleSelect().addValueChangeListener(e -> adjustActionButtonState());
        filterByName.addValueChangeListener(e -> {
            listEntities(e.getValue());
        });

        // Listen to change events emitted by DepartmentForm see onEvent method
        eventBus.subscribe(this);
    }

    protected void adjustActionButtonState() {
        boolean hasSelection = !list.getSelectedItems().isEmpty();
        edit.setEnabled(hasSelection);
        delete.setEnabled(hasSelection);
    }

    private void listEntities() {
        listEntities(filterByName.getValue());
    }

    final int PAGESIZE = 45;

    private void listEntities(String nameFilter) {
        // A dead simple in memory listing would be:
        // list.setRows(repo.findAll());

        // But we want to support filtering, first add the % marks for SQL name query
        String likeFilter = "%" + nameFilter + "%";

        // If there is a moderate amount of rows, one can just fetch everything
        //list.setRows(repo.findByNameLikeIgnoreCase(likeFilter));

        // Lazy binding for better optimized connection from the Vaadin Grid to
        // Spring Repository. This approach uses less memory and database
        // resources. Use this approach if you expect you'll have lots of data
        // in your table. There are simpler APIs if you don't need sorting.
        list.setDataProvider(
                // lazy entity fetching strategy, due to a design flaw in DataProvider API,
                // this is bit tricky with Spring Data's Pageable abstration as requests
                // by Grid may be on two pages, see https://github.com/vaadin/framework/issues/8982
                // TODO see if this could be simplified in Viritin MGrid
                (sortOrder, offset, limit) -> {
                    final int pageSize = limit;
                    final int startPage = (int) Math.floor((double) offset / pageSize);
                    final int endPage = (int) Math.floor((double) (offset + pageSize - 1) / pageSize);
                    final Sort.Direction sortDirection = sortOrder.isEmpty() || sortOrder.get(0).getDirection() == SortDirection.ASCENDING ? Sort.Direction.ASC : Sort.Direction.DESC;
                    // fall back to id as "natural order"
                    final String sortProperty = sortOrder.isEmpty() ? "id" : sortOrder.get(0).getSorted();
                    if (startPage != endPage) {
                        List<Department> page0 = repo.findByNameLikeIgnoreCase(likeFilter, PageRequest.of(startPage, pageSize, sortDirection, sortProperty));
                        page0 = page0.subList(offset % pageSize, page0.size());
                        List<Department> page1 = repo.findByNameLikeIgnoreCase(likeFilter, PageRequest.of(endPage, pageSize, sortDirection, sortProperty));
                        page1 = page1.subList(0, limit - page0.size());
                        List<Department> result = new ArrayList<>(page0);
                        result.addAll(page1);
                        return result.stream();
                    } else {
                        return repo.findByNameLikeIgnoreCase(likeFilter, PageRequest.of(startPage, pageSize, sortDirection, sortProperty)).stream();
                    }
                },
                // count fetching strategy
                () -> (int) repo.countByNameLikeIgnoreCase(likeFilter)
        );
        adjustActionButtonState();

    }

    public void add(ClickEvent clickEvent) {
        edit(new Department());
    }

    public void edit(ClickEvent e) {
        edit(list.asSingleSelect().getValue());
    }

    public void remove() {
        repo.delete(list.asSingleSelect().getValue());
        list.deselectAll();
        listEntities();
    }

    protected void edit(final Department phoneBookEntry) {
        personForm.setEntity(phoneBookEntry);
        personForm.openInModalPopup();
    }

    @EventBusListenerMethod(scope = EventScope.UI)
    public void onPersonModified(DepartmentModifiedEvent event) {
        listEntities();
        personForm.closePopup();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

}
