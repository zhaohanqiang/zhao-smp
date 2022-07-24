package jp.co.seamaple.form;

import lombok.Data;

import java.io.Serializable;

@Data
public class VacationAppApprovalForm implements Serializable {

    private Integer vacationappNo; // 申請番号

    private Integer appStatus; // 承認ステータス

    private String updateFlag; // 否認フラグ
}
