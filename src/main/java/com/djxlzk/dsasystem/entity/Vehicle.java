package com.djxlzk.dsasystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("vehicle")
public class Vehicle {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("plate_number")
    private String plateNumber;

    @TableField("vehicle_type")
    private String vehicleType;

    @TableField("coach_id")
    private Long coachId;

    private Integer status;

    private String remark;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String coachName;
}
