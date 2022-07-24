package jp.co.seamaple.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class DateForm {

    private String userId;

    private String userName;

    @DateTimeFormat(pattern = "yyyyMM")
    private Date monthYear;
}
