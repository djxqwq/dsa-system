-- =============================================
-- 驾校练车预约系统 - 数据库初始化脚本
-- =============================================

-- 1. 为学员表添加学号字段（如果不存在）
ALTER TABLE `student` ADD COLUMN IF NOT EXISTS `student_no` VARCHAR(20) DEFAULT NULL COMMENT '学号' AFTER `id`;
CREATE INDEX IF NOT EXISTS `idx_student_no` ON `student` (`student_no`);

-- 2. 教练表假数据（密码：abc12345）
INSERT INTO `coach` (`coach_no`, `mobile`, `password`, `name`, `create_time`, `status`) VALUES
('C001', '13900139001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '张三', NOW(), 1),
('C002', '13900139002', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '李四', NOW(), 1),
('C003', '13900139003', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '王五', NOW(), 1),
('C004', '13900139004', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '赵六', NOW(), 1),
('C005', '13900139005', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '钱七', NOW(), 1);

-- 3. 车辆表假数据
INSERT INTO `vehicle` (`plate_number`, `vehicle_type`, `coach_id`, `status`, `remark`, `create_time`, `update_time`) VALUES
('京A12345', 'C1', 1, 1, '大众桑塔纳', NOW(), NOW()),
('京A67890', 'C1', 2, 1, '大众捷达', NOW(), NOW()),
('京B12345', 'C2', 3, 1, '丰田卡罗拉', NOW(), NOW()),
('京B67890', 'C2', 4, 1, '本田思域', NOW(), NOW()),
('京C12345', 'C1', 5, 1, '现代伊兰特', NOW(), NOW());

-- 4. 学员表假数据（密码：abc12345）
INSERT INTO `student` (`student_no`, `mobile`, `password`, `user_name`, `create_time`, `status`, `no_show_count`, `car_type`, `required_hours`) VALUES
('S001', '13700137001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '学员1', NOW(), 1, 0, 'C1', 12),
('S002', '13700137002', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '学员2', NOW(), 1, 0, 'C1', 12),
('S003', '13700137003', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '学员3', NOW(), 1, 0, 'C2', 12),
('S004', '13700137004', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '学员4', NOW(), 1, 0, 'C2', 12),
('S005', '13700137005', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '学员5', NOW(), 1, 0, 'C1', 12),
('S006', '13700137006', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '学员6', NOW(), 1, 0, 'C1', 12),
('S007', '13700137007', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '学员7', NOW(), 1, 0, 'C2', 12),
('S008', '13700137008', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '学员8', NOW(), 1, 0, 'C2', 12),
('S009', '13700137009', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '学员9', NOW(), 1, 0, 'C1', 12),
('S010', '13700137010', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '学员10', NOW(), 1, 0, 'C1', 12);

-- 5. 课程表假数据
INSERT INTO `schedule` (`coach_id`, `schedule_date`, `start_time`, `end_time`, `capacity`, `booked_count`, `status`, `create_time`, `update_time`) VALUES
(1, DATE_ADD(NOW(), INTERVAL 1 DAY), '09:00:00', '11:00:00', 4, 0, 1, NOW(), NOW()),
(1, DATE_ADD(NOW(), INTERVAL 1 DAY), '14:00:00', '16:00:00', 4, 0, 1, NOW(), NOW()),
(2, DATE_ADD(NOW(), INTERVAL 1 DAY), '09:00:00', '11:00:00', 4, 0, 1, NOW(), NOW()),
(2, DATE_ADD(NOW(), INTERVAL 1 DAY), '14:00:00', '16:00:00', 4, 0, 1, NOW(), NOW()),
(3, DATE_ADD(NOW(), INTERVAL 2 DAY), '09:00:00', '11:00:00', 4, 0, 1, NOW(), NOW()),
(3, DATE_ADD(NOW(), INTERVAL 2 DAY), '14:00:00', '16:00:00', 4, 0, 1, NOW(), NOW()),
(4, DATE_ADD(NOW(), INTERVAL 2 DAY), '09:00:00', '11:00:00', 4, 0, 1, NOW(), NOW()),
(4, DATE_ADD(NOW(), INTERVAL 2 DAY), '14:00:00', '16:00:00', 4, 0, 1, NOW(), NOW()),
(5, DATE_ADD(NOW(), INTERVAL 3 DAY), '09:00:00', '11:00:00', 4, 0, 1, NOW(), NOW()),
(5, DATE_ADD(NOW(), INTERVAL 3 DAY), '14:00:00', '16:00:00', 4, 0, 1, NOW(), NOW());

-- 6. 预约表假数据
INSERT INTO `appointment` (`student_id`, `coach_id`, `vehicle_id`, `appointment_date`, `start_time`, `end_time`, `status`, `remark`, `create_time`, `update_time`) VALUES
(1, 1, 1, DATE_ADD(NOW(), INTERVAL 1 DAY), '09:00:00', '11:00:00', 1, '科目二练习', NOW(), NOW()),
(2, 1, 1, DATE_ADD(NOW(), INTERVAL 1 DAY), '09:00:00', '11:00:00', 1, '科目二练习', NOW(), NOW()),
(3, 2, 2, DATE_ADD(NOW(), INTERVAL 1 DAY), '14:00:00', '16:00:00', 1, '科目三练习', NOW(), NOW()),
(4, 2, 2, DATE_ADD(NOW(), INTERVAL 1 DAY), '14:00:00', '16:00:00', 1, '科目三练习', NOW(), NOW()),
(5, 3, 3, DATE_ADD(NOW(), INTERVAL 2 DAY), '09:00:00', '11:00:00', 1, '科目二练习', NOW(), NOW()),
(6, 3, 3, DATE_ADD(NOW(), INTERVAL 2 DAY), '09:00:00', '11:00:00', 1, '科目二练习', NOW(), NOW()),
(7, 4, 4, DATE_ADD(NOW(), INTERVAL 2 DAY), '14:00:00', '16:00:00', 1, '科目三练习', NOW(), NOW()),
(8, 4, 4, DATE_ADD(NOW(), INTERVAL 2 DAY), '14:00:00', '16:00:00', 1, '科目三练习', NOW(), NOW()),
(9, 5, 5, DATE_ADD(NOW(), INTERVAL 3 DAY), '09:00:00', '11:00:00', 1, '科目二练习', NOW(), NOW()),
(10, 5, 5, DATE_ADD(NOW(), INTERVAL 3 DAY), '09:00:00', '11:00:00', 1, '科目二练习', NOW(), NOW());

-- 7. 保养记录表假数据
INSERT INTO `maintenance_record` (`vehicle_id`, `coach_id`, `maintenance_type`, `maintenance_date`, `cost`, `mileage`, `description`, `next_maintenance_date`, `next_maintenance_mileage`, `create_time`, `update_time`) VALUES
(1, 1, '常规保养', DATE_SUB(NOW(), INTERVAL 30 DAY), 300.00, 5000, '更换机油、机滤', DATE_ADD(NOW(), INTERVAL 30 DAY), 10000, NOW(), NOW()),
(2, 2, '常规保养', DATE_SUB(NOW(), INTERVAL 45 DAY), 350.00, 8000, '更换机油、机滤、空滤', DATE_ADD(NOW(), INTERVAL 15 DAY), 13000, NOW(), NOW()),
(3, 3, '常规保养', DATE_SUB(NOW(), INTERVAL 60 DAY), 400.00, 12000, '更换机油、机滤、空滤、空调滤', DATE_ADD(NOW(), INTERVAL 0 DAY), 17000, NOW(), NOW()),
(4, 4, '常规保养', DATE_SUB(NOW(), INTERVAL 15 DAY), 320.00, 3000, '更换机油、机滤', DATE_ADD(NOW(), INTERVAL 45 DAY), 8000, NOW(), NOW()),
(5, 5, '常规保养', DATE_SUB(NOW(), INTERVAL 20 DAY), 380.00, 6000, '更换机油、机滤、空滤', DATE_ADD(NOW(), INTERVAL 40 DAY), 11000, NOW(), NOW());

-- 8. 维修记录表假数据
INSERT INTO `repair_record` (`vehicle_id`, `coach_id`, `repair_type`, `repair_date`, `cost`, `mileage`, `description`, `parts`, `repair_shop`, `status`, `create_time`, `update_time`) VALUES
(1, 1, '小修', DATE_SUB(NOW(), INTERVAL 10 DAY), 200.00, 4500, '更换雨刮器', '雨刮器', '汽车维修厂', 1, NOW(), NOW()),
(2, 2, '中修', DATE_SUB(NOW(), INTERVAL 20 DAY), 800.00, 7500, '更换刹车片', '刹车片', '汽车维修厂', 1, NOW(), NOW()),
(3, 3, '小修', DATE_SUB(NOW(), INTERVAL 5 DAY), 150.00, 11500, '更换灯泡', '灯泡', '汽车维修厂', 1, NOW(), NOW()),
(4, 4, '中修', DATE_SUB(NOW(), INTERVAL 15 DAY), 600.00, 2500, '更换电瓶', '电瓶', '汽车维修厂', 1, NOW(), NOW()),
(5, 5, '小修', DATE_SUB(NOW(), INTERVAL 8 DAY), 180.00, 5500, '更换轮胎', '轮胎', '汽车维修厂', 1, NOW(), NOW());

-- 9. 系统配置表假数据
INSERT INTO `system_config` (`config_key`, `config_value`, `description`, `create_time`, `update_time`) VALUES
('appointment_advance_days', '7', '预约提前天数', NOW(), NOW()),
('appointment_max_per_day', '2', '每天最大预约次数', NOW(), NOW()),
('no_show_threshold', '3', '爽约阈值', NOW(), NOW()),
('required_hours', '12', '要求学时', NOW(), NOW()),
('maintenance_interval_days', '30', '保养间隔天数', NOW(), NOW()),
('maintenance_interval_mileage', '5000', '保养间隔里程', NOW(), NOW());

-- =============================================
-- 账号信息汇总
-- =============================================
-- 教练账号（密码：abc12345）：
--   C001 - 13900139001 - 张三
--   C002 - 13900139002 - 李四
--   C003 - 13900139003 - 王五
--   C004 - 13900139004 - 赵六
--   C005 - 13900139005 - 钱七
--
-- 学员账号（密码：abc12345）：
--   S001 - 13700137001 - 学员1
--   S002 - 13700137002 - 学员2
--   S003 - 13700137003 - 学员3
--   S004 - 13700137004 - 学员4
--   S005 - 13700137005 - 学员5
--   S006 - 13700137006 - 学员6
--   S007 - 13700137007 - 学员7
--   S008 - 13700137008 - 学员8
--   S009 - 13700137009 - 学员9
--   S010 - 13700137010 - 学员10
-- =============================================
