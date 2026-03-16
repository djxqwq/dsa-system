package com.djxlzk.dsasystem.controller;

import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.dto.ScheduleDTO;
import com.djxlzk.dsasystem.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping
    public ResultDTO<?> createSchedule(@RequestBody ScheduleDTO dto, Authentication auth) {
        Long coachId = (Long) auth.getPrincipal();
        return scheduleService.createSchedule(dto, coachId);
    }

    @PutMapping
    public ResultDTO<?> updateSchedule(@RequestBody ScheduleDTO dto) {
        return scheduleService.updateSchedule(dto);
    }

    @DeleteMapping("/{id}")
    public ResultDTO<?> deleteSchedule(@PathVariable Long id) {
        return scheduleService.deleteSchedule(id);
    }

    @PutMapping("/toggle/{id}")
    public ResultDTO<?> toggleStatus(@PathVariable Long id) {
        return scheduleService.toggleStatus(id);
    }

    @GetMapping("/coach")
    public ResultDTO<?> getCoachSchedules(
            Authentication auth,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        Long coachId = (Long) auth.getPrincipal();
        return scheduleService.getCoachSchedules(coachId, startDate);
    }

    @GetMapping("/available")
    public ResultDTO<?> getAvailableSlots(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Long coachId) {
        return scheduleService.getAvailableSlots(date, coachId);
    }
}
