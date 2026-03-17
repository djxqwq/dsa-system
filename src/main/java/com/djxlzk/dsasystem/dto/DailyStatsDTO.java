package com.djxlzk.dsasystem.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DailyStatsDTO {
    private String date;
    private Long bookings;
    private BigDecimal hours;
    private Long completed;
    private Long cancelled;
}
