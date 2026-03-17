-- 学员表
CREATE TABLE IF NOT EXISTS `student` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `mobile` VARCHAR(20) NOT NULL COMMENT '手机号',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `user_name` VARCHAR(50) DEFAULT NULL COMMENT '用户名',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` TINYINT DEFAULT 1 COMMENT '状态（0：禁用，1：启用）',
  `no_show_count` INT DEFAULT 0 COMMENT '爽约次数',
  `car_type` VARCHAR(10) DEFAULT NULL COMMENT '车型（C1/C2）',
  `required_hours` INT DEFAULT 12 COMMENT '要求学时',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_mobile` (`mobile`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学员表';

-- 如果表已存在，添加required_hours字段（如果不存在）
-- ALTER TABLE `student` ADD COLUMN IF NOT EXISTS `required_hours` INT DEFAULT 12 COMMENT '要求学时';
