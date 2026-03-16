package com.djxlzk.dsasystem.service.impl;

import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.dto.ScheduleDTO;
import com.djxlzk.dsasystem.entity.Schedule;
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

    @Override
    @Transactional
    public ResultDTO<?> createSchedule(ScheduleDTO dto, Long coachId) {
        LocalTime startTime = LocalTime.parse(dto.getStartTime());
        LocalTime endTime = LocalTime.parse(dto.getEndTime());

        long durationMinutes = Duration.between(startTime, endTime).toMinutes();
        if (durationMinutes < 120) {
            return ResultDTO.error(400, "排班时长最少2小时");
        }

        Schedule schedule = new Schedule();
        schedule.setCoachId(coachId);
        schedule.setScheduleDate(LocalDate.parse(dto.getScheduleDate()));
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        schedule.setCapacity(dto.getCapacity() != null ? dto.getCapacity() : 1);
        schedule.setBookedCount(0);
        schedule.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);

        scheduleMapper.insert(schedule);
        return ResultDTO.success("排班创建成功", schedule);
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
