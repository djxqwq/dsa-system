package com.djxlzk.dsasystem.service.impl;

import com.djxlzk.dsasystem.dto.CoachStatsDTO;
import com.djxlzk.dsasystem.dto.DailyStatsDTO;
import com.djxlzk.dsasystem.dto.DashboardStatsDTO;
import com.djxlzk.dsasystem.dto.HourlyDistributionDTO;
import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.mapper.AppointmentMapper;
import com.djxlzk.dsasystem.mapper.CoachMapper;
import com.djxlzk.dsasystem.mapper.StudentMapper;
import com.djxlzk.dsasystem.mapper.VehicleMapper;
import com.djxlzk.dsasystem.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StatsServiceImpl implements StatsService {

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private CoachMapper coachMapper;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Override
    public ResultDTO<DashboardStatsDTO> getDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        LocalDate today = LocalDate.now();

        stats.setTodayBookings(appointmentMapper.countByDate(today));
        stats.setPendingConfirm(appointmentMapper.countPending());

        Long activeCoaches = coachMapper.selectCount(null);
        stats.setActiveCoaches(activeCoaches);

        Long availableVehicles = vehicleMapper.selectCount(null);
        stats.setAvailableVehicles(availableVehicles);

        Long totalStudents = studentMapper.selectCount(null);
        stats.setTotalStudents(totalStudents);

        Long totalCoaches = coachMapper.selectCount(null);
        stats.setTotalCoaches(totalCoaches);

        Long totalVehicles = vehicleMapper.selectCount(null);
        stats.setTotalVehicles(totalVehicles);

        BigDecimal todayHours = appointmentMapper.sumHoursByDate(today);
        stats.setTodayHours(todayHours != null ? todayHours.setScale(1, RoundingMode.HALF_UP) : BigDecimal.ZERO);

        BigDecimal totalHours = appointmentMapper.sumTotalCompletedHours();
        stats.setTotalCompletedHours(totalHours != null ? totalHours.setScale(1, RoundingMode.HALF_UP) : BigDecimal.ZERO);

        stats.setTotalCompletedSessions(appointmentMapper.countTotalCompletedSessions());

        Map<String, Object> statusStats = appointmentMapper.getStatusStats();
        if (statusStats != null && statusStats.get("total") != null) {
            Long total = ((Number) statusStats.get("total")).longValue();
            if (total > 0) {
                Long confirmed = statusStats.get("confirmed") != null ? ((Number) statusStats.get("confirmed")).longValue() : 0L;
                Long completed = statusStats.get("completed") != null ? ((Number) statusStats.get("completed")).longValue() : 0L;
                Long noShow = statusStats.get("noShow") != null ? ((Number) statusStats.get("noShow")).longValue() : 0L;

                Long totalProcessed = confirmed + completed;
                if (totalProcessed > 0) {
                    stats.setConfirmRate((double) completed / totalProcessed * 100);
                } else {
                    stats.setConfirmRate(0.0);
                }

                stats.setCompletionRate((double) completed / total * 100);
                stats.setNoShowRate((double) noShow / total * 100);
            } else {
                stats.setConfirmRate(0.0);
                stats.setCompletionRate(0.0);
                stats.setNoShowRate(0.0);
            }
        } else {
            stats.setConfirmRate(0.0);
            stats.setCompletionRate(0.0);
            stats.setNoShowRate(0.0);
        }

        LocalDate endDate = today;
        LocalDate startDate = today.minusDays(6);
        List<Map<String, Object>> dailyStatsRaw = appointmentMapper.findDailyStats(startDate, endDate);
        List<DailyStatsDTO> weeklyTrend = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            DailyStatsDTO dailyStats = new DailyStatsDTO();
            dailyStats.setDate(date.format(formatter));

            Map<String, Object> found = dailyStatsRaw.stream()
                    .filter(m -> date.equals(((java.sql.Date) m.get("date")).toLocalDate()))
                    .findFirst()
                    .orElse(null);

            if (found != null) {
                dailyStats.setBookings(((Number) found.get("bookings")).longValue());
                dailyStats.setHours(found.get("hours") != null ? new BigDecimal(found.get("hours").toString()).setScale(1, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                dailyStats.setCompleted(((Number) found.get("completed")).longValue());
                dailyStats.setCancelled(((Number) found.get("cancelled")).longValue());
            } else {
                dailyStats.setBookings(0L);
                dailyStats.setHours(BigDecimal.ZERO);
                dailyStats.setCompleted(0L);
                dailyStats.setCancelled(0L);
            }
            weeklyTrend.add(dailyStats);
        }
        stats.setWeeklyTrend(weeklyTrend);

        List<Map<String, Object>> topCoachesRaw = appointmentMapper.findTopCoaches();
        List<CoachStatsDTO> topCoaches = new ArrayList<>();
        for (Map<String, Object> row : topCoachesRaw) {
            CoachStatsDTO coachStats = new CoachStatsDTO();
            coachStats.setCoachId(((Number) row.get("coachId")).longValue());
            coachStats.setCoachName((String) row.get("coachName"));
            coachStats.setTotalSessions(((Number) row.get("totalSessions")).longValue());
            coachStats.setTotalHours(row.get("totalHours") != null ? new BigDecimal(row.get("totalHours").toString()).setScale(1, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            coachStats.setCompletedSessions(((Number) row.get("completedSessions")).longValue());
            if (coachStats.getTotalSessions() > 0) {
                coachStats.setCompletionRate((double) coachStats.getCompletedSessions() / coachStats.getTotalSessions() * 100);
            } else {
                coachStats.setCompletionRate(0.0);
            }
            topCoaches.add(coachStats);
        }
        stats.setTopCoaches(topCoaches);

        List<Map<String, Object>> hourlyRaw = appointmentMapper.findHourlyDistribution();
        List<HourlyDistributionDTO> hourlyDistribution = new ArrayList<>();
        for (int hour = 6; hour <= 21; hour++) {
            HourlyDistributionDTO dto = new HourlyDistributionDTO();
            dto.setHour(hour);

            final int h = hour;
            Map<String, Object> found = hourlyRaw.stream()
                    .filter(m -> m.get("hour") != null && ((Number) m.get("hour")).intValue() == h)
                    .findFirst()
                    .orElse(null);

            if (found != null) {
                dto.setCount(((Number) found.get("count")).longValue());
            } else {
                dto.setCount(0L);
            }
            hourlyDistribution.add(dto);
        }
        stats.setHourlyDistribution(hourlyDistribution);

        return ResultDTO.success(stats);
    }
}
