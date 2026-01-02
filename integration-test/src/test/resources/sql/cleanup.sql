-- ============================================================
-- Integration Test Cleanup Script
-- ============================================================
-- 테스트 후 모든 테이블의 데이터를 삭제합니다.
-- 순서: FK 제약조건을 고려하여 자식 테이블부터 삭제
-- ============================================================

-- H2에서 FK 체크 비활성화
SET REFERENTIAL_INTEGRITY FALSE;

-- ============================================================
-- Brand & Category (Read-only sync data)
-- ============================================================
DELETE FROM brand;
DELETE FROM category;

-- ============================================================
-- Product Related Tables
-- ============================================================
DELETE FROM product_stocks;
DELETE FROM product_images;
DELETE FROM product_notices;
DELETE FROM product_descriptions;
DELETE FROM products;
DELETE FROM product_groups;

-- ============================================================
-- Seller Related Tables
-- ============================================================
DELETE FROM seller_shipping_policies;
DELETE FROM seller_refund_policies;
DELETE FROM seller_cs_infos;
DELETE FROM sellers;

-- ============================================================
-- Member & Auth Related Tables
-- ============================================================
DELETE FROM refresh_tokens;
DELETE FROM members;

-- ============================================================
-- Order Related Tables
-- ============================================================
DELETE FROM claim;
DELETE FROM order_event;
DELETE FROM payments;
DELETE FROM order_items;
DELETE FROM orders;
DELETE FROM checkout_items;
DELETE FROM checkouts;

-- ============================================================
-- Discount Related Tables
-- ============================================================
DELETE FROM discount_usage_histories;
DELETE FROM discount_policies;

-- ============================================================
-- User Data Tables
-- ============================================================
DELETE FROM shipping_addresses;
DELETE FROM refund_accounts;
DELETE FROM cart_items;
DELETE FROM carts;

-- ============================================================
-- Content & CMS Tables
-- ============================================================
DELETE FROM cms_banners;
DELETE FROM cms_components;
DELETE FROM cms_contents;
DELETE FROM cms_gnbs;

-- ============================================================
-- Review & QnA Tables
-- ============================================================
-- DELETE FROM reviews;  -- Entity not yet implemented
-- DELETE FROM qnas;     -- Entity not yet implemented

-- H2에서 FK 체크 활성화
SET REFERENTIAL_INTEGRITY TRUE;
