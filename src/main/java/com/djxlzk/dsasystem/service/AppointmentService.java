package com.djxlzk.dsasystem.service;

import com.djxlzk.dsasystem.dto.AppointmentCreateDTO;
import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.entity.Appointment;

public interface AppointmentService {

    ResultDTO<?> createAppointment(AppointmentCreateDTO dto, Long studentId);

    ResultDTO<?> cancelAppointment(Long id, Long userId, String role);

    ResultDTO<?> confirmAppointment(Long id, Long coachId);

    ResultDTO<?> rejectAppointment(Long id, Long coachId);

    ResultDTO<?> completeAppointment(Long id, Long coachId);

    ResultDTO<?> markNoShow(Long id, Long coachId);

    ResultDTO<?> getStudentAppointments(Long studentId);

    ResultDTO<?> getCoachAppointments(Long coachId, Integer status);

    ResultDTO<?> getActiveAppointments(Long studentId);

    Appointment getById(Long id);
}
