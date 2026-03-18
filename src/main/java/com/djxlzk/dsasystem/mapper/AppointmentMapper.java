package com.djxlzk.dsasystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.djxlzk.dsasystem.entity.Appointment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Mapper
public interface AppointmentMapper extends BaseMapper<Appointment> {

        @Select("SELECT a.*, s.user_name as student_name, c.name as coach_name, v.plate_number, v.vehicle_type " +
                        "FROM appointment a " +
                        "LEFT JOIN student s ON a.student_id = s.id " +
                        "LEFT JOIN coach c ON a.coach_id = c.id " +
                        "LEFT JOIN vehicle v ON a.vehicle_id = v.id " +
                        "WHERE a.student_id = #{studentId} " +
                        "ORDER BY a.appointment_date DESC, a.start_time DESC")
        List<Appointment> findByStudentId(@Param("studentId") Long studentId);

        @Select("SELECT a.*, s.user_name as student_name, c.name as coach_name, v.plate_number, v.vehicle_type " +
                        "FROM appointment a " +
                        "LEFT JOIN student s ON a.student_id = s.id " +
                        "LEFT JOIN coach c ON a.coach_id = c.id " +
                        "LEFT JOIN vehicle v ON a.vehicle_id = v.id " +
                        "WHERE a.coach_id = #{coachId} " +
                        "ORDER BY a.appointment_date DESC, a.start_time DESC")
        List<Appointment> findByCoachId(@Param("coachId") Long coachId);

