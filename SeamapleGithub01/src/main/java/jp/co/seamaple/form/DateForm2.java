package jp.co.seamaple.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class DateForm2 {

    private String userId;

    private String userName;

    @DateTimeFormat(pattern = "yyyy")
    private Date monthYear;
}
