package staff.vaadin;

import java.util.Locale;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
public class VaadinloginUI extends UI {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = VaadinloginUI.class)
    public static class Servlet extends VaadinServlet {
    }

    public static Authentication AUTH;
    @Override
    protected void init(VaadinRequest request) {
        AUTH = new Authentication();
        new Navigator(this, this);

        getNavigator().addView(LoginPage.NAME, LoginPage.class);
        getNavigator().setErrorView(LoginPage.class);

        Page.getCurrent().addUriFragmentChangedListener(new Page.UriFragmentChangedListener() {

            @Override
            public void uriFragmentChanged(Page.UriFragmentChangedEvent event) {
                router(event.getUriFragment());
            }
        });


        router("");
    }

    private void router(String route){
        Notification.show(route);
        if(getSession().getAttribute("user") != null){
            getNavigator().addView(DepartmentVaadin.NAME, DepartmentVaadin.class);
            getNavigator().navigateTo(DepartmentVaadin.NAME);
        }else{
            getNavigator().navigateTo(LoginPage.NAME);
        }
    }
}