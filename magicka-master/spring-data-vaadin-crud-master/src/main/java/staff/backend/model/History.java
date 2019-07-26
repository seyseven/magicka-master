package staff.backend.model;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

@Entity
@Table(name = "history")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY as the example has flyway in it and fixed schema in src/main/resources/db/migration
    private long id;

    @Column(name = "Date", nullable = false)
    private Date date;

    @Column(name = "Time", nullable = false)
    private Time time;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @ManyToOne
    @OrderBy("officer_id")
    private Officer officers;

    public History(){
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }


}