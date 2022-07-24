package jp.co.seamaple.form;

import lombok.Data;

import java.io.Serializable;

@Data
public class TransportCostListForm implements Serializable {

    public String[] cost;
    private Integer[] type;
    private String[] monthYear;
    private Integer[] day;
    private String[] departure;
    private String[] arrival;
    private String[] reason;

    private Boolean[] disabled;
}