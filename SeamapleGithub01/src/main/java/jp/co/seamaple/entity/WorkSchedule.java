package jp.co.seamaple.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "m_work_schedule")
public class WorkSchedule {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "work_hour")
    private Float workHour;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_content")
    private byte[] fileContent;

    @Column(name = "file_size")
    private String fileSize;

    @Column(name = "month_year")
    private String monthYear;

    @Column(name = "weekends_work")
    private String weekendsWork;

    @Column(name = "memo")
    private String memo;

    @Column(name = "registered_date")
    private Date registeredDate;

    public boolean isPresent() {
        return false;
    }
}
