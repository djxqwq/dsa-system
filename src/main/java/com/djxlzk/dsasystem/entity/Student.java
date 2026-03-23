package com.djxlzk.dsasystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 学员实体类
 */
@Data
@TableName("student")
public class Student {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 学号
     */
    @TableField("student_no")
    private String studentNo;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 状态（0：禁用，1：启用）
     */
    private Integer status;

    /**
     * 爽约次数
     */
    @TableField("no_show_count")
    private Integer noShowCount;

    /**
     * 车型（C1/C2）
     */
    @TableField("car_type")
    private String carType;

    /**
     * 性别（0：女，1：男）
     */
    private Integer gender;

    /**
     * 要求学时（默认12小时）
     */
    @TableField(value = "required_hours")
    private Integer requiredHours = 12;
}