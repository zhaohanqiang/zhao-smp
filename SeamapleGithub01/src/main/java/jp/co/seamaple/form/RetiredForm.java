package jp.co.seamaple.form;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class RetiredForm implements Serializable {

    private String userId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date retiredDate;

}
