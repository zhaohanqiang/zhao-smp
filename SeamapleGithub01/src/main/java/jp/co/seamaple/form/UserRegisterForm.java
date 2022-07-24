package jp.co.seamaple.form;

import jp.co.seamaple.config.GroupValidation.Group1;
import jp.co.seamaple.config.GroupValidation.Group2;
import jp.co.seamaple.config.GroupValidation.Group3;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.GroupSequence;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

@Data
public class UserRegisterForm implements Serializable {

    @NotBlank(message = "{blank_check}")
    private String userId; // 社員番号

    @NotBlank(message = "{blank_check}")
    private String userName; // 氏名

    @NotBlank(message = "{blank_check}", groups = Group1.class)
    @Length(min = 8, max = 16, message = "{length_check}", groups = Group2.class)
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "{pass_pattern_check}", groups = Group3.class)
    private String password; // パスワード

    @NotBlank(message = "{blank_check}", groups = Group1.class)
    @Email(message = "{email_check}", groups = Group2.class)
    private String mailAddress; // メールアドレス

    @NotBlank(message = "{blank_check}", groups = Group1.class)
    @Pattern(regexp = "0\\d{1,4}-\\d{1,4}-\\d{4}", message = "{pattern_check}", groups = Group2.class)
    private String phoneNumber; // 電話番号

    @NotNull(message = "{not_null_check}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date joinCompanyDate; // 入社日

    @NotBlank(message = "{blank_check}")
    private String roleId; // ユーザー権限

    @GroupSequence({Group1.class, Group2.class, Group3.class})
    public interface GroupValidation {
    }
}
