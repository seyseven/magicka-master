package staff.backend.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import staff.backend.model.History;

public interface HistoryRepo extends JpaRepository<History, Long> {

}