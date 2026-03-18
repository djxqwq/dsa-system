-- =============================================
-- 驾校练车预约系统 - 完整假数据脚本
-- =============================================

-- 1. 教练表假数据（密码：abc12345）
-- 使用 INSERT IGNORE 避免重复插入
INSERT IGNORE INTO
    `coach` (
        `coach_no`,
        `mobile`,
        `password`,
        `name`,
        `create_time`,
        `status`
    )
VALUES (
        'C001',
        '13900139001',
        '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH',
        '张三',
        NOW(),
        1
    ),
    (
        'C002',
        '13900139002',
        '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH',
        '李四',
        NOW(),
        1
    ),
    (
        'C003',
        '13900139003',
        '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH',
        '王五',
        NOW(),
        1
    ),
    (
        'C004',
        '13900139004',
        '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH',
        '赵六',
        NOW(),
        1
    ),
    (
        'C005',
        '13900139005',
        '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH',
        '钱七',
        NOW(),
        1
    );

-- 2. 车辆表假数据
INSERT IGNORE INTO
    `vehicle` (
        `plate_number`,
        `vehicle_type`,
        `coach_id`,
        `status`,
        `remark`,
        `create_time`,
        `update_time`
    )
VALUES (
        '京A12345',
        'C1',
        1,
        1,
        '大众桑塔纳-手动挡',
        NOW(),
        NOW()
    ),
    (
        '京A67890',
        'C1',
        2,
        1,
        '大众捷达-手动挡',
        NOW(),
        NOW()
    ),
    (
        '京B12345',
        'C2',
        3,
        1,
        '丰田卡罗拉-自动挡',
        NOW(),
        NOW()
    ),
    (
        '京B67890',
        'C2',
        4,
        1,
        '本田思域-自动挡',
        NOW(),
        NOW()
    ),
    (
        '京C12345',
        'C1',
        5,
        1,
        '现代伊兰特-手动挡',
        NOW(),
        NOW()
    );

-- 3. 学员表假数据（密码：abc12345，学号格式：S+年份+序号）
INSERT IGNORE INTO
    `student` (
        `student_no`,
        `mobile`,
        `password`,
        `user_name`,
        `create_time`,
        `status`,
        `no_show_count`,
        `car_type`,
        `required_hours`
    )
VALUES (
        'S26001',
        '13700137001',
        '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH',
        '陈明华',
        NOW(),
        1,
        0,
        'C1',
        12
    ),
    (
        'S26002',
        '13700137002',
        '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH',
        '李晓红',
        NOW(),
        1,
        0,
        'C1',
        12
    ),
    (
        'S26003',
        '13700137003',
        '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH',
        '王建国',
        NOW(),
        1,
        0,
        'C2',
        12
    ),
    (
        'S26004',
        '13700137004',
        '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH',
        '张丽娜',
        NOW(),
        1,
        0,
        'C2',
        12
    ),
    (
        'S26005',
        '13700137005',
        '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH',
        '刘志强',
        NOW(),
        1,
        0,
        'C1',
        12
    ),
    (
        'S26006',
        '13700137006',
        '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH',
        '赵小燕',
        NOW(),
        1,
        0,
        'C1',
        12
    ),
    (
        'S26007',
        '13700137007',
        '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH',
        '孙伟杰',
        NOW(),
        1,
        0,
        'C2',
        12
    ),
    (
        'S26008',
        '13700137008',
        '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH',
        '周美玲',
        NOW(),
        1,
        0,
        'C2',
        12
    ),
    (
        'S26009',
        '13700137009',
        '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH',
        '吴鹏飞',
        NOW(),
        1,
        0,
        'C1',
        12
    ),
    (
        'S26010',
        '13700137010',
        '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH',
        '郑雨婷',
        NOW(),
        1,
        0,
        'C1',
        12
    );

-- 4. 课程表假数据
INSERT INTO
    `schedule` (
        `coach_id`,
        `schedule_date`,
        `start_time`,
        `end_time`,
        `capacity`,
        `booked_count`,
        `status`,
        `create_time`,
        `update_time`
    )
