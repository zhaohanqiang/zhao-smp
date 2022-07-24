package jp.co.seamaple.form;

import lombok.Data;

import jp.co.seamaple.config.GroupValidation.Group1;
import jp.co.seamaple.config.GroupValidation.Group2;
import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class UserFmForm implements Serializable {

    private String user_fmid; // 社員番号

    private String fm_furigana; // フリガナ

    private String fm_sex; // 性別

    private String fm_birth; // 親友誕生日

    @NotBlank(message = "{blank_check}", groups = Group1.class)
    private String fm_name; // 緊急連絡先の名前

    private String fm_nameb; // 扶養家族の名前

    private String fm_tel; // TEL

    @NotBlank(message = "{blank_check}", groups = Group1.class)
    private String fm_relation; // 緊急連絡先続柄

    private String fm_relationb; // 扶養家族続柄

    private String fm_job; // 職業

    @NotBlank(message = "{blank_check}", groups = Group1.class)
    @Pattern(regexp = "^\\d{3}-\\d{4}$", message = "000-0000の形式で入力してください。", groups = Group2.class)
    private String fm_postcode; // 郵便番号

    @NotBlank(message = "{blank_check}", groups = Group1.class)
    private String fm_address; // 緊急連絡先住所

    private String fm_addressb; // 扶養家族住所

    @NotBlank(message = "{blank_check}", groups = Group1.class)
    @Pattern(regexp = "0\\d{1,4}-\\d{1,4}-\\d{4}", message = "000-0000-0000の形式で入力してください。", groups = Group2.class)
    private String fm_phone; // 携帯

    private Integer fm_money; // 収入

    private Integer fm_status; // fm_status
    // 緊急連絡先かどうか これによって判断
    // 1が緊急連絡先、0が扶養家族

    @GroupSequence({ jp.co.seamaple.config.GroupValidation.Group1.class,
            jp.co.seamaple.config.GroupValidation.Group2.class, jp.co.seamaple.config.GroupValidation.Group3.class })
    public interface GroupValidation {
    }

}
