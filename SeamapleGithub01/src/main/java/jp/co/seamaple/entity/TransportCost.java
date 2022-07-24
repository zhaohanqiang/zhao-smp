package jp.co.seamaple.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "m_transportcost")
public class TransportCost implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "type")
    private Integer type;

    @Column(name = "month_year")
    private String monthYear;

    @Column(name = "day")
    private Integer day;

    @Column(name = "departure")
    private String departure;

    @Column(name = "arrival")
    private String arrival;

    @Column(name = "cost")
    private String cost;

    @Column(name = "reason")
    private String reason;

    @Column(name = "reg_time")
    private Timestamp regTime;

    @Column(name = "disabled")
    private Boolean disabled; // true(1) = 編集不可
}
