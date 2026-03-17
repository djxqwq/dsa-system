package com.djxlzk.dsasystem.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class HoursRecordDTO {
    private Long id;
    private LocalDate date;
    private String studentName;
    private String coachName;
    private BigDecimal hours;
    private String remark;
    private Integer status;
    private String statusText;
}
