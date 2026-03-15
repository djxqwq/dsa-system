-- 将 coach 表的 coach_no 从 varchar(2) 扩容，避免添加教练时因工号超长导致插入失败
-- 执行前请备份数据；若表名/库名不同请自行修改

ALTER TABLE coach MODIFY COLUMN coach_no VARCHAR(32) NOT NULL COMMENT '教练工号';
