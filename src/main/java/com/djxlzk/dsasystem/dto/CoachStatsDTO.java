package com.djxlzk.dsasystem.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CoachStatsDTO {
    private Long coachId;
    private String coachName;
    private Long totalSessions;
    private BigDecimal totalHours;
    private Long completedSessions;
    private Double completionRate;
}
