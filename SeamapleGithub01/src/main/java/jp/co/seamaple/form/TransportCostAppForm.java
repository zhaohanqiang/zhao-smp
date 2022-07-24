package jp.co.seamaple.form;

import lombok.Data;

import java.io.Serializable;

@Data
public class TransportCostAppForm implements Serializable {

    // @NotNull(message = "{null_check}")
    private Integer type;

    private String monthYear;

    // @NotNull(message = "{null_check}")
    private Integer day;

    // @NotBlank(message = "{blank_check}")
    // @Size(max = 10, message = "{max_size_check}")
    private String departure;

    // @NotBlank(message = "{blank_check}")
    // @Size(max = 10, message = "{max_size_check}")
    private String arrival;

    // @NotBlank(message = "{blank_check}")
    // @Size(max = 6, message = "{max_size_check}")
    // @Pattern(regexp = "^[0-9]*$", message = "{pattern_check_half-width_digit}")
    private String cost;

    // @NotBlank(message = "{blank_check}")
    // @Size(max = 200, message = "{max_size_check}")
    private String reason;

    private Boolean disabled;
}
