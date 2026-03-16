package com.djxlzk.dsasystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.djxlzk.dsasystem.entity.Appointment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface AppointmentMapper extends BaseMapper<Appointment> {

    @Select("SELECT a.*, s.user_name as student_name, c.name as coach_name, v.plate_number " +
            "FROM appointment a " +
            "LEFT JOIN student s ON a.student_id = s.id " +
            "LEFT JOIN coach c ON a.coach_id = c.id " +
            "LEFT JOIN vehicle v ON a.vehicle_id = v.id " +
            "WHERE a.student_id = #{studentId} " +
            "ORDER BY a.appointment_date DESC, a.start_time DESC")
    List<Appointment> findByStudentId(@Param("studentId") Long studentId);

    @Select("SELECT a.*, s.user_name as student_name, c.name as coach_name, v.plate_number " +
            "FROM appointment a " +
            "LEFT JOIN student s ON a.student_id = s.id " +
            "LEFT JOIN coach c ON a.coach_id = c.id " +
            "LEFT JOIN vehicle v ON a.vehicle_id = v.id " +
            "WHERE a.coach_id = #{coachId} " +
            "ORDER BY a.appointment_date DESC, a.start_time DESC")
    List<Appointment> findByCoachId(@Param("coachId") Long coachId);

    @Select("SELECT a.*, s.user_name as student_name, c.name as coach_name, v.plate_number " +
            "FROM appointment a " +
            "LEFT JOIN student s ON a.student_id = s.id " +
            "LEFT JOIN coach c ON a.coach_id = c.id " +
            "LEFT JOIN vehicle v ON a.vehicle_id = v.id " +
            "WHERE a.coach_id = #{coachId} AND a.status = #{status} " +
            "ORDER BY a.appointment_date, a.start_time")
    List<Appointment> findByCoachIdAndStatus(@Param("coachId") Long coachId, @Param("status") Integer status);

    @Select("SELECT COUNT(*) FROM appointment WHERE schedule_id = #{scheduleId} AND student_id = #{studentId} AND status != 3")
    int countByScheduleAndStudent(@Param("scheduleId") Long scheduleId, @Param("studentId") Long studentId);

    @Select("SELECT a.*, s.user_name as student_name, c.name as coach_name, v.plate_number " +
            "FROM appointment a " +
            "LEFT JOIN student s ON a.student_id = s.id " +
            "LEFT JOIN coach c ON a.coach_id = c.id " +
            "LEFT JOIN vehicle v ON a.vehicle_id = v.id " +
            "WHERE a.id = #{id}")
    Appointment findByIdWithDetails(@Param("id") Long id);

    @Select("SELECT a.*, s.user_name as student_name, c.name as coach_name, v.plate_number " +
            "FROM appointment a " +
            "LEFT JOIN student s ON a.student_id = s.id " +
            "LEFT JOIN coach c ON a.coach_id = c.id " +
            "LEFT JOIN vehicle v ON a.vehicle_id = v.id " +
            "WHERE a.student_id = #{studentId} AND a.status IN (0, 1) " +
            "ORDER BY a.appointment_date, a.start_time")
    List<Appointment> findActiveByStudentId(@Param("studentId") Long studentId);
}
