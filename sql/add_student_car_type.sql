-- 为学员表添加车型字段
ALTER TABLE `student` ADD COLUMN `car_type` VARCHAR(10) DEFAULT 'C1' COMMENT '车型（C1/C2）' AFTER `no_show_count`;
