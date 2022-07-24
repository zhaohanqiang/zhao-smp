package jp.co.seamaple.form;

import jp.co.seamaple.config.GroupValidation.Group1;
import jp.co.seamaple.config.GroupValidation.Group2;
import jp.co.seamaple.config.GroupValidation.Group3;
import lombok.Data;

import javax.validation.GroupSequence;
import javax.validation.constraints.*;
import java.io.Serializable;

@Data
public class UserEditForm implements Serializable {

    @NotBlank(message = "{blank_check}")
    private String userId; // 社員番号

    @NotBlank(message = "{blank_check}")
    private String userName; // 氏名

    @NotBlank(message = "{blank_check}", groups = Group1.class)
    @Email(message = "{email_check}", groups = Group2.class)
    private String mailAddress; // メールアドレス

    @NotBlank(message = "{blank_check}", groups = Group1.class)
    @Pattern(regexp = "0\\d{1,4}-\\d{1,4}-\\d{4}", message = "{pattern_check}", groups = Group2.class)
    private String phoneNumber; // 電話番号

    @NotNull(message = "{not_null_check}", groups = Group1.class)
    @Min(value = 0, message = "{min_check}", groups = Group2.class)
    @Max(value = 20, message = "{max_check}", groups = Group3.class)
    private int lastPaidVacation; // 最新付与の有給休暇日数

    @NotNull(message = "{not_null_check}", groups = Group1.class)
    @Min(value = 0, message = "{min_check}", groups = Group2.class)
    @Max(value = 20, message = "{max_check}", groups = Group3.class)
    private int previousPaidVacation; // 前々回付与の有給休暇日数

    @NotNull(message = "{not_null_check}", groups = Group1.class)
    @Min(value = 0, message = "{min_check}", groups = Group2.class)
    @Max(value = 2, message = "{max_check}", groups = Group3.class)
    private int summerVacation; // 夏季休暇日数

    @GroupSequence({Group1.class, Group2.class, Group3.class})
    public interface GroupValidation {
    }

}
