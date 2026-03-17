-- 保养记录表
CREATE TABLE IF NOT EXISTS `maintenance_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `vehicle_id` BIGINT NOT NULL COMMENT '车辆ID',
  `coach_id` BIGINT NOT NULL COMMENT '教练ID',
  `maintenance_type` VARCHAR(50) NOT NULL COMMENT '保养类型',
  `maintenance_date` DATE NOT NULL COMMENT '保养日期',
  `cost` DECIMAL(10,2) DEFAULT NULL COMMENT '费用',
  `mileage` INT DEFAULT NULL COMMENT '里程数(公里)',
  `description` VARCHAR(500) DEFAULT NULL COMMENT '描述',
  `next_maintenance_date` DATE DEFAULT NULL COMMENT '下次保养日期',
  `next_maintenance_mileage` INT DEFAULT NULL COMMENT '下次保养里程',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_vehicle_id` (`vehicle_id`),
  KEY `idx_coach_id` (`coach_id`),
  KEY `idx_maintenance_date` (`maintenance_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='保养记录表';

-- 维修记录表
CREATE TABLE IF NOT EXISTS `repair_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `vehicle_id` BIGINT NOT NULL COMMENT '车辆ID',
  `coach_id` BIGINT NOT NULL COMMENT '教练ID',
  `repair_type` VARCHAR(50) NOT NULL COMMENT '维修类型',
  `repair_date` DATE NOT NULL COMMENT '维修日期',
  `cost` DECIMAL(10,2) DEFAULT NULL COMMENT '费用',
  `mileage` INT DEFAULT NULL COMMENT '里程数(公里)',
  `description` VARCHAR(500) DEFAULT NULL COMMENT '描述',
  `parts` VARCHAR(255) DEFAULT NULL COMMENT '更换配件',
  `repair_shop` VARCHAR(100) DEFAULT NULL COMMENT '维修店',
  `status` INT DEFAULT 1 COMMENT '状态（0：进行中，1：已完成）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_vehicle_id` (`vehicle_id`),
  KEY `idx_coach_id` (`coach_id`),
  KEY `idx_repair_date` (`repair_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='维修记录表';
