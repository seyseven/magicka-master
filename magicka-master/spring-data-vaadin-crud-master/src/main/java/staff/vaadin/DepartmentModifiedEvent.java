package staff.vaadin;

import staff.backend.model.Department;

import java.io.Serializable;

public class DepartmentModifiedEvent implements Serializable {

    private final Department departments;

    public DepartmentModifiedEvent(Department p) {
        this.departments = p;
    }

    public Department getPerson() {
        return departments;
    }
    
}
