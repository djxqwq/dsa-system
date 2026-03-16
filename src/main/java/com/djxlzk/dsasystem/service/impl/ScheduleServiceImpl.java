package com.djxlzk.dsasystem.service.impl;

import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.dto.ScheduleDTO;
import com.djxlzk.dsasystem.entity.Schedule;
import com.djxlzk.dsasystem.mapper.AppointmentMapper;
import com.djxlzk.dsasystem.mapper.ScheduleMapper;
import com.djxlzk.dsasystem.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Override
    @Transactional
    public ResultDTO<?> createSchedule(ScheduleDTO dto, Long coachId) {
        LocalTime startTime = LocalTime.parse(dto.getStartTime());
        LocalTime endTime = LocalTime.parse(dto.getEndTime());

        long durationMinutes = Duration.between(startTime, endTime).toMinutes();
        if (durationMinutes < 120) {
            return ResultDTO.error(400, "排班时长最少2小时");
        }

        int slotCount = (int) (durationMinutes / 30);
        LocalDate scheduleDate = LocalDate.parse(dto.getScheduleDate());
        Integer capacity = dto.getCapacity() != null ? dto.getCapacity() : 3;
        Integer status = dto.getStatus() != null ? dto.getStatus() : 1;

        int createdCount = 0;
        int skippedCount = 0;

        for (int i = 0; i < slotCount; i++) {
            LocalTime slotStart = startTime.plusMinutes(i * 30);
            LocalTime slotEnd = slotStart.plusMinutes(30);

            int exists = scheduleMapper.countByCoachDateAndTime(coachId, scheduleDate, slotStart, slotEnd);
            if (exists > 0) {
                skippedCount++;
                continue;
            }

            Schedule schedule = new Schedule();
            schedule.setCoachId(coachId);
            schedule.setScheduleDate(scheduleDate);
            schedule.setStartTime(slotStart);
            schedule.setEndTime(slotEnd);
            schedule.setCapacity(capacity);
            schedule.setBookedCount(0);
            schedule.setStatus(status);

            scheduleMapper.insert(schedule);
            createdCount++;
        }

        if (createdCount == 0) {
            return ResultDTO.error(400, "所选时间段均已存在排班");
        }

        String message = "排班创建成功，共创建 " + createdCount + " 个半小时时段";
        if (skippedCount > 0) {
            message += "，跳过 " + skippedCount + " 个已存在时段";
        }

        return ResultDTO.success(message, null);
    }

    @Override
    @Transactional
    public ResultDTO<?> updateSchedule(ScheduleDTO dto) {
        Schedule existing = scheduleMapper.selectById(dto.getId());
        if (existing == null) {
            return ResultDTO.error(404, "排班不存在");
        }

        if (dto.getScheduleDate() != null) {
            existing.setScheduleDate(LocalDate.parse(dto.getScheduleDate()));
        }
        if (dto.getStartTime() != null) {
            existing.setStartTime(LocalTime.parse(dto.getStartTime()));
        }
        if (dto.getEndTime() != null) {
            existing.setEndTime(LocalTime.parse(dto.getEndTime()));
        }
        if (dto.getCapacity() != null) {
            existing.setCapacity(dto.getCapacity());
        }
        if (dto.getStatus() != null) {
            existing.setStatus(dto.getStatus());
        }

        scheduleMapper.updateById(existing);
        return ResultDTO.success("排班更新成功", existing);
    }

    @Override
    @Transactional
    public ResultDTO<?> deleteSchedule(Long id) {
        Schedule existing = scheduleMapper.selectById(id);
        if (existing == null) {
            return ResultDTO.error(404, "排班不存在");
        }

        if (existing.getBookedCount() > 0) {
            return ResultDTO.error(400, "该时段已有预约，无法删除");
        }

        scheduleMapper.deleteById(id);
        return ResultDTO.success("排班删除成功", null);
    }

    @Override
    @Transactional
    public ResultDTO<?> toggleStatus(Long id) {
        Schedule existing = scheduleMapper.selectById(id);
        if (existing == null) {
            return ResultDTO.error(404, "排班不存在");
        }

        existing.setStatus(existing.getStatus() == 1 ? 0 : 1);
        scheduleMapper.updateById(existing);
        return ResultDTO.success(existing.getStatus() == 1 ? "已开放预约" : "已关闭预约", existing);
    }

    @Override
    public ResultDTO<?> getCoachSchedules(Long coachId, LocalDate startDate) {
        if (startDate == null) {
            startDate = LocalDate.now();
        }
        List<Schedule> schedules = scheduleMapper.findByCoachIdFromDate(coachId, startDate);

        for (Schedule schedule : schedules) {
            List<String> studentNames = appointmentMapper.findStudentNamesBySchedule(
                    coachId,
                    schedule.getScheduleDate(),
                    schedule.getStartTime(),
                    schedule.getEndTime());
            schedule.setStudentNames(studentNames);
        }

        return ResultDTO.success(schedules);
    }

    @Override
    public ResultDTO<?> getAvailableSlots(LocalDate date, Long coachId) {
        List<Schedule> slots;
        if (coachId != null) {
            slots = scheduleMapper.findAvailableSlotsByCoach(date, coachId);
        } else {
            slots = scheduleMapper.findAvailableSlots(date);
        }
        return ResultDTO.success(slots);
    }

    @Override
    public Schedule getById(Long id) {
        return scheduleMapper.selectById(id);
    }
}
