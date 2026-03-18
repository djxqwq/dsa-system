-- 修复排班表中 capacity 为 NULL 的记录，设置默认值为 3
UPDATE schedule SET capacity = 3 WHERE capacity IS NULL;

-- 修复排班表中 booked_count 为 NULL 的记录，设置默认值为 0
UPDATE schedule SET booked_count = 0 WHERE booked_count IS NULL;