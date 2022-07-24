package jp.co.seamaple.form;

import jp.co.seamaple.config.GroupValidation.Group1;
import jp.co.seamaple.config.GroupValidation.Group2;
import jp.co.seamaple.config.GroupValidation.Group3;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class WorkScheduleForm implements Serializable {

    private String userId;

    @NotBlank(message = "{blank_check}")
    private String monthYear;

    @NotNull(message = "{not_null_check}")
    private Float workHour;

    private MultipartFile fileContent;

    @NotBlank(message = "{blank_check}")
    private String weekendsWork;

    private String memo;

    @GroupSequence({Group1.class, Group2.class, Group3.class})
    public interface GroupValidation {
    }
}
