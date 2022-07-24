package jp.co.seamaple.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Data
@Table(name = "m_user")
public class User implements Serializable {

    @Id
    @Column(name = "user_id")
    private String userId; // 社員番号

    @Column(name = "password")
    private String password; // パスワード

    @Column(name = "user_name")
    private String userName; // 氏名

    @Column(name = "mail_address")
    private String mailAddress; // メールアドレス

    @Column(name = "phone_number")
    private String phoneNumber; // 電話番号

    @Column(name = "enabled")
    private Integer enabled; // 有効フラグ

    @Column(name = "reg_time")
    private Timestamp regTime; // 登録日時

    @Column(name = "update_time")
    private Timestamp updateTime; // 更新日時

    @Column(name = "role_id")
    private String roleId; // ユーザー権限

    @Column(name = "join_company_date")
    private Date joinCompanyDate; // 入社日

    @Column(name = "retired_date")
    private Date retiredDate; // 退職日

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    private VacationDays vacationDays; // 有給情報

}
