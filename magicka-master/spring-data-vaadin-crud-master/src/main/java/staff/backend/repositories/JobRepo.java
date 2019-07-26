package staff.backend.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import staff.backend.model.Job;

import java.util.List;

public interface JobRepo extends JpaRepository<Job, Long> {

    List<Job> findAllBy(Pageable pageable);

    List<Job> findByNameLikeIgnoreCase(String nameFilter);

    // For lazy loading and filtering
    List<Job> findByNameLikeIgnoreCase(String nameFilter, Pageable pageable);

    long countByNameLikeIgnoreCase(String nameFilter);
}
