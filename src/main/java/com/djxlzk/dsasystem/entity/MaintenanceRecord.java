package com.djxlzk.dsasystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("maintenance_record")
public class MaintenanceRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("vehicle_id")
    private Long vehicleId;

    @TableField("coach_id")
    private Long coachId;

    @TableField("maintenance_type")
    private String maintenanceType;

    @TableField("maintenance_date")
    private LocalDate maintenanceDate;

    private BigDecimal cost;

    private Integer mileage;

    private String description;

    @TableField("next_maintenance_date")
    private LocalDate nextMaintenanceDate;

    @TableField("next_maintenance_mileage")
    private Integer nextMaintenanceMileage;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String plateNumber;

    @TableField(exist = false)
    private String coachName;
}
