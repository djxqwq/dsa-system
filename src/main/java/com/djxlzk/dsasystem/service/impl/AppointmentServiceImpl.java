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
        Schedule schedule = scheduleMapper.selectById(dto.getScheduleId());
        if (schedule == null) {
            return ResultDTO.error(404, "排班不存在");
        }

        if (schedule.getStatus() != 1) {
            return ResultDTO.error(400, "该时段未开放预约");
        }

        if (schedule.getBookedCount() >= schedule.getCapacity()) {
            return ResultDTO.error(400, "该时段已约满");
        }

        int existingCount = appointmentMapper.countByScheduleAndStudent(dto.getScheduleId(), studentId);
        if (existingCount > 0) {
            return ResultDTO.error(400, "您已预约过该时段");
        }

        Appointment appointment = new Appointment();
        appointment.setScheduleId(dto.getScheduleId());
        appointment.setStudentId(studentId);
        appointment.setCoachId(dto.getCoachId() != null ? dto.getCoachId() : schedule.getCoachId());
        appointment.setVehicleId(dto.getVehicleId());
        appointment.setAppointmentDate(schedule.getScheduleDate());
        appointment.setStartTime(schedule.getStartTime());
        appointment.setEndTime(schedule.getEndTime());
        appointment.setStatus(0);
        appointment.setRemark(dto.getRemark());

        appointmentMapper.insert(appointment);
        scheduleMapper.updateBookedCount(dto.getScheduleId(), 1);

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

        appointment.setStatus(3);
        appointmentMapper.updateById(appointment);
        scheduleMapper.updateBookedCount(appointment.getScheduleId(), -1);

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
