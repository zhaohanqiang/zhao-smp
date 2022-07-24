package jp.co.seamaple.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class SearchForm implements Serializable {

    private String searchUserId; // 社員番号 管理者用

    private String searchUserName; // 社員名 管理者用

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date searchStartDate; // 検索開始日

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date searchEndDate; // 検索末日

    private Integer searchAppStatus; // 検索ステータス
}
