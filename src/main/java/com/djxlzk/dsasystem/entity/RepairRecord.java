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
@TableName("repair_record")
public class RepairRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("vehicle_id")
    private Long vehicleId;

    @TableField("coach_id")
    private Long coachId;

    @TableField("repair_type")
    private String repairType;

    @TableField("repair_date")
    private LocalDate repairDate;

    private BigDecimal cost;

    private Integer mileage;

    private String description;

    private String parts;

    @TableField("repair_shop")
    private String repairShop;

    private Integer status;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String plateNumber;

    @TableField(exist = false)
    private String coachName;
}
