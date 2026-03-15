-- 车辆表
CREATE TABLE IF NOT EXISTS `vehicle` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `plate_number` VARCHAR(20) NOT NULL COMMENT '车牌号',
  `vehicle_type` VARCHAR(10) DEFAULT NULL COMMENT '车型（C1/C2）',
  `coach_id` BIGINT DEFAULT NULL COMMENT '关联教练ID',
  `status` INT DEFAULT 1 COMMENT '状态（0：停用，1：可用，2：维修中）',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_plate_number` (`plate_number`),
  KEY `idx_vehicle_type` (`vehicle_type`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='车辆表';
