package com.djxlzk.dsasystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.djxlzk.dsasystem.entity.Vehicle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface VehicleMapper extends BaseMapper<Vehicle> {

        @Select("SELECT v.*, c.name as coach_name FROM vehicle v " +
                        "LEFT JOIN coach c ON v.coach_id = c.id " +
                        "WHERE v.status = 1 " +
                        "ORDER BY v.id")
        List<Vehicle> findAllWithCoachName();

        @Select("SELECT v.*, c.name as coach_name FROM vehicle v " +
                        "LEFT JOIN coach c ON v.coach_id = c.id " +
                        "WHERE v.status = 1 AND v.vehicle_type = #{carType} " +
                        "ORDER BY v.id")
        List<Vehicle> findByCarTypeWithCoachName(@Param("carType") String carType);
}
