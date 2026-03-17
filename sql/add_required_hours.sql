-- 为学员表添加要求学时字段
ALTER TABLE `student` ADD COLUMN `required_hours` INT DEFAULT 40 COMMENT '要求学时' AFTER `car_type`;
