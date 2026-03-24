-- =============================================
-- 消息表创建脚本
-- =============================================

CREATE TABLE IF NOT EXISTS `message` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '消息ID',
    `student_id` BIGINT NULL COMMENT '学员ID',
    `coach_id` BIGINT NULL COMMENT '教练ID',
    `title` VARCHAR(255) NOT NULL COMMENT '消息标题',
    `content` TEXT NOT NULL COMMENT '消息内容',
    `type` VARCHAR(20) NOT NULL COMMENT '消息类型：system-系统通知, booking-预约提醒, hours-学时提醒',
    `read` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '已读状态：0-未读, 1-已读',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_student_id` (`student_id`),
    KEY `idx_coach_id` (`coach_id`),
    KEY `idx_type` (`type`),
    KEY `idx_read` (`read`),
    KEY `idx_created_at` (`created_at`),
    CONSTRAINT `fk_message_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_message_coach` FOREIGN KEY (`coach_id`) REFERENCES `coach` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '消息表';
-- =====