-- Claim (클레임) 테이블 생성
-- 주문에 대한 취소/반품/교환/환불 요청을 관리합니다.

CREATE TABLE claim (
    -- 기본 식별자
    id BIGINT NOT NULL AUTO_INCREMENT,
    claim_id VARCHAR(36) NOT NULL COMMENT '클레임 비즈니스 ID (UUID)',
    claim_number VARCHAR(30) NOT NULL COMMENT '클레임 번호 (CLM-YYYYMMDD-XXXXXXXX)',

    -- 주문 연결
    order_id VARCHAR(36) NOT NULL COMMENT '주문 ID',
    order_item_id VARCHAR(36) NULL COMMENT '주문 상품 ID (전체 주문 클레임시 NULL)',

    -- 클레임 정보
    claim_type VARCHAR(20) NOT NULL COMMENT '클레임 유형 (CANCEL, RETURN, EXCHANGE, PARTIAL_REFUND)',
    claim_reason VARCHAR(50) NOT NULL COMMENT '클레임 사유',
    claim_reason_detail VARCHAR(500) NULL COMMENT '상세 사유',
    quantity INT NULL COMMENT '클레임 수량',
    refund_amount DECIMAL(15, 2) NULL COMMENT '환불 예정 금액',

    -- 처리 상태
    status VARCHAR(20) NOT NULL DEFAULT 'REQUESTED' COMMENT '클레임 상태 (REQUESTED, APPROVED, IN_PROGRESS, COMPLETED, REJECTED, CANCELLED)',
    processed_by VARCHAR(36) NULL COMMENT '처리자 ID',
    processed_at TIMESTAMP NULL COMMENT '처리 일시',
    reject_reason VARCHAR(500) NULL COMMENT '반려 사유',

    -- 반품 배송 정보 (기본)
    return_tracking_number VARCHAR(50) NULL COMMENT '반품 송장 번호',
    return_carrier VARCHAR(20) NULL COMMENT '반품 배송사',

    -- 반품 배송 관리 (확장)
    return_shipping_method VARCHAR(30) NULL COMMENT '반품 배송 방식 (SELLER_PICKUP, SELLER_PREPAID_LABEL, CUSTOMER_SHIP, CUSTOMER_VISIT)',
    return_shipping_status VARCHAR(30) NULL COMMENT '반품 배송 상태 (PENDING, PICKUP_SCHEDULED, PICKED_UP, IN_TRANSIT, RECEIVED)',
    return_pickup_scheduled_at TIMESTAMP NULL COMMENT '방문수거 예약 일시',
    return_pickup_address VARCHAR(500) NULL COMMENT '수거지 주소',
    return_customer_phone VARCHAR(20) NULL COMMENT '고객 연락처',
    return_received_at TIMESTAMP NULL COMMENT '반품 수령 일시',

    -- 검수 정보
    inspection_result VARCHAR(20) NULL COMMENT '검수 결과 (PASS, FAIL, PARTIAL)',
    inspection_note VARCHAR(500) NULL COMMENT '검수 메모',

    -- 교환 배송 정보
    exchange_tracking_number VARCHAR(50) NULL COMMENT '교환 송장 번호',
    exchange_carrier VARCHAR(20) NULL COMMENT '교환 배송사',
    exchange_shipped_at TIMESTAMP NULL COMMENT '교환품 발송 일시',
    exchange_delivered_at TIMESTAMP NULL COMMENT '교환품 수령 일시',

    -- 감사 필드
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',

    PRIMARY KEY (id),
    UNIQUE KEY uk_claim_claim_id (claim_id),
    UNIQUE KEY uk_claim_claim_number (claim_number),
    INDEX idx_claim_order_id (order_id),
    INDEX idx_claim_status (status),
    INDEX idx_claim_type (claim_type),
    INDEX idx_claim_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='클레임 테이블';
