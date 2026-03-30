-- test1 ユーザー（app_users.username = 'test1'）にのみ Person を20件追加するワンショット用 SQL
USE persondb;

SET @uid := (SELECT id FROM app_users WHERE username = 'test1' LIMIT 1);

INSERT INTO people (user_id, name, email, phone, age, memo, created_at, updated_at) VALUES
(@uid, '山田太郎01', 'dummy01.test1@example.com', '03-1000-0001', 22, 'seed 01', NOW(6), NOW(6)),
(@uid, '佐藤花子02', 'dummy02.test1@example.com', '090-2000-0002', 28, 'seed 02', NOW(6), NOW(6)),
(@uid, '鈴木一郎03', 'dummy03.test1@example.com', '03-3000-0003', 31, 'seed 03', NOW(6), NOW(6)),
(@uid, '高橋美咲04', 'dummy04.test1@example.com', '090-4000-0004', 24, 'seed 04', NOW(6), NOW(6)),
(@uid, '田中健05', 'dummy05.test1@example.com', '06-5000-0005', 35, 'seed 05', NOW(6), NOW(6)),
(@uid, '伊藤直樹06', 'dummy06.test1@example.com', '090-6000-0006', 29, 'seed 06', NOW(6), NOW(6)),
(@uid, '渡辺陽子07', 'dummy07.test1@example.com', '03-7000-0007', 40, 'seed 07', NOW(6), NOW(6)),
(@uid, '中村翼08', 'dummy08.test1@example.com', '090-8000-0008', 19, 'seed 08', NOW(6), NOW(6)),
(@uid, '小林誠09', 'dummy09.test1@example.com', '03-9000-0009', 45, 'seed 09', NOW(6), NOW(6)),
(@uid, '加藤舞10', 'dummy10.test1@example.com', '090-1000-0010', 27, 'seed 10', NOW(6), NOW(6)),
(@uid, '吉田修11', 'dummy11.test1@example.com', '03-1100-0011', 33, 'seed 11', NOW(6), NOW(6)),
(@uid, '山本奈々12', 'dummy12.test1@example.com', '090-1200-0012', 21, 'seed 12', NOW(6), NOW(6)),
(@uid, '井上浩13', 'dummy13.test1@example.com', '03-1300-0013', 38, 'seed 13', NOW(6), NOW(6)),
(@uid, '木村彩14', 'dummy14.test1@example.com', '090-1400-0014', 26, 'seed 14', NOW(6), NOW(6)),
(@uid, '林大輔15', 'dummy15.test1@example.com', '03-1500-0015', 30, 'seed 15', NOW(6), NOW(6)),
(@uid, '清水優16', 'dummy16.test1@example.com', '090-1600-0016', 23, 'seed 16', NOW(6), NOW(6)),
(@uid, '阿部隼人17', 'dummy17.test1@example.com', '03-1700-0017', 34, 'seed 17', NOW(6), NOW(6)),
(@uid, '森田葵18', 'dummy18.test1@example.com', '090-1800-0018', 20, 'seed 18', NOW(6), NOW(6)),
(@uid, '池田剛19', 'dummy19.test1@example.com', '03-1900-0019', 42, 'seed 19', NOW(6), NOW(6)),
(@uid, '橋本里佳20', 'dummy20.test1@example.com', '090-2000-0020', 25, 'seed 20', NOW(6), NOW(6));
