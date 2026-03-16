-- 排班表（教练可预约时段）
CREATE TABLE IF NOT EXISTS `schedule` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `coach_id` BIGINT NOT NULL COMMENT '教练ID',
  `schedule_date` DATE NOT NULL COMMENT '排班日期',
  `start_time` TIME NOT NULL COMMENT '开始时间',
  `end_time` TIME NOT NULL COMMENT '结束时间',
  `capacity` INT DEFAULT 1 COMMENT '可预约人数',
  `booked_count` INT DEFAULT 0 COMMENT '已预约人数',
  `status` TINYINT DEFAULT 1 COMMENT '状态（0：关闭，1：开放）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_coach_date` (`coach_id`, `schedule_date`),
  KEY `idx_date` (`schedule_date`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='排班表';

-- 预约表
CREATE TABLE IF NOT EXISTS `appointment` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `schedule_id` BIGINT NOT NULL COMMENT '排班ID',
  `student_id` BIGINT NOT NULL COMMENT '学员ID',
  `coach_id` BIGINT NOT NULL COMMENT '教练ID',
  `vehicle_id` BIGINT DEFAULT NULL COMMENT '车辆ID',
  `appointment_date` DATE NOT NULL COMMENT '预约日期',
  `start_time` TIME NOT NULL COMMENT '开始时间',
  `end_time` TIME NOT NULL COMMENT '结束时间',
  `status` TINYINT DEFAULT 0 COMMENT '状态（0：待确认，1：已确认，2：已完成，3：已取消，4：爽约）',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_schedule_student` (`schedule_id`, `student_id`),
  KEY `idx_student` (`student_id`),
  KEY `idx_coach` (`coach_id`),
  KEY `idx_date` (`appointment_date`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约表';
