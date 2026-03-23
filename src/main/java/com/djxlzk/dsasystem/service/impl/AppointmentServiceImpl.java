package com.djxlzk.dsasystem.service.impl;

import com.djxlzk.dsasystem.dto.AppointmentCreateDTO;
import com.djxlzk.dsasystem.dto.AppointmentUpdateDTO;
import com.djxlzk.dsasystem.dto.HoursRecordDTO;
import com.djxlzk.dsasystem.dto.HoursStatsDTO;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

        int slotCount = (int) (durationMinutes / 30);
        List<Schedule> availableSlots = scheduleMapper.findAvailableSlotsByCoach(appointmentDate, dto.getCoachId());

        List<Schedule> matchedSlots = new ArrayList<>();
        for (int i = 0; i < slotCount; i++) {
            LocalTime slotStart = startTime.plusMinutes(i * 30);
            LocalTime slotEnd = slotStart.plusMinutes(30);

            Schedule matchedSlot = null;
            for (Schedule slot : availableSlots) {
                if (slot.getStartTime().equals(slotStart) && slot.getEndTime().equals(slotEnd)) {
                    if (slot.getStatus() == 1 && slot.getRemainingCapacity() != null
                            && slot.getRemainingCapacity() > 0) {
                        matchedSlot = slot;
                        break;
                    }
                }
            }

            if (matchedSlot == null) {
                return ResultDTO.error(400, "部分时间段未开放预约或已约满");
            }
            matchedSlots.add(matchedSlot);
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

        int previousStatus = appointment.getStatus();
        appointment.setStatus(3);
        appointmentMapper.updateById(appointment);

        if (previousStatus == 1) {
            LocalDate appointmentDate = appointment.getAppointmentDate();
            LocalTime startTime = appointment.getStartTime();
            LocalTime endTime = appointment.getEndTime();

            int slotCount = (int) java.time.Duration.between(startTime, endTime).toMinutes() / 30;
            List<Schedule> schedules = scheduleMapper.findAvailableSlotsByCoach(appointmentDate,
                    appointment.getCoachId());

            for (int i = 0; i < slotCount; i++) {
                LocalTime slotStart = startTime.plusMinutes(i * 30);
                LocalTime slotEnd = slotStart.plusMinutes(30);

                for (Schedule slot : schedules) {
                    if (slot.getStartTime().equals(slotStart) && slot.getEndTime().equals(slotEnd)) {
                        scheduleMapper.updateBookedCount(slot.getId(), -1);
                        break;
                    }
                }
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

        LocalDate appointmentDate = appointment.getAppointmentDate();
        LocalTime startTime = appointment.getStartTime();
        LocalTime endTime = appointment.getEndTime();

        int slotCount = (int) java.time.Duration.between(startTime, endTime).toMinutes() / 30;
        List<Schedule> schedules = scheduleMapper.findAvailableSlotsByCoach(appointmentDate, coachId);

        List<Schedule> slotsToUpdate = new ArrayList<>();
        for (int i = 0; i < slotCount; i++) {
            LocalTime slotStart = startTime.plusMinutes(i * 30);
            LocalTime slotEnd = slotStart.plusMinutes(30);

            Schedule matchedSlot = null;
            for (Schedule slot : schedules) {
                if (slot.getStartTime().equals(slotStart) && slot.getEndTime().equals(slotEnd)) {
                    if (slot.getRemainingCapacity() == null || slot.getRemainingCapacity() <= 0) {
                        return ResultDTO.error(400, "部分时段已约满，无法确认");
                    }
                    matchedSlot = slot;
                    break;
                }
            }

            if (matchedSlot == null) {
                return ResultDTO.error(400, "部分时段未找到，无法确认");
            }
            slotsToUpdate.add(matchedSlot);
        }

        for (Schedule slot : slotsToUpdate) {
            scheduleMapper.updateBookedCount(slot.getId(), 1);
        }

        appointment.setStatus(1);
        appointmentMapper.updateById(appointment);
        return ResultDTO.success("预约已确认", null);
    }

    @Override
    @Transactional
    public ResultDTO<?> rejectAppointment(Long id, Long coachId) {
        Appointment appointment = appointmentMapper.selectById(id);
        if (appointment == null) {
            return ResultDTO.error(404, "预约不存在");
        }

        if (!appointment.getCoachId().equals(coachId)) {
            return ResultDTO.error(403, "无权操作此预约");
        }

        if (appointment.getStatus() != 0) {
            return ResultDTO.error(400, "只能拒绝待确认状态的预约");
        }

        appointment.setStatus(5);
        appointmentMapper.updateById(appointment);
        return ResultDTO.success("预约已拒绝", null);
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
    public ResultDTO<?> undoComplete(Long id, Long coachId) {
        Appointment appointment = appointmentMapper.selectById(id);
        if (appointment == null) {
            return ResultDTO.error(404, "预约记录不存在");
        }

        if (!appointment.getCoachId().equals(coachId)) {
            return ResultDTO.error(403, "无权操作此预约");
        }

        if (appointment.getStatus() != 2 && appointment.getStatus() != 3 
                && appointment.getStatus() != 4 && appointment.getStatus() != 5) {
            return ResultDTO.error(400, "该状态不支持撤销");
        }

        appointment.setStatus(1);
        appointmentMapper.updateById(appointment);

        return ResultDTO.success("已撤销", null);
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

    @Override
    public ResultDTO<?> getAppointmentsByVehicleId(Long vehicleId) {
        List<Appointment> appointments = appointmentMapper.findByVehicleId(vehicleId);
        return ResultDTO.success(appointments);
    }

    @Override
    public ResultDTO<HoursStatsDTO> getStudentHoursStats(Long studentId) {
        BigDecimal completedHours = appointmentMapper.sumCompletedHoursByStudentId(studentId);
        if (completedHours == null) {
            completedHours = BigDecimal.ZERO;
        }
        
        int completedSessions = appointmentMapper.countCompletedSessionsByStudentId(studentId);
        
        BigDecimal totalHours = new BigDecimal("12");
        
        Student student = studentMapper.selectById(studentId);
        if (student != null && student.getRequiredHours() != null) {
            totalHours = new BigDecimal(student.getRequiredHours());
        }
        
        HoursStatsDTO stats = new HoursStatsDTO();
        stats.setTotalHours(totalHours);
        stats.setCompletedHours(completedHours);
        stats.setRemainingHours(totalHours.subtract(completedHours).max(BigDecimal.ZERO));
        stats.setCompletedSessions(completedSessions);
        
        return ResultDTO.success(stats);
    }

    @Override
    public ResultDTO<List<HoursRecordDTO>> getStudentHoursRecords(Long studentId) {
        List<Appointment> appointments = appointmentMapper.findCompletedByStudentId(studentId);
        List<HoursRecordDTO> records = new ArrayList<>();
        
        for (Appointment appointment : appointments) {
            HoursRecordDTO record = convertToHoursRecord(appointment);
            records.add(record);
        }
        
        return ResultDTO.success(records);
    }

    @Override
    public ResultDTO<List<HoursRecordDTO>> getCoachHoursRecords(Long coachId) {
        List<Appointment> appointments = appointmentMapper.findCompletedByCoachId(coachId);
        List<HoursRecordDTO> records = new ArrayList<>();
        
        for (Appointment appointment : appointments) {
            HoursRecordDTO record = convertToHoursRecord(appointment);
            records.add(record);
        }
        
        return ResultDTO.success(records);
    }
    
    private HoursRecordDTO convertToHoursRecord(Appointment appointment) {
        HoursRecordDTO record = new HoursRecordDTO();
        record.setId(appointment.getId());
        record.setDate(appointment.getAppointmentDate());
        record.setStudentName(appointment.getStudentName());
        record.setCoachName(appointment.getCoachName());
        record.setRemark(appointment.getRemark());
        record.setStatus(appointment.getStatus());
        record.setPlateNumber(appointment.getPlateNumber());
        record.setVehicleType(appointment.getVehicleType());
        
        if (appointment.getStartTime() != null) {
            record.setStartTime(appointment.getStartTime().toString());
        }
        if (appointment.getEndTime() != null) {
            record.setEndTime(appointment.getEndTime().toString());
        }
        
        long minutes = Duration.between(appointment.getStartTime(), appointment.getEndTime()).toMinutes();
        BigDecimal hours = BigDecimal.valueOf(minutes)
                .divide(BigDecimal.valueOf(60), 1, RoundingMode.HALF_UP);
        record.setHours(hours);
        
        String statusText;
        switch (appointment.getStatus()) {
            case 0:
                statusText = "待确认";
                break;
            case 1:
                statusText = "已确认";
                break;
            case 2:
                statusText = "已完成";
                break;
            case 3:
                statusText = "已取消";
                break;
            case 4:
                statusText = "爽约";
                break;
            default:
                statusText = "未知";
        }
        record.setStatusText(statusText);
        
        return record;
    }

    @Override
    public ResultDTO<?> getAllAppointments() {
        List<Appointment> appointments = appointmentMapper.findAllWithDetails();
        return ResultDTO.success(appointments);
    }

    @Override
    public ResultDTO<?> deleteAppointment(Long id) {
        Appointment appointment = appointmentMapper.selectById(id);
        if (appointment == null) {
            return ResultDTO.error(404, "预约记录不存在");
        }
        appointmentMapper.deleteById(id);
        return ResultDTO.success("删除成功");
    }

    @Override
    public ResultDTO<?> updateAppointment(AppointmentUpdateDTO dto) {
        if (dto.getId() == null) {
            return ResultDTO.error(400, "预约ID不能为空");
        }
        Appointment appointment = appointmentMapper.selectById(dto.getId());
        if (appointment == null) {
            return ResultDTO.error(404, "预约记录不存在");
        }
        if (dto.getVehicleId() != null) {
            appointment.setVehicleId(dto.getVehicleId());
        }
        if (dto.getAppointmentDate() != null) {
            appointment.setAppointmentDate(LocalDate.parse(dto.getAppointmentDate()));
        }
        if (dto.getStartTime() != null) {
            appointment.setStartTime(LocalTime.parse(dto.getStartTime()));
        }
        if (dto.getEndTime() != null) {
            appointment.setEndTime(LocalTime.parse(dto.getEndTime()));
        }
        if (dto.getStatus() != null) {
            appointment.setStatus(dto.getStatus());
        }
        if (dto.getRemark() != null) {
            appointment.setRemark(dto.getRemark());
        }
        appointmentMapper.updateById(appointment);
        return ResultDTO.success("更新成功");
    }
}
