package com.djxlzk.dsasystem.controller;

import com.djxlzk.dsasystem.dto.AppointmentCreateDTO;
import com.djxlzk.dsasystem.dto.ResultDTO;
import com.djxlzk.dsasystem.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public ResultDTO<?> createAppointment(@RequestBody AppointmentCreateDTO dto, Authentication auth) {
        Long studentId = (Long) auth.getPrincipal();
        return appointmentService.createAppointment(dto, studentId);
    }

    @DeleteMapping("/{id}")
    public ResultDTO<?> cancelAppointment(@PathVariable Long id, Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        String role = (String) auth.getCredentials();
        return appointmentService.cancelAppointment(id, userId, role);
    }

    @PutMapping("/confirm/{id}")
    public ResultDTO<?> confirmAppointment(@PathVariable Long id, Authentication auth) {
        Long coachId = (Long) auth.getPrincipal();
        return appointmentService.confirmAppointment(id, coachId);
    }

    @PutMapping("/reject/{id}")
    public ResultDTO<?> rejectAppointment(@PathVariable Long id, Authentication auth) {
        Long coachId = (Long) auth.getPrincipal();
        return appointmentService.rejectAppointment(id, coachId);
    }

    @PutMapping("/complete/{id}")
    public ResultDTO<?> completeAppointment(@PathVariable Long id, Authentication auth) {
        Long coachId = (Long) auth.getPrincipal();
        return appointmentService.completeAppointment(id, coachId);
    }

    @PutMapping("/noshow/{id}")
    public ResultDTO<?> markNoShow(@PathVariable Long id, Authentication auth) {
        Long coachId = (Long) auth.getPrincipal();
        return appointmentService.markNoShow(id, coachId);
    }

    @GetMapping("/student")
    public ResultDTO<?> getStudentAppointments(Authentication auth) {
        Long studentId = (Long) auth.getPrincipal();
        return appointmentService.getStudentAppointments(studentId);
    }

    @GetMapping("/coach")
    public ResultDTO<?> getCoachAppointments(Authentication auth, @RequestParam(required = false) Integer status) {
        Long coachId = (Long) auth.getPrincipal();
        return appointmentService.getCoachAppointments(coachId, status);
    }

    @GetMapping("/active")
    public ResultDTO<?> getActiveAppointments(Authentication auth) {
        Long studentId = (Long) auth.getPrincipal();
        return appointmentService.getActiveAppointments(studentId);
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResultDTO<?> getAppointmentsByVehicle(@PathVariable Long vehicleId) {
        return appointmentService.getAppointmentsByVehicleId(vehicleId);
    }

    @GetMapping("/hours/stats")
    public ResultDTO<?> getStudentHoursStats(Authentication auth) {
        Long studentId = (Long) auth.getPrincipal();
        return appointmentService.getStudentHoursStats(studentId);
    }

    @GetMapping("/hours/records")
    public ResultDTO<?> getStudentHoursRecords(Authentication auth) {
        Long studentId = (Long) auth.getPrincipal();
        return appointmentService.getStudentHoursRecords(studentId);
    }

    @GetMapping("/hours/coach/records")
    public ResultDTO<?> getCoachHoursRecords(Authentication auth) {
        Long coachId = (Long) auth.getPrincipal();
        return appointmentService.getCoachHoursRecords(coachId);
    }

    @GetMapping("/hours/stats/byStudent")
    public ResultDTO<?> getStudentHoursStatsById(@RequestParam Long studentId) {
        return appointmentService.getStudentHoursStats(studentId);
    }

    @GetMapping("/all")
    public ResultDTO<?> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }
}
