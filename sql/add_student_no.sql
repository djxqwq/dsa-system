-- 为学员表添加学号字段
ALTER TABLE `student` ADD COLUMN `student_no` VARCHAR(20) DEFAULT NULL COMMENT '学号' AFTER `id`;

-- 为学号创建索引
CREATE INDEX `idx_student_no` ON `student` (`student_no`);
