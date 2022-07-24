package jp.co.seamaple.form;

import jp.co.seamaple.config.GroupValidation.Group1;
import jp.co.seamaple.config.GroupValidation.Group2;
import jp.co.seamaple.config.GroupValidation.Group3;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class ChangePassForm implements Serializable {

    @NotBlank(message = "{blank_check}", groups = Group1.class)
    @Length(min = 8, max = 16, message = "{length_check}", groups = Group2.class)
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "{pass_pattern_check}", groups = Group3.class)
    String newPass; // 旧パスワード

    @NotBlank(message = "{blank_check}", groups = Group1.class)
    @Length(min = 8, max = 16, message = "{length_check}", groups = Group2.class)
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "{pass_pattern_check}", groups = Group3.class)
    String confirmedPass; // 新パスワード

    @GroupSequence({Group1.class, Group2.class, Group3.class})
    public interface GroupValidation {
    }
}
