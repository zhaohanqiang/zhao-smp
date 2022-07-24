package jp.co.seamaple.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.util.Date;

@Data
@Component
@SessionScope
public class SessionData implements Serializable {
    private static final long serialVersionUID = 1L;
    String userId;
    String userName;
    String roleId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date vacationappDate;
    Integer appStatus;
}