-- Brand 테이블: display_name → display_korean_name + display_english_name 분리
ALTER TABLE `brand`
    ADD COLUMN `display_korean_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' AFTER `brand_icon_image_url`,
    ADD COLUMN `display_english_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' AFTER `display_korean_name`;

-- 기존 display_name 값을 korean/english 모두에 복사 (데이터 마이그레이션)
UPDATE `brand` SET `display_korean_name` = `display_name`, `display_english_name` = `display_name` WHERE `display_korean_name` = '';

-- 기존 display_name 컬럼 삭제
ALTER TABLE `brand` DROP COLUMN `display_name`;
