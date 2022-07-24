package jp.co.seamaple.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 休暇日数Entity
 */
@Entity
@Data
@Table(name = "m_vacation_days")
public class VacationDays implements Serializable {

    /**
     * 社員番号
     */
    @Id
    @Column(name = "user_id")
    private String userId;

    /**
     * 最新の有給休暇日数
     */
    @Column(name = "last_paid_vacation")
    private int lastPaidVacation;

    /**
     * 前々回の有給休暇日数
     */
    @Column(name = "previous_paid_vacation")
    private int previousPaidVacation;

    /**
     * 有給付与回数
     */
    @Column(name = "paid_vacation_add_count")
    private int paidVacationAddCount;

    /**
     * 有給付与日
     */
    @Column(name = "paid_vacation_add_date")
    private Date paidVacationAddDate;

    /**
     * 夏季休暇日数
     */
    @Column(name = "summer_vacation")
    private int summerVacation;

    /**
     * 夏季休暇付与日
     */
    @Column(name = "summer_vacation_add_date")
    private Date summerVacationAddDate;

    @OneToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @ToString.Exclude // stack over flow error 防止
    private User user;
}
