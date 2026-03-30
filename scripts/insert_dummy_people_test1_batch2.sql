-- test1 向けダミー追加（21〜40件目想定・メールは一意のドメイン）
USE persondb;

SET @uid := (SELECT id FROM app_users WHERE username = 'test1' LIMIT 1);

INSERT INTO people (user_id, name, email, phone, age, memo, created_at, updated_at) VALUES
(@uid, '石川蓮21', 'dummy21.test1@example.com', '03-2100-0021', 32, 'seed batch2 21', NOW(6), NOW(6)),
(@uid, '宮崎遥22', 'dummy22.test1@example.com', '090-2200-0022', 24, 'seed batch2 22', NOW(6), NOW(6)),
(@uid, '上田剛23', 'dummy23.test1@example.com', '03-2300-0023', 41, 'seed batch2 23', NOW(6), NOW(6)),
(@uid, '大野結衣24', 'dummy24.test1@example.com', '090-2400-0024', 22, 'seed batch2 24', NOW(6), NOW(6)),
(@uid, '星野拓25', 'dummy25.test1@example.com', '03-2500-0025', 29, 'seed batch2 25', NOW(6), NOW(6)),
(@uid, '藤井真央26', 'dummy26.test1@example.com', '090-2600-0026', 27, 'seed batch2 26', NOW(6), NOW(6)),
(@uid, '三浦亮27', 'dummy27.test1@example.com', '03-2700-0027', 36, 'seed batch2 27', NOW(6), NOW(6)),
(@uid, '坂本琴音28', 'dummy28.test1@example.com', '090-2800-0028', 21, 'seed batch2 28', NOW(6), NOW(6)),
(@uid, '岡田勲29', 'dummy29.test1@example.com', '03-2900-0029', 39, 'seed batch2 29', NOW(6), NOW(6)),
(@uid, '福田沙耶30', 'dummy30.test1@example.com', '090-3000-0030', 25, 'seed batch2 30', NOW(6), NOW(6)),
(@uid, '原口慎31', 'dummy31.test1@example.com', '03-3100-0031', 33, 'seed batch2 31', NOW(6), NOW(6)),
(@uid, '竹内芽依32', 'dummy32.test1@example.com', '090-3200-0032', 20, 'seed batch2 32', NOW(6), NOW(6)),
(@uid, '中島拓也33', 'dummy33.test1@example.com', '03-3300-0033', 44, 'seed batch2 33', NOW(6), NOW(6)),
(@uid, '松田彩花34', 'dummy34.test1@example.com', '090-3400-0034', 26, 'seed batch2 34', NOW(6), NOW(6)),
(@uid, '竹本隆35', 'dummy35.test1@example.com', '03-3500-0035', 31, 'seed batch2 35', NOW(6), NOW(6)),
(@uid, '河村奈央36', 'dummy36.test1@example.com', '090-3600-0036', 23, 'seed batch2 36', NOW(6), NOW(6)),
(@uid, '平井雄37', 'dummy37.test1@example.com', '03-3700-0037', 37, 'seed batch2 37', NOW(6), NOW(6)),
(@uid, '杉山実咲38', 'dummy38.test1@example.com', '090-3800-0038', 28, 'seed batch2 38', NOW(6), NOW(6)),
(@uid, '西山哲39', 'dummy39.test1@example.com', '03-3900-0039', 43, 'seed batch2 39', NOW(6), NOW(6)),
(@uid, '梅田優香40', 'dummy40.test1@example.com', '090-4000-0040', 24, 'seed batch2 40', NOW(6), NOW(6));
