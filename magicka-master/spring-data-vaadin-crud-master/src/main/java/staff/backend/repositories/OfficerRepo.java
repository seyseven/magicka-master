package staff.backend.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import staff.backend.model.Officer;


public interface OfficerRepo extends JpaRepository<Officer, Long> {
}