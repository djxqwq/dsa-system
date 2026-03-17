package com.djxlzk.dsasystem.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DashboardStatsDTO {
    private Long todayBookings;
    private Long pendingConfirm;
    private Long activeCoaches;
    private Long availableVehicles;
    private Long totalStudents;
    private Long totalCoaches;
    private Long totalVehicles;
    private BigDecimal todayHours;
    private BigDecimal totalCompletedHours;
    private Long totalCompletedSessions;
    private Double confirmRate;
    private Double completionRate;
    private Double noShowRate;
    private List<DailyStatsDTO> weeklyTrend;
    private List<CoachStatsDTO> topCoaches;
    private List<HourlyDistributionDTO> hourlyDistribution;
}
