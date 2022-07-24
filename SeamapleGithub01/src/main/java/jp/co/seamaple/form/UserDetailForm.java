package jp.co.seamaple.form;

import java.io.Serializable;

import javax.validation.GroupSequence;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import jp.co.seamaple.config.GroupValidation.Group1;
import jp.co.seamaple.config.GroupValidation.Group2;
import jp.co.seamaple.config.GroupValidation.Group3;
import lombok.Data;

@Data
public class UserDetailForm extends UserFmForm implements Serializable {

  private String userId; // 社員番号

  @NotBlank(message = "{blank_check}", groups = Group1.class)
  private String userName; // 社員氏名

  @NotBlank(message = "{blank_check}", groups = Group1.class)
  @Pattern(regexp = "^[\u30a0-\u30ff]+$", message = "全角カタカナを入力してください。", groups = Group2.class)
  private String furigana; // フリガナ

  @NotBlank(message = "{blank_check}", groups = Group1.class)
  private String sex; // 性別

  @NotBlank(message = "{blank_check}", groups = Group1.class)
  @Pattern(regexp = "\\d{4}-\\d{1,3}-\\d{1,3}", message = "yyyy-MM-ddの形式で入力してください。", groups = Group2.class)

  private String birthday; // 生年月日

  @NotBlank(message = "{blank_check}", groups = Group1.class)
  private String lastname; // 英語姓

  @NotBlank(message = "{blank_check}", groups = Group1.class)
  private String firstname; // 英語名

  @NotBlank(message = "{blank_check}", groups = Group1.class)
  @Pattern(regexp = "^\\d{3}-\\d{4}$", message = "000-0000の形式で入力してください。", groups = Group2.class)
  private String postcode; // 郵便番号

  @NotBlank(message = "{blank_check}", groups = Group1.class)
  private String address; // 住所

  @NotBlank(message = "{blank_check}", groups = Group1.class)
  @Email(message = "123456@gmail.comの形式で入力してください。", groups = Group2.class)
  private String mail; // メールアドレス

  @NotBlank(message = "{blank_check}", groups = Group1.class)
  @Pattern(regexp = "0\\d{1,4}-\\d{1,4}-\\d{4}", message = "000-0000-0000の形式で入力してください。", groups = Group2.class)
  private String phone; // 電話番号

//  @NotBlank(message = "{blank_check}", groups = Group1.class)
  private String stay; // 在留資格

//  @NotBlank(message = "{blank_check}", groups = Group1.class)
  private String time; // 在留期間

//  @NotBlank(message = "{blank_check}", groups = Group1.class)
  private String limit; // 有効期限

  @NotBlank(message = "{blank_check}", groups = Group1.class)
  private String contory; // 国籍

  @GroupSequence({ Group1.class, Group2.class, Group3.class })
  public interface GroupValidation {
  }
}
