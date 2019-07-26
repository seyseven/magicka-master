package staff.backend.services;

import staff.backend.model.Department;

import java.util.List;

public interface DepartmentService {
    List<Department> fetchDepartment(int offset, int limit);
    int getDepartmentCount();
}
