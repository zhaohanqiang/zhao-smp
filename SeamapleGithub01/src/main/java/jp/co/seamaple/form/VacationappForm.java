package jp.co.seamaple.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
public class VacationappForm implements Serializable {

    @NotBlank(message = "{blank_check}")
    private String vacationAppType; // 休暇種別

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "{not_null_check}")
    private Date vacationappDate; // 休暇日程

    @Size(max = 100, message = "{max_size_check}")
    private String vacationappComment; // コメント

}