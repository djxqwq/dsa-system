-- 系统配置表
CREATE TABLE IF NOT EXISTS system_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    config_key VARCHAR(100) NOT NULL UNIQUE COMMENT '配置键',
    config_value TEXT COMMENT '配置值',
    config_type VARCHAR(50) DEFAULT 'string' COMMENT '配置类型: string, number, boolean, json',
    description VARCHAR(255) COMMENT '配置描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '系统配置表';

-- 初始化默认配置
INSERT INTO
    system_config (
        config_key,
        config_value,
        config_type,
        description
    )
VALUES (
        'open_days',
        '7',
        'number',
        '开放预约天数'
    ),
    (
        'daily_limit',
        '1',
        'number',
        '单人每日预约上限'
    ),
    (
        'cancel_deadline',
        '2',
        'number',
        '取消截止时间（小时）'
    ),
    (
        'confirm_timeout',
        '30',
        'number',
        '预约确认超时时间（分钟）'
    ),
    (
        'system_notice',
        '欢迎使用系统。请提前预约，按时到场。',
        'string',
        '系统公告'
    ),
    (
        'min_appointment_hours',
        '1',
        'number',
        '最小预约时长（小时）'
    ),
    (
        'max_appointment_hours',
        '4',
        'number',
        '最大预约时长（小时）'
    ),
    (
        'slot_duration',
        '30',
        'number',
        '时间槽时长（分钟）'
    ),
    (
        'enable_sms_notify',
        'false',
        'boolean',
        '启用短信通知'
    ),
    (
        'enable_email_notify',
        'false',
        'boolean',
        '启用邮件通知'
    ),
    (
        'working_start_time',
        '08:00',
        'string',
        '工作时间开始'
    ),
    (
        'working_end_time',
        '18:00',
        'string',
        '工作时间结束'
    ),
    (
        'default_required_hours',
        '12',
        'number',
        '默认要求学时'
    );