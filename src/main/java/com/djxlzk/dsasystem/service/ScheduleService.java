package com.djxlzk.dsasystem.service;

import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.dto.ScheduleDTO;
import com.djxlzk.dsasystem.entity.Schedule;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

    ResultDTO<?> createSchedule(ScheduleDTO dto, Long coachId);

    ResultDTO<?> updateSchedule(ScheduleDTO dto);

    ResultDTO<?> deleteSchedule(Long id);

    ResultDTO<?> toggleStatus(Long id);

    ResultDTO<?> getCoachSchedules(Long coachId, LocalDate startDate);

    ResultDTO<?> getAvailableSlots(LocalDate date, Long coachId);

    Schedule getById(Long id);
}
