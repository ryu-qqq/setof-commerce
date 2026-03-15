-- =============================================
-- V7: Member PK from VARCHAR(36) to BIGINT AUTO_INCREMENT
-- members.id, member_auths.member_id, member_consents.member_id,
-- shipping_addresses.member_id, refund_accounts.member_id,
-- wishlist_items.member_id, cart_items.member_id,
-- mileage_ledgers.member_id
-- =============================================

-- 1. members: Drop existing PK, add new BIGINT AUTO_INCREMENT PK
-- Drop FK-referencing unique keys first, then alter members table
ALTER TABLE `member_auths` DROP KEY `idx_member_auths_member_id`;
ALTER TABLE `member_consents` DROP KEY `uk_member_consents_member_id`;
ALTER TABLE `shipping_addresses` DROP KEY `idx_shipping_addresses_member_id`;
ALTER TABLE `refund_accounts` DROP KEY `idx_refund_accounts_member_id`;
ALTER TABLE `wishlist_items` DROP KEY `idx_wishlist_items_member_id`;
ALTER TABLE `cart_items` DROP KEY `idx_cart_items_member_id`;
ALTER TABLE `mileage_ledgers` DROP KEY `uk_mileage_ledgers_member_id`;

-- Alter member_id columns in child tables to BIGINT
ALTER TABLE `member_auths` MODIFY COLUMN `member_id` BIGINT NOT NULL COMMENT 'members.id 참조';
ALTER TABLE `member_consents` MODIFY COLUMN `member_id` BIGINT NOT NULL COMMENT 'members.id 참조';
ALTER TABLE `shipping_addresses` MODIFY COLUMN `member_id` BIGINT DEFAULT NULL COMMENT 'members.id 참조 (신규)';
ALTER TABLE `refund_accounts` MODIFY COLUMN `member_id` BIGINT NOT NULL COMMENT 'members.id 참조';
ALTER TABLE `wishlist_items` MODIFY COLUMN `member_id` BIGINT DEFAULT NULL COMMENT 'members.id 참조 (신규)';
ALTER TABLE `cart_items` MODIFY COLUMN `member_id` BIGINT DEFAULT NULL COMMENT 'members.id 참조 (신규)';
ALTER TABLE `mileage_ledgers` MODIFY COLUMN `member_id` BIGINT NOT NULL COMMENT 'members.id 참조';

-- Drop members PK and change to BIGINT AUTO_INCREMENT
ALTER TABLE `members` DROP PRIMARY KEY;
ALTER TABLE `members` DROP COLUMN `id`;
ALTER TABLE `members` ADD COLUMN `id` BIGINT NOT NULL AUTO_INCREMENT FIRST, ADD PRIMARY KEY (`id`);

-- Drop legacy_member_id column (no longer needed, member.id IS the Long PK)
ALTER TABLE `members` DROP KEY `uk_members_legacy_member_id`;
ALTER TABLE `members` DROP COLUMN `legacy_member_id`;

-- Re-add indexes on child tables
ALTER TABLE `member_auths` ADD KEY `idx_member_auths_member_id` (`member_id`);
ALTER TABLE `member_consents` ADD UNIQUE KEY `uk_member_consents_member_id` (`member_id`);
ALTER TABLE `shipping_addresses` ADD KEY `idx_shipping_addresses_member_id` (`member_id`);
ALTER TABLE `refund_accounts` ADD KEY `idx_refund_accounts_member_id` (`member_id`);
ALTER TABLE `wishlist_items` ADD KEY `idx_wishlist_items_member_id` (`member_id`);
ALTER TABLE `cart_items` ADD KEY `idx_cart_items_member_id` (`member_id`);
ALTER TABLE `mileage_ledgers` ADD UNIQUE KEY `uk_mileage_ledgers_member_id` (`member_id`);
