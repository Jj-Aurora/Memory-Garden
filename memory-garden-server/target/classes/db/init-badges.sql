-- ============================================================
-- 记忆花园系统 - 徽章初始数据
-- @author jLU
-- @date 2026-05-20
-- ============================================================

USE memory_garden;

-- 8 枚徽章定义
INSERT INTO t_badge (name, description, icon, rarity, condition_type, condition_value) VALUES
('初入花园', '种下第一棵知识植物', 'seed', 0, 'PLANT_FIRST', 1),
('坚持不懈', '连续打卡7天', 'streak-7', 0, 'STREAK_DAYS', 7),
('花开满园', '拥有5棵开花植物', 'bloom-5', 1, 'BLOOMING_COUNT', 5),
('硕果累累', '第一棵植物结果', 'fruit-1', 1, 'FRUIT_FIRST', 1),
('知识森林', '拥有20棵知识植物', 'forest', 2, 'TOTAL_PLANTS', 20),
('枯木逢春', '复活1棵枯萎植物', 'revive', 1, 'REVIVE_COUNT', 1),
('博学多才', '创建5个分类', 'category-5', 0, 'CATEGORY_COUNT', 5),
('记忆大师', '连续打卡30天', 'streak-30', 2, 'STREAK_DAYS', 30);
