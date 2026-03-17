package com.djxlzk.dsasystem.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HoursStatsDTO {
    private BigDecimal totalHours;
    private BigDecimal completedHours;
    private BigDecimal remainingHours;
    private Integer completedSessions;
}