VALUES (
        1,
        DATE_ADD(NOW(), INTERVAL 1 DAY),
        '08:00:00',
        '10:00:00',
        4,
        0,
        1,
        NOW(),
        NOW()
    ),
    (
        1,
        DATE_ADD(NOW(), INTERVAL 1 DAY),
        '10:00:00',
        '12:00:00',
        4,
        0,
        1,
        NOW(),
        NOW()
    ),
    (
        1,
        DATE_ADD(NOW(), INTERVAL 1 DAY),
        '14:00:00',
        '16:00:00',
        4,
        0,
        1,
        NOW(),
        NOW()
    ),
    (
        2,
        DATE_ADD(NOW(), INTERVAL 1 DAY),
        '08:00:00',
        '10:00:00',
        4,
        0,
        1,
        NOW(),
        NOW()
    ),
    (
        2,
        DATE_ADD(NOW(), INTERVAL 1 DAY),
        '14:00:00',
        '16:00:00',
        4,
        0,
        1,
        NOW(),
        NOW()
    ),
    (
        3,
        DATE_ADD(NOW(), INTERVAL 2 DAY),
        '08:00:00',
        '10:00:00',
        4,
        0,
        1,
        NOW(),
        NOW()
    ),
    (
        3,
        DATE_ADD(NOW(), INTERVAL 2 DAY),
        '14:00:00',
        '16:00:00',
        4,
        0,
        1,
        NOW(),
        NOW()
    ),
    (
        4,
        DATE_ADD(NOW(), INTERVAL 2 DAY),
        '08:00:00',
        '10:00:00',
        4,
        0,
        1,
        NOW(),
        NOW()
    ),
    (
        4,
        DATE_ADD(NOW(), INTERVAL 2 DAY),
        '14:00:00',
        '16:00:00',
        4,
        0,
        1,
        NOW(),
        NOW()
    ),
    (
        5,
        DATE_ADD(NOW(), INTERVAL 3 DAY),
        '08:00:00',
        '10:00:00',
        4,
        0,
        1,
        NOW(),
        NOW()
    ),
    (
        5,
        DATE_ADD(NOW(), INTERVAL 3 DAY),
        '10:00:00',
        '12:00:00',
        4,
        0,
        1,
        NOW(),
        NOW()
    ),
    (
        5,
        DATE_ADD(NOW(), INTERVAL 3 DAY),
        '14:00:00',
        '16:00:00',
        4,
        0,
        1,
        NOW(),
        NOW()
    );

-- 5. 预约表假数据（status: 1-已预约, 2-已完成, 3-已取消）
INSERT INTO
    `appointment` (
        `student_id`,
        `coach_id`,
        `vehicle_id`,
        `appointment_date`,
        `start_time`,
        `end_time`,
        `status`,
        `remark`,
        `create_time`,
        `update_time`
    )
VALUES (
        1,
        1,
        1,
        DATE_ADD(NOW(), INTERVAL 1 DAY),
        '08:00:00',
        '10:00:00',
        1,
        '科目二倒车入库练习',
        NOW(),
        NOW()
    ),
    (
        2,
        1,
        1,
        DATE_ADD(NOW(), INTERVAL 1 DAY),
        '08:00:00',
        '10:00:00',
        1,
        '科目二侧方停车练习',
        NOW(),
        NOW()
    ),
    (
        3,
        2,
        2,
        DATE_ADD(NOW(), INTERVAL 1 DAY),
        '14:00:00',
        '16:00:00',
        1,
        '科目三路考练习',
        NOW(),
        NOW()
    ),
    (
        4,
        2,
        2,
        DATE_ADD(NOW(), INTERVAL 1 DAY),
        '14:00:00',
        '16:00:00',
        1,
        '科目三灯光模拟',
        NOW(),
        NOW()
    ),
    (
        5,
        3,
        3,
        DATE_ADD(NOW(), INTERVAL 2 DAY),
        '08:00:00',
        '10:00:00',
        1,
        '科目二曲线行驶',
        NOW(),
        NOW()
    ),
    (
        6,
        3,
        3,
        DATE_ADD(NOW(), INTERVAL 2 DAY),
        '08:00:00',
        '10:00:00',
        1,
        '科目二直角转弯',
        NOW(),
        NOW()
    ),
    (
        7,
        4,
        4,
        DATE_ADD(NOW(), INTERVAL 2 DAY),
        '14:00:00',
        '16:00:00',
        1,
        '科目三变道练习',
        NOW(),
        NOW()
    ),
    (
        8,
        4,
        4,
        DATE_ADD(NOW(), INTERVAL 2 DAY),
        '14:00:00',
        '16:00:00',
        1,
        '科目三靠边停车',
        NOW(),
        NOW()
    ),
    (
        9,
        5,
        5,
        DATE_ADD(NOW(), INTERVAL 3 DAY),
        '08:00:00',
        '10:00:00',
        1,
        '科目二坡道起步',
        NOW(),
        NOW()
    ),
    (
        10,
        5,
        5,
        DATE_ADD(NOW(), INTERVAL 3 DAY),
        '08:00:00',
        '10:00:00',
        1,
        '科目二综合练习',
        NOW(),
        NOW()
    );

-- 6. 保养记录表假数据
INSERT INTO
    `maintenance_record` (
        `vehicle_id`,
        `coach_id`,
        `maintenance_type`,
        `maintenance_date`,
        `cost`,
        `mileage`,
        `description`,
        `next_maintenance_date`,
        `next_maintenance_mileage`,
        `create_time`,
        `update_time`
    )
