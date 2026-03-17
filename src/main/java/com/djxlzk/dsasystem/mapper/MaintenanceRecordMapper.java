package com.djxlzk.dsasystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.djxlzk.dsasystem.entity.MaintenanceRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MaintenanceRecordMapper extends BaseMapper<MaintenanceRecord> {

    @Select("SELECT m.*, v.plate_number, c.name as coach_name " +
            "FROM maintenance_record m " +
            "LEFT JOIN vehicle v ON m.vehicle_id = v.id " +
            "LEFT JOIN coach c ON m.coach_id = c.id " +
            "WHERE m.coach_id = #{coachId} " +
            "ORDER BY m.maintenance_date DESC")
    List<MaintenanceRecord> findByCoachId(@Param("coachId") Long coachId);

    @Select("SELECT m.*, v.plate_number, c.name as coach_name " +
            "FROM maintenance_record m " +
            "LEFT JOIN vehicle v ON m.vehicle_id = v.id " +
            "LEFT JOIN coach c ON m.coach_id = c.id " +
            "WHERE m.vehicle_id = #{vehicleId} " +
            "ORDER BY m.maintenance_date DESC")
    List<MaintenanceRecord> findByVehicleId(@Param("vehicleId") Long vehicleId);

    @Select("SELECT m.*, v.plate_number, c.name as coach_name " +
            "FROM maintenance_record m " +
            "LEFT JOIN vehicle v ON m.vehicle_id = v.id " +
            "LEFT JOIN coach c ON m.coach_id = c.id " +
            "ORDER BY m.maintenance_date DESC")
    List<MaintenanceRecord> findAllWithDetails();
}
