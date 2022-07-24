package jp.co.seamaple.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "m_usermore")
public class UserDetail implements Serializable {

    @Id
    @Column(name = "um_id")
    private String userId; // 社員番号

    @Column(name = "um_name")
    private String userName; // 社員番号

    @Column(name = "um_furigana")
    private String furigana; // フリガナ

    @Column(name = "um_birth")
    private String birthday; // 社員誕生日

    @Column(name = "um_sex")
    private String sex; // 性別

    @Column(name = "um_lastname")
    private String lastname; // 英語姓

    @Column(name = "um_firstname")
    private String firstname; // 英語名

    @Column(name = "um_postcode")
    private String postcode; // 郵便番号

    @Column(name = "um_address")
    private String address; // 住所

    @Column(name = "um_mail")
    private String mail; // メールアドレス

    @Column(name = "um_phone")
    private String phone; // 携帯

    @Column(name = "um_stay")
    private String stay; // 在留資格

    @Column(name = "um_time")
    private String time; // 在留期間

    @Column(name = "um_limit")
    private String limit; // 有効期限

    @Column(name = "um_contory")
    private String contory; // 国籍


}
