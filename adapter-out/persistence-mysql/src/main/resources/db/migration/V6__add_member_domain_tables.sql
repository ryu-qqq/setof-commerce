-- =============================================
-- V6: Member Domain Tables
-- members, member_auths, member_consents,
-- shipping_addresses, refund_accounts,
-- wishlist_items, cart_items,
-- mileage_ledgers, mileage_entries, mileage_transactions
-- =============================================

-- 1. members
CREATE TABLE IF NOT EXISTS `members` (
    `id`                VARCHAR(36)     NOT NULL COMMENT 'UUIDv7 PK',
    `legacy_member_id`  BIGINT          DEFAULT NULL COMMENT '레거시 users.user_id 참조',
    `name`              VARCHAR(50)     NOT NULL,
    `email`             VARCHAR(255)    DEFAULT NULL,
    `phone_number`      VARCHAR(20)     NOT NULL,
    `date_of_birth`     DATE            DEFAULT NULL,
    `gender`            VARCHAR(10)     DEFAULT NULL COMMENT 'MALE, FEMALE, OTHER',
    `status`            VARCHAR(20)     NOT NULL DEFAULT 'ACTIVE' COMMENT 'ACTIVE, INACTIVE, SUSPENDED, WITHDRAWN',
    `created_at`        DATETIME(6)     NOT NULL,
    `updated_at`        DATETIME(6)     NOT NULL,
    `deleted_at`        DATETIME(6)     DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_members_phone_number` (`phone_number`),
    UNIQUE KEY `uk_members_legacy_member_id` (`legacy_member_id`),
    KEY `idx_members_status` (`status`),
    KEY `idx_members_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. member_auths
CREATE TABLE IF NOT EXISTS `member_auths` (
    `id`                BIGINT          NOT NULL AUTO_INCREMENT,
    `member_id`         VARCHAR(36)     NOT NULL COMMENT 'members.id 참조',
    `auth_provider`     VARCHAR(20)     NOT NULL COMMENT 'PHONE, KAKAO',
    `provider_user_id`  VARCHAR(255)    NOT NULL COMMENT '전화번호 또는 소셜 ID',
    `password_hash`     VARCHAR(255)    DEFAULT NULL COMMENT 'PHONE만 사용, 소셜은 NULL',
    `created_at`        DATETIME(6)     NOT NULL,
    `updated_at`        DATETIME(6)     NOT NULL,
    `deleted_at`        DATETIME(6)     DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_member_auths_member_id` (`member_id`),
    KEY `idx_member_auths_provider` (`auth_provider`, `provider_user_id`),
    KEY `idx_member_auths_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. member_consents
CREATE TABLE IF NOT EXISTS `member_consents` (
    `id`                     BIGINT       NOT NULL AUTO_INCREMENT,
    `member_id`              VARCHAR(36)  NOT NULL COMMENT 'members.id 참조',
    `privacy_consent`        TINYINT(1)   NOT NULL DEFAULT 0,
    `service_terms_consent`  TINYINT(1)   NOT NULL DEFAULT 0,
    `ad_consent`             TINYINT(1)   NOT NULL DEFAULT 0,
    `created_at`             DATETIME(6)  NOT NULL,
    `updated_at`             DATETIME(6)  NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_member_consents_member_id` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. shipping_addresses
CREATE TABLE IF NOT EXISTS `shipping_addresses` (
    `id`                     BIGINT       NOT NULL AUTO_INCREMENT,
    `member_id`              VARCHAR(36)  DEFAULT NULL COMMENT 'members.id 참조 (신규)',
    `legacy_member_id`       BIGINT       DEFAULT NULL COMMENT '레거시 user_id 참조',
    `receiver_name`          VARCHAR(50)  NOT NULL,
    `shipping_address_name`  VARCHAR(50)  NOT NULL,
    `address_line1`          VARCHAR(255) NOT NULL,
    `address_line2`          VARCHAR(255) DEFAULT NULL,
    `zip_code`               VARCHAR(10)  NOT NULL,
    `country`                VARCHAR(50)  NOT NULL,
    `delivery_request`       VARCHAR(500) DEFAULT NULL,
    `phone_number`           VARCHAR(20)  NOT NULL,
    `default_address`        TINYINT(1)   NOT NULL DEFAULT 0,
    `created_at`             DATETIME(6)  NOT NULL,
    `updated_at`             DATETIME(6)  NOT NULL,
    `deleted_at`             DATETIME(6)  DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_shipping_addresses_member_id` (`member_id`),
    KEY `idx_shipping_addresses_legacy_member_id` (`legacy_member_id`),
    KEY `idx_shipping_addresses_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. refund_accounts
CREATE TABLE IF NOT EXISTS `refund_accounts` (
    `id`                   BIGINT       NOT NULL AUTO_INCREMENT,
    `member_id`            VARCHAR(36)  NOT NULL COMMENT 'members.id 참조',
    `bank_name`            VARCHAR(50)  NOT NULL,
    `account_number`       VARCHAR(50)  NOT NULL,
    `account_holder_name`  VARCHAR(50)  NOT NULL,
    `created_at`           DATETIME(6)  NOT NULL,
    `updated_at`           DATETIME(6)  NOT NULL,
    `deleted_at`           DATETIME(6)  DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_refund_accounts_member_id` (`member_id`),
    KEY `idx_refund_accounts_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. wishlist_items
CREATE TABLE IF NOT EXISTS `wishlist_items` (
    `id`                BIGINT       NOT NULL AUTO_INCREMENT,
    `member_id`         VARCHAR(36)  DEFAULT NULL COMMENT 'members.id 참조 (신규)',
    `legacy_member_id`  BIGINT       DEFAULT NULL COMMENT '레거시 user_id 참조',
    `product_group_id`  BIGINT       NOT NULL,
    `created_at`        DATETIME(6)  NOT NULL,
    `deleted_at`        DATETIME(6)  DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_wishlist_items_member_id` (`member_id`),
    KEY `idx_wishlist_items_legacy_member_id` (`legacy_member_id`),
    KEY `idx_wishlist_items_product_group_id` (`product_group_id`),
    KEY `idx_wishlist_items_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. cart_items
CREATE TABLE IF NOT EXISTS `cart_items` (
    `id`                BIGINT       NOT NULL AUTO_INCREMENT,
    `member_id`         VARCHAR(36)  DEFAULT NULL COMMENT 'members.id 참조 (신규)',
    `legacy_user_id`    BIGINT       DEFAULT NULL COMMENT '레거시 user_id 참조',
    `product_group_id`  BIGINT       NOT NULL,
    `product_id`        BIGINT       NOT NULL,
    `seller_id`         BIGINT       DEFAULT NULL,
    `quantity`           INT          NOT NULL DEFAULT 1,
    `created_at`        DATETIME(6)  NOT NULL,
    `updated_at`        DATETIME(6)  NOT NULL,
    `deleted_at`        DATETIME(6)  DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_cart_items_member_id` (`member_id`),
    KEY `idx_cart_items_legacy_user_id` (`legacy_user_id`),
    KEY `idx_cart_items_product_id` (`product_id`),
    KEY `idx_cart_items_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 8. mileage_ledgers (테이블만, 기능은 나중에)
CREATE TABLE IF NOT EXISTS `mileage_ledgers` (
    `id`              BIGINT       NOT NULL AUTO_INCREMENT,
    `member_id`       VARCHAR(36)  NOT NULL COMMENT 'members.id 참조',
    `legacy_user_id`  BIGINT       DEFAULT NULL COMMENT '레거시 user_id 참조',
    `created_at`      DATETIME(6)  NOT NULL,
    `updated_at`      DATETIME(6)  NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_mileage_ledgers_member_id` (`member_id`),
    KEY `idx_mileage_ledgers_legacy_user_id` (`legacy_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 9. mileage_entries (테이블만, 기능은 나중에)
CREATE TABLE IF NOT EXISTS `mileage_entries` (
    `id`                BIGINT       NOT NULL AUTO_INCREMENT,
    `mileage_ledger_id` BIGINT       NOT NULL COMMENT 'mileage_ledgers.id 참조',
    `earned_amount`     BIGINT       NOT NULL,
    `used_amount`       BIGINT       NOT NULL DEFAULT 0,
    `status`            VARCHAR(20)  NOT NULL COMMENT 'ACTIVE, EXHAUSTED, EXPIRED, REVOKED',
    `issue_type`        VARCHAR(30)  NOT NULL COMMENT 'PURCHASE, SIGNUP, REVIEW, EVENT, ADMIN, REFUND',
    `title`             VARCHAR(200) NOT NULL,
    `target_id`         BIGINT       DEFAULT NULL COMMENT '주문ID 등 관련 엔티티 참조',
    `issued_at`         DATETIME(6)  NOT NULL,
    `expiration_date`   DATETIME(6)  DEFAULT NULL,
    `created_at`        DATETIME(6)  NOT NULL,
    `updated_at`        DATETIME(6)  NOT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_mileage_entries_ledger_id` (`mileage_ledger_id`),
    KEY `idx_mileage_entries_status` (`status`),
    KEY `idx_mileage_entries_expiration` (`expiration_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 10. mileage_transactions (테이블만, 기능은 나중에)
CREATE TABLE IF NOT EXISTS `mileage_transactions` (
    `id`                  BIGINT       NOT NULL AUTO_INCREMENT,
    `mileage_entry_id`    BIGINT       NOT NULL COMMENT 'mileage_entries.id 참조',
    `change_amount`       BIGINT       NOT NULL COMMENT '양수=적립, 음수=사용/차감',
    `reason`              VARCHAR(30)  NOT NULL COMMENT 'EARN, USE, REFUND, EXPIRE, REVOKE',
    `related_order_id`    BIGINT       DEFAULT NULL,
    `related_payment_id`  BIGINT       DEFAULT NULL,
    `description`         VARCHAR(500) DEFAULT NULL,
    `created_at`          DATETIME(6)  NOT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_mileage_transactions_entry_id` (`mileage_entry_id`),
    KEY `idx_mileage_transactions_reason` (`reason`),
    KEY `idx_mileage_transactions_order_id` (`related_order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
