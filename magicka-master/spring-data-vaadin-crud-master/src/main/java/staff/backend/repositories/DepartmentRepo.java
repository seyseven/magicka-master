package staff.backend.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import staff.backend.model.Department;

import java.util.List;

public interface DepartmentRepo extends JpaRepository<Department, Long> {

    List<Department> findAllBy(Pageable pageable);

    List<Department> findByNameLikeIgnoreCase(String nameFilter);

    // For lazy loading and filtering
    List<Department> findByNameLikeIgnoreCase(String nameFilter, Pageable pageable);

    long countByNameLikeIgnoreCase(String nameFilter);
}
