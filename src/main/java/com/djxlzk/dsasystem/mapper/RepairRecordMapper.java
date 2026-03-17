package com.djxlzk.dsasystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.djxlzk.dsasystem.entity.RepairRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RepairRecordMapper extends BaseMapper<RepairRecord> {

    @Select("SELECT r.*, v.plate_number, c.name as coach_name " +
            "FROM repair_record r " +
            "LEFT JOIN vehicle v ON r.vehicle_id = v.id " +
            "LEFT JOIN coach c ON r.coach_id = c.id " +
            "WHERE r.coach_id = #{coachId} " +
            "ORDER BY r.repair_date DESC")
    List<RepairRecord> findByCoachId(@Param("coachId") Long coachId);

    @Select("SELECT r.*, v.plate_number, c.name as coach_name " +
            "FROM repair_record r " +
            "LEFT JOIN vehicle v ON r.vehicle_id = v.id " +
            "LEFT JOIN coach c ON r.coach_id = c.id " +
            "WHERE r.vehicle_id = #{vehicleId} " +
            "ORDER BY r.repair_date DESC")
    List<RepairRecord> findByVehicleId(@Param("vehicleId") Long vehicleId);

    @Select("SELECT r.*, v.plate_number, c.name as coach_name " +
            "FROM repair_record r " +
            "LEFT JOIN vehicle v ON r.vehicle_id = v.id " +
            "LEFT JOIN coach c ON r.coach_id = c.id " +
            "ORDER BY r.repair_date DESC")
    List<RepairRecord> findAllWithDetails();
}
