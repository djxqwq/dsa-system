package com.djxlzk.dsasystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.djxlzk.dsasystem.entity.Appointment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalTime;
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

        @Select("SELECT COUNT(*) FROM appointment " +
                        "WHERE student_id = #{studentId} " +
                        "AND appointment_date = #{date} " +
                        "AND status != 3 " +
                        "AND ((start_time <= #{startTime} AND end_time > #{startTime}) " +
                        "OR (start_time < #{endTime} AND end_time >= #{endTime}) " +
                        "OR (start_time >= #{startTime} AND end_time <= #{endTime}))")
        int countByDateAndTime(@Param("studentId") Long studentId, @Param("date") LocalDate date,
                        @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

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

        @Select("SELECT s.user_name " +
                        "FROM appointment a " +
                        "LEFT JOIN student s ON a.student_id = s.id " +
                        "WHERE a.coach_id = #{coachId} " +
                        "AND a.appointment_date = #{date} " +
                        "AND a.start_time < #{endTime} " +
                        "AND a.end_time > #{startTime} " +
                        "AND a.status = 1 " +
                        "ORDER BY a.create_time")
        List<String> findStudentNamesBySchedule(@Param("coachId") Long coachId, @Param("date") LocalDate date,
                        @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);
}
