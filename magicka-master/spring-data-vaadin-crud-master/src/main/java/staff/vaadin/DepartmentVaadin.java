package staff.vaadin;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.spring.events.EventBus;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.grid.MGrid;
import staff.backend.model.Department;
import staff.backend.repositories.DepartmentRepo;

public class DepartmentVaadin extends VerticalLayout implements View{
    private static final long serialVersionUID = 1L;
    public static final String NAME = "Main";


    private Grid<Department> grid;
    private MGrid<Department> list;
    private MTextField filterByName;
    private Button addNew;
    private Button edit;
    private Button delete;

    public DepartmentVaadin(){
        initDepartmentVaadin();
    }

    private void initDepartmentVaadin() {
        DepartmentRepo repo;
        DepartmentForm personForm;
        EventBus.UIEventBus eventBus;

        grid = new Grid<>();;
        grid.addColumn(Department::getId).setCaption("ID");
        grid.addColumn(Department::getName).setCaption("Name");

        filterByName = new MTextField()
                .withPlaceholder("Filter by name");

        addNew = new MButton(VaadinIcons.PLUS);
        ((MButton) addNew).addClickListener(new MyListener());

        edit = new MButton(VaadinIcons.PENCIL);

        addComponent(filterByName);
        addComponent(addNew);
        addComponent(edit);
        addComponent(grid);
    }

    class MyListener implements MButton.MClickListener {
        @Override
        public void onClick() {
            System.out.print("Call from my MyListener.");
        }
    }
}
