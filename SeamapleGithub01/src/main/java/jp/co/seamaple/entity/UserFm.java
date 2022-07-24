package jp.co.seamaple.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "m_userfm")

public class UserFm implements Serializable {
    // User 家族の情報
    @Id
    @Column(name = "fm_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fm_id; // 親友のＩＤ

    @Column(name = "user_fmid")
    private String userId; // 社員番号

    @Column(name = "fm_furigana")
    private String fm_furigana; // フリガナ

    @Column(name = "fm_birth")
    private String fm_birth; // 親友誕生日

    @Column(name = "fm_name")
    private String fm_name; // 名前

    @Column(name = "fm_tel")
    private String fm_tel; // TEL

    @Column(name = "fm_relation")
    private String fm_relation; // 続柄

    @Column(name = "fm_job")
    private String fm_job; // 職業

    @Column(name = "fm_sex")
    private String fm_sex; // 性別

    @Column(name = "fm_postcode")
    private String fm_postcode; // 郵便番号

    @Column(name = "fm_address")
    private String fm_address; // 住所

    @Column(name = "fm_phone")
    private String fm_phone; // 携帯

    @Column(name = "fm_money")
    private Integer fm_money; // 収入

    // 緊急連絡先かどうか これによって判断
    // 1が緊急連絡先、0が扶養家族
    @Column(name = "fm_status")
    private Integer fm_status;

}
