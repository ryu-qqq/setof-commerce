-- ============================================================
-- V3: Display 도메인 복합 인덱스 + 전시 오버라이드 컬럼 추가
-- ============================================================
-- 1) 쿼리 패턴 기반 복합 인덱스 추가
-- 2) component_fixed_product에 전시명/전시이미지 오버라이드 컬럼 추가
--    (레거시 component_item.PRODUCT_DISPLAY_NAME / PRODUCT_DISPLAY_IMAGE 대응)
-- ============================================================

-- component_fixed_product: 전시 오버라이드 컬럼 추가
-- NULL이면 원본 상품 정보 사용, 값이 있으면 전시 영역에서만 덮어쓰기
ALTER TABLE component_fixed_product
    ADD COLUMN display_name varchar(200) DEFAULT NULL AFTER product_group_id,
    ADD COLUMN display_image_url varchar(500) DEFAULT NULL AFTER display_name;

-- banner_group: WHERE banner_type = ? AND active = 1 AND deleted_at IS NULL
CREATE INDEX idx_banner_group_type_active ON banner_group (banner_type, active, deleted_at);

-- content_page: WHERE active = 1 AND deleted_at IS NULL
CREATE INDEX idx_content_page_active ON content_page (active, deleted_at);

-- navigation_menu: WHERE active = 1 AND deleted_at IS NULL ORDER BY display_order
CREATE INDEX idx_navigation_menu_active_order ON navigation_menu (active, deleted_at, display_order);

-- display_component: WHERE content_page_id = ? AND active = 1 AND deleted_at IS NULL
CREATE INDEX idx_display_component_page_active ON display_component (content_page_id, active, deleted_at);

-- component_fixed_product: WHERE component_id IN (...) AND deleted_at IS NULL
CREATE INDEX idx_cfp_component_deleted ON component_fixed_product (component_id, deleted_at);

-- component_fixed_product: WHERE tab_id IN (...) AND deleted_at IS NULL
CREATE INDEX idx_cfp_tab_deleted ON component_fixed_product (tab_id, deleted_at);

-- component_fixed_product: product_group_id 조회용 (cross-domain JOIN)
CREATE INDEX idx_cfp_product_group_id ON component_fixed_product (product_group_id);
