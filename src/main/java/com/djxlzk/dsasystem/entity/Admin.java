package com.djxlzk.dsasystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("admin")
public class Admin {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String mobile;

    private String password;

    /** 若表中无 name 列，可不建该列，实体中不参与查询避免报错 */
    @TableField(exist = false)
    private String name;

    private LocalDateTime createTime;

    private Integer status;
}