        @Select("SELECT a.*, s.user_name as student_name, c.name as coach_name, v.plate_number, v.vehicle_type " +
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

        @Select("SELECT a.*, s.user_name as student_name, c.name as coach_name, v.plate_number, v.vehicle_type " +
                        "FROM appointment a " +
                        "LEFT JOIN student s ON a.student_id = s.id " +
                        "LEFT JOIN coach c ON a.coach_id = c.id " +
                        "LEFT JOIN vehicle v ON a.vehicle_id = v.id " +
                        "WHERE a.id = #{id}")
        Appointment findByIdWithDetails(@Param("id") Long id);

        @Select("SELECT a.*, s.user_name as student_name, c.name as coach_name, v.plate_number, v.vehicle_type " +
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

        @Select("SELECT a.*, s.user_name as student_name, c.name as coach_name, v.plate_number, v.vehicle_type " +
                        "FROM appointment a " +
                        "LEFT JOIN student s ON a.student_id = s.id " +
                        "LEFT JOIN coach c ON a.coach_id = c.id " +
                        "LEFT JOIN vehicle v ON a.vehicle_id = v.id " +
                        "WHERE a.vehicle_id = #{vehicleId} " +
                        "ORDER BY a.appointment_date DESC, a.start_time DESC")
        List<Appointment> findByVehicleId(@Param("vehicleId") Long vehicleId);

        @Select("SELECT COALESCE(SUM(TIMESTAMPDIFF(HOUR, a.start_time, a.end_time)), 0) " +
                        "FROM appointment a " +
                        "WHERE a.student_id = #{studentId} AND a.status = 2")
        BigDecimal sumCompletedHoursByStudentId(@Param("studentId") Long studentId);

        @Select("SELECT COUNT(*) FROM appointment WHERE student_id = #{studentId} AND status = 2")
        int countCompletedSessionsByStudentId(@Param("studentId") Long studentId);

        @Select("SELECT a.*, s.user_name as student_name, c.name as coach_name " +
                        "FROM appointment a " +
                        "LEFT JOIN student s ON a.student_id = s.id " +
                        "LEFT JOIN coach c ON a.coach_id = c.id " +
                        "WHERE a.student_id = #{studentId} AND a.status = 2 " +
                        "ORDER BY a.appointment_date DESC, a.start_time DESC")
        List<Appointment> findCompletedByStudentId(@Param("studentId") Long studentId);

        @Select("SELECT a.*, s.user_name as student_name, c.name as coach_name, v.plate_number, v.vehicle_type " +
                        "FROM appointment a " +
                        "LEFT JOIN student s ON a.student_id = s.id " +
                        "LEFT JOIN coach c ON a.coach_id = c.id " +
                        "LEFT JOIN vehicle v ON a.vehicle_id = v.id " +
                        "WHERE a.coach_id = #{coachId} AND a.status IN (1, 2, 4) " +
                        "ORDER BY a.appointment_date DESC, a.start_time DESC")
        List<Appointment> findCompletedByCoachId(@Param("coachId") Long coachId);

        @Select("SELECT COUNT(*) FROM appointment WHERE DATE(appointment_date) = #{date}")
        Long countByDate(@Param("date") LocalDate date);

        @Select("SELECT COUNT(*) FROM appointment WHERE status = 0")
        Long countPending();

        @Select("SELECT COALESCE(SUM(TIMESTAMPDIFF(MINUTE, start_time, end_time)) / 60, 0) " +
                        "FROM appointment WHERE DATE(appointment_date) = #{date} AND status = 2")
        BigDecimal sumHoursByDate(@Param("date") LocalDate date);

        @Select("SELECT COALESCE(SUM(TIMESTAMPDIFF(MINUTE, start_time, end_time)) / 60, 0) " +
                        "FROM appointment WHERE status = 2")
        BigDecimal sumTotalCompletedHours();

        @Select("SELECT COUNT(*) FROM appointment WHERE status = 2")
        Long countTotalCompletedSessions();

        @Select("SELECT DATE(appointment_date) as date, COUNT(*) as bookings, " +
                        "COALESCE(SUM(CASE WHEN status = 2 THEN TIMESTAMPDIFF(MINUTE, start_time, end_time) ELSE 0 END) / 60, 0) as hours, " +
                        "SUM(CASE WHEN status = 2 THEN 1 ELSE 0 END) as completed, " +
                        "SUM(CASE WHEN status = 3 THEN 1 ELSE 0 END) as cancelled " +
                        "FROM appointment " +
                        "WHERE DATE(appointment_date) BETWEEN #{startDate} AND #{endDate} " +
                        "GROUP BY DATE(appointment_date) " +
                        "ORDER BY DATE(appointment_date)")
        List<java.util.Map<String, Object>> findDailyStats(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

        @Select("SELECT c.id as coachId, c.name as coachName, " +
                        "COUNT(*) as totalSessions, " +
                        "COALESCE(SUM(TIMESTAMPDIFF(MINUTE, a.start_time, a.end_time)) / 60, 0) as totalHours, " +
                        "SUM(CASE WHEN a.status = 2 THEN 1 ELSE 0 END) as completedSessions " +
                        "FROM appointment a " +
                        "LEFT JOIN coach c ON a.coach_id = c.id " +
                        "WHERE a.status IN (1, 2) " +
                        "GROUP BY c.id, c.name " +
                        "ORDER BY completedSessions DESC " +
                        "LIMIT 5")
        List<java.util.Map<String, Object>> findTopCoaches();

        @Select("SELECT HOUR(start_time) as hour, COUNT(*) as count " +
                        "FROM appointment " +
                        "WHERE status IN (1, 2) " +
                        "GROUP BY HOUR(start_time) " +
                        "ORDER BY HOUR(start_time)")
        List<java.util.Map<String, Object>> findHourlyDistribution();

        @Select("SELECT " +
                        "COUNT(CASE WHEN status = 1 THEN 1 END) as confirmed, " +
                        "COUNT(CASE WHEN status = 2 THEN 1 END) as completed, " +
                        "COUNT(CASE WHEN status = 4 THEN 1 END) as noShow, " +
                        "COUNT(*) as total " +
                        "FROM appointment")
        java.util.Map<String, Object> getStatusStats();

        @Select("SELECT a.*, s.user_name as student_name, c.name as coach_name, v.plate_number, v.vehicle_type " +
                        "FROM appointment a " +
                        "LEFT JOIN student s ON a.student_id = s.id " +
                        "LEFT JOIN coach c ON a.coach_id = c.id " +
                        "LEFT JOIN vehicle v ON a.vehicle_id = v.id " +
                        "ORDER BY a.appointment_date DESC, a.start_time DESC")
        List<Appointment> findAllWithDetails();
}
