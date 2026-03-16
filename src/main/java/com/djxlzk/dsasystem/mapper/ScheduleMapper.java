package com.djxlzk.dsasystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.djxlzk.dsasystem.entity.Schedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ScheduleMapper extends BaseMapper<Schedule> {

    @Select("SELECT s.*, c.name as coach_name FROM schedule s " +
            "LEFT JOIN coach c ON s.coach_id = c.id " +
            "WHERE s.coach_id = #{coachId} AND s.schedule_date >= #{startDate} " +
            "ORDER BY s.schedule_date, s.start_time")
    List<Schedule> findByCoachIdFromDate(@Param("coachId") Long coachId, @Param("startDate") LocalDate startDate);

    @Select("SELECT s.*, c.name as coach_name, " +
            "(s.capacity - s.booked_count) as remaining_capacity " +
            "FROM schedule s " +
            "LEFT JOIN coach c ON s.coach_id = c.id " +
            "WHERE s.schedule_date = #{date} AND s.status = 1 " +
            "AND s.booked_count < s.capacity " +
            "ORDER BY s.start_time")
    List<Schedule> findAvailableSlots(@Param("date") LocalDate date);

    @Select("SELECT s.*, c.name as coach_name, " +
            "(s.capacity - s.booked_count) as remaining_capacity " +
            "FROM schedule s " +
            "LEFT JOIN coach c ON s.coach_id = c.id " +
            "WHERE s.schedule_date = #{date} AND s.status = 1 " +
            "AND s.coach_id = #{coachId} AND s.booked_count < s.capacity " +
            "ORDER BY s.start_time")
    List<Schedule> findAvailableSlotsByCoach(@Param("date") LocalDate date, @Param("coachId") Long coachId);

    @Update("UPDATE schedule SET booked_count = booked_count + #{delta} WHERE id = #{id}")
    int updateBookedCount(@Param("id") Long id, @Param("delta") int delta);

    @Select("SELECT s.*, c.name as coach_name FROM schedule s " +
            "LEFT JOIN coach c ON s.coach_id = c.id " +
            "WHERE s.id = #{id}")
    Schedule findByIdWithCoachName(@Param("id") Long id);
}
