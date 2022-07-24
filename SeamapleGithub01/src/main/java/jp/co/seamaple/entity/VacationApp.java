package jp.co.seamaple.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 休暇申請情報 Entity
 */
@Entity
@Data
@Table(name = "m_vacationapp")
public class VacationApp implements Serializable {

    @Id
    @Column(name = "vacationapp_No")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer vacationappNo; // 休暇申請番号

    @Column(name = "vacationapp_date")
    private Date vacationappDate; // 休暇申請日時

    @Column(name = "vacationapp_type")
    private String vacationAppType; // 休暇種別

    @Column(name = "user_id")
    private String userId; // 社員番号

    @Column(name = "user_name")
    private String userName; // 氏名

    @Column(name = "vacationapp_comment")
    private String vacationappComment; // 休暇申請コメント

    @Column(name = "approver_name")
    private String approverName; // 承認者

    @Column(name = "app_status")
    private Integer appStatus; // 承認ステータス

    @Column(name = "update_date")
    private Date updateDate; // 更新日時

    @Column(name = "create_date")
    private Date createDate; // 作成日時

    @Column(name = "paid_vacation_type")
    private Integer paidVacationType; // 有給タイプ 0 = lastPaidVacation, 1 = previousPaidVacation
}
