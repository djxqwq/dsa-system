-- 管理员表（若不存在则创建）及默认管理员，便于一键登录与正常登录
-- 执行后可用：手机号 13800000000 / 密码 12345678 登录管理员；或使用前端的「一键管理员登录」

CREATE TABLE IF NOT EXISTS `admin` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `mobile` varchar(20) NOT NULL,
  `password` varchar(64) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `status` int DEFAULT 1,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_mobile` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入默认管理员（密码为 12345678 的 BCrypt 哈希），若已存在则忽略
INSERT INTO `admin` (`mobile`, `password`, `name`, `status`)
SELECT '13800000000', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '系统管理员', 1
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `admin` LIMIT 1);
