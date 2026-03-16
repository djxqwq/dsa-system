package com.djxlzk.dsasystem.service.impl;

import com.djxlzk.dsasystem.dto.AppointmentCreateDTO;
import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.entity.Appointment;
import com.djxlzk.dsasystem.entity.Schedule;
import com.djxlzk.dsasystem.entity.Student;
import com.djxlzk.dsasystem.mapper.AppointmentMapper;
import com.djxlzk.dsasystem.mapper.ScheduleMapper;
import com.djxlzk.dsasystem.mapper.StudentMapper;
import com.djxlzk.dsasystem.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Override
    @Transactional
    public ResultDTO<?> createAppointment(AppointmentCreateDTO dto, Long studentId) {
        LocalDate appointmentDate = LocalDate.parse(dto.getAppointmentDate());
        LocalTime startTime = LocalTime.parse(dto.getStartTime());
        LocalTime endTime = LocalTime.parse(dto.getEndTime());

        long durationMinutes = java.time.Duration.between(startTime, endTime).toMinutes();
        if (durationMinutes < 60) {
            return ResultDTO.error(400, "预约时长最少1小时");
        }

        List<Schedule> availableSlots = scheduleMapper.findAvailableSlotsByCoach(appointmentDate, dto.getCoachId());

        Schedule matchedSlot = null;
        for (Schedule slot : availableSlots) {
            boolean isWithinSlot = !startTime.isBefore(slot.getStartTime()) && !endTime.isAfter(slot.getEndTime());
            if (isWithinSlot && slot.getStatus() == 1 && slot.getRemainingCapacity() != null
                    && slot.getRemainingCapacity() > 0) {
                matchedSlot = slot;
                break;
            }
        }

        if (matchedSlot == null) {
            return ResultDTO.error(400, "该时间段未开放预约或已约满");
        }

        int existingCount = appointmentMapper.countByDateAndTime(studentId, appointmentDate, startTime, endTime);
        if (existingCount > 0) {
            return ResultDTO.error(400, "您在该时间段已有预约");
        }

        Appointment appointment = new Appointment();
        appointment.setStudentId(studentId);
        appointment.setCoachId(dto.getCoachId());
        appointment.setVehicleId(dto.getVehicleId());
        appointment.setAppointmentDate(appointmentDate);
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);
        appointment.setStatus(0);
        appointment.setRemark(dto.getRemark());

        appointmentMapper.insert(appointment);
        scheduleMapper.updateBookedCount(matchedSlot.getId(), 1);

        return ResultDTO.success("预约成功，等待教练确认", appointment);
    }

    @Override
    @Transactional
    public ResultDTO<?> cancelAppointment(Long id, Long userId, String role) {
        Appointment appointment = appointmentMapper.selectById(id);
        if (appointment == null) {
            return ResultDTO.error(404, "预约不存在");
        }

        if ("student".equals(role) && !appointment.getStudentId().equals(userId)) {
            return ResultDTO.error(403, "无权操作此预约");
        }
        if ("coach".equals(role) && !appointment.getCoachId().equals(userId)) {
            return ResultDTO.error(403, "无权操作此预约");
        }

        if (appointment.getStatus() == 3) {
            return ResultDTO.error(400, "该预约已取消");
        }
        if (appointment.getStatus() == 2) {
            return ResultDTO.error(400, "已完成的预约无法取消");
        }

        LocalDate appointmentDate = appointment.getAppointmentDate();
        LocalTime startTime = appointment.getStartTime();
        LocalTime endTime = appointment.getEndTime();

        appointment.setStatus(3);
        appointmentMapper.updateById(appointment);

        List<Schedule> schedules = scheduleMapper.findAvailableSlotsByCoach(appointmentDate, appointment.getCoachId());
        for (Schedule slot : schedules) {
            boolean isWithinSlot = !startTime.isBefore(slot.getStartTime()) && !endTime.isAfter(slot.getEndTime());
            if (isWithinSlot) {
                scheduleMapper.updateBookedCount(slot.getId(), -1);
                break;
            }
        }

        if ("student".equals(role)) {
            Student student = studentMapper.selectById(userId);
            if (student != null && student.getNoShowCount() != null) {
                student.setNoShowCount(student.getNoShowCount() + 1);
                studentMapper.updateById(student);
            }
        }

        return ResultDTO.success("预约已取消", null);
    }

    @Override
    @Transactional
    public ResultDTO<?> confirmAppointment(Long id, Long coachId) {
        Appointment appointment = appointmentMapper.selectById(id);
        if (appointment == null) {
            return ResultDTO.error(404, "预约不存在");
        }

        if (!appointment.getCoachId().equals(coachId)) {
            return ResultDTO.error(403, "无权操作此预约");
        }

        if (appointment.getStatus() != 0) {
            return ResultDTO.error(400, "只能确认待确认状态的预约");
        }

        appointment.setStatus(1);
        appointmentMapper.updateById(appointment);
        return ResultDTO.success("预约已确认", null);
    }

    @Override
    @Transactional
    public ResultDTO<?> completeAppointment(Long id, Long coachId) {
        Appointment appointment = appointmentMapper.selectById(id);
        if (appointment == null) {
            return ResultDTO.error(404, "预约不存在");
        }

        if (!appointment.getCoachId().equals(coachId)) {
            return ResultDTO.error(403, "无权操作此预约");
        }

        if (appointment.getStatus() != 1) {
            return ResultDTO.error(400, "只能完成已确认状态的预约");
        }

        appointment.setStatus(2);
        appointmentMapper.updateById(appointment);
        return ResultDTO.success("预约已完成", null);
    }

    @Override
    @Transactional
    public ResultDTO<?> markNoShow(Long id, Long coachId) {
        Appointment appointment = appointmentMapper.selectById(id);
        if (appointment == null) {
            return ResultDTO.error(404, "预约不存在");
        }

        if (!appointment.getCoachId().equals(coachId)) {
            return ResultDTO.error(403, "无权操作此预约");
        }

        if (appointment.getStatus() != 1) {
            return ResultDTO.error(400, "只能标记已确认状态的预约为爽约");
        }

        appointment.setStatus(4);
        appointmentMapper.updateById(appointment);

        Student student = studentMapper.selectById(appointment.getStudentId());
        if (student != null) {
            if (student.getNoShowCount() == null) {
                student.setNoShowCount(1);
            } else {
                student.setNoShowCount(student.getNoShowCount() + 1);
            }
            studentMapper.updateById(student);
        }

        return ResultDTO.success("已标记为爽约", null);
    }

    @Override
    public ResultDTO<?> getStudentAppointments(Long studentId) {
        List<Appointment> appointments = appointmentMapper.findByStudentId(studentId);
        return ResultDTO.success(appointments);
    }

    @Override
    public ResultDTO<?> getCoachAppointments(Long coachId, Integer status) {
        List<Appointment> appointments;
        if (status != null) {
            appointments = appointmentMapper.findByCoachIdAndStatus(coachId, status);
        } else {
            appointments = appointmentMapper.findByCoachId(coachId);
        }
        return ResultDTO.success(appointments);
    }

    @Override
    public ResultDTO<?> getActiveAppointments(Long studentId) {
        List<Appointment> appointments = appointmentMapper.findActiveByStudentId(studentId);
        return ResultDTO.success(appointments);
    }

    @Override
    public Appointment getById(Long id) {
        return appointmentMapper.selectById(id);
    }
}