VALUES (
        1,
        1,
        '常规保养',
        DATE_SUB(NOW(), INTERVAL 30 DAY),
        350.00,
        5000,
        '更换机油、机滤、检查刹车片',
        DATE_ADD(NOW(), INTERVAL 60 DAY),
        10000,
        NOW(),
        NOW()
    ),
    (
        2,
        2,
        '常规保养',
        DATE_SUB(NOW(), INTERVAL 45 DAY),
        380.00,
        8000,
        '更换机油、机滤、空滤、空调滤',
        DATE_ADD(NOW(), INTERVAL 45 DAY),
        13000,
        NOW(),
        NOW()
    ),
    (
        3,
        3,
        '常规保养',
        DATE_SUB(NOW(), INTERVAL 60 DAY),
        420.00,
        12000,
        '更换机油、机滤、空滤、检查轮胎',
        DATE_ADD(NOW(), INTERVAL 30 DAY),
        17000,
        NOW(),
        NOW()
    ),
    (
        4,
        4,
        '常规保养',
        DATE_SUB(NOW(), INTERVAL 15 DAY),
        360.00,
        3000,
        '更换机油、机滤、检查电瓶',
        DATE_ADD(NOW(), INTERVAL 75 DAY),
        8000,
        NOW(),
        NOW()
    ),
    (
        5,
        5,
        '常规保养',
        DATE_SUB(NOW(), INTERVAL 20 DAY),
        400.00,
        6000,
        '更换机油、机滤、空滤、检查悬挂',
        DATE_ADD(NOW(), INTERVAL 70 DAY),
        11000,
        NOW(),
        NOW()
    );

-- 7. 维修记录表假数据（status: 1-已完成, 0-进行中）
INSERT INTO
    `repair_record` (
        `vehicle_id`,
        `coach_id`,
        `repair_type`,
        `repair_date`,
        `cost`,
        `mileage`,
        `description`,
        `parts`,
        `repair_shop`,
        `status`,
        `create_time`,
        `update_time`
    )
VALUES (
        1,
        1,
        '小修',
        DATE_SUB(NOW(), INTERVAL 10 DAY),
        180.00,
        4500,
        '更换前雨刮器',
        '雨刮器总成',
        '4S店',
        1,
        NOW(),
        NOW()
    ),
    (
        2,
        2,
        '中修',
        DATE_SUB(NOW(), INTERVAL 20 DAY),
        850.00,
        7500,
        '更换前刹车片',
        '刹车片套装',
        '专业维修店',
        1,
        NOW(),
        NOW()
    ),
    (
        3,
        3,
        '小修',
        DATE_SUB(NOW(), INTERVAL 5 DAY),
        120.00,
        11500,
        '更换后尾灯',
        '尾灯总成',
        '4S店',
        1,
        NOW(),
        NOW()
    ),
    (
        4,
        4,
        '中修',
        DATE_SUB(NOW(), INTERVAL 15 DAY),
        680.00,
        2500,
        '更换蓄电池',
        '蓄电池',
        '专业维修店',
        1,
        NOW(),
        NOW()
    ),
    (
        5,
        5,
        '小修',
        DATE_SUB(NOW(), INTERVAL 8 DAY),
        450.00,
        5500,
        '更换左前轮胎',
        '轮胎',
        '轮胎专卖店',
        1,
        NOW(),
        NOW()
    );

-- 8. 系统配置表假数据
INSERT IGNORE INTO
    `system_config` (
        `config_key`,
        `config_value`,
        `description`,
        `create_time`,
        `update_time`
    )
VALUES (
        'appointment_advance_days',
        '7',
        '预约提前天数',
        NOW(),
        NOW()
    ),
    (
        'appointment_max_per_day',
        '2',
        '每天最大预约次数',
        NOW(),
        NOW()
    ),
    (
        'no_show_threshold',
        '3',
        '爽约阈值（达到后禁用账号）',
        NOW(),
        NOW()
    ),
    (
        'required_hours',
        '12',
        '默认要求学时（小时）',
        NOW(),
        NOW()
    ),
    (
        'maintenance_interval_days',
        '90',
        '保养间隔天数',
        NOW(),
        NOW()
    ),
    (
        'maintenance_interval_mileage',
        '5000',
        '保养间隔里程（公里）',
        NOW(),
        NOW()
    ),
    (
        'booking_time_limit',
        '2',
        '预约时间限制（小时前可取消）',
        NOW(),
        NOW()
    );

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
--   S26001 - 13700137001 - 陈明华 (C1)
--   S26002 - 13700137002 - 李晓红 (C1)
--   S26003 - 13700137003 - 王建国 (C2)
--   S26004 - 13700137004 - 张丽娜 (C2)
--   S26005 - 13700137005 - 刘志强 (C1)
--   S26006 - 13700137006 - 赵小燕 (C1)
--   S26007 - 13700137007 - 孙伟杰 (C2)
--   S26008 - 13700137008 - 周美玲 (C2)
--   S26009 - 13700137009 - 吴鹏飞 (C1)
--   S26010 - 13700137010 - 郑雨婷 (C1)
-- =============================================