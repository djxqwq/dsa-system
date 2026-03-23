package com.djxlzk.dsasystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 教练实体类
 */
@Data
@TableName("coach")
public class Coach {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 教练编号
     */
    private String coachNo;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 密码（接口不返回）
     */
    @JsonIgnore
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 状态（0：禁用，1：启用）
     */
    private Integer status;

    /**
     * 在岗状态（0：休假，1：在岗）
     */
    private Integer workStatus;
}