-- =============================================================================
-- Seller Test Data for REST API Admin Integration Tests
-- =============================================================================

-- 테스트 셀러 1: 승인 완료된 셀러
INSERT INTO sellers (id, seller_name, logo_url, description, approval_status, created_at, updated_at)
VALUES (1, '테스트 셀러 1', 'https://example.com/logo1.png', '테스트 셀러 1 설명입니다.', 'APPROVED', NOW(), NOW());

INSERT INTO seller_business_infos (id, seller_id, registration_number, sale_report_number, representative, address_line1, address_line2, zip_code, created_at, updated_at)
VALUES (1, 1, '1234567890', '2024-서울강남-0001', '홍길동', '서울시 강남구 테헤란로', '123번지 4층', '06234', NOW(), NOW());

INSERT INTO seller_cs_infos (id, seller_id, email, mobile_phone, landline_phone, created_at, updated_at)
VALUES (1, 1, 'cs1@example.com', '01012345678', '0212345678', NOW(), NOW());

-- 테스트 셀러 2: 승인 대기 중인 셀러
INSERT INTO sellers (id, seller_name, logo_url, description, approval_status, created_at, updated_at)
VALUES (2, '테스트 셀러 2', 'https://example.com/logo2.png', '테스트 셀러 2 설명입니다.', 'PENDING', NOW(), NOW());

INSERT INTO seller_business_infos (id, seller_id, registration_number, sale_report_number, representative, address_line1, address_line2, zip_code, created_at, updated_at)
VALUES (2, 2, '2345678901', NULL, '김영희', '서울시 서초구 반포대로', '456번지', '06535', NOW(), NOW());

INSERT INTO seller_cs_infos (id, seller_id, email, mobile_phone, landline_phone, created_at, updated_at)
VALUES (2, 2, 'cs2@example.com', '01023456789', NULL, NOW(), NOW());

-- 테스트 셀러 3: 거부된 셀러
INSERT INTO sellers (id, seller_name, logo_url, description, approval_status, created_at, updated_at)
VALUES (3, '테스트 셀러 3', NULL, '테스트 셀러 3 설명입니다.', 'REJECTED', NOW(), NOW());

INSERT INTO seller_business_infos (id, seller_id, registration_number, sale_report_number, representative, address_line1, address_line2, zip_code, created_at, updated_at)
VALUES (3, 3, '3456789012', NULL, '이철수', '경기도 성남시 분당구', '정자동 100', '13561', NOW(), NOW());

-- 테스트 셀러 4: 정지된 셀러
INSERT INTO sellers (id, seller_name, logo_url, description, approval_status, created_at, updated_at)
VALUES (4, '테스트 셀러 4', 'https://example.com/logo4.png', NULL, 'SUSPENDED', NOW(), NOW());

INSERT INTO seller_business_infos (id, seller_id, registration_number, sale_report_number, representative, address_line1, address_line2, zip_code, created_at, updated_at)
VALUES (4, 4, '4567890123', '2024-경기성남-0002', '박민수', '인천시 연수구', '송도동 200', '21990', NOW(), NOW());

INSERT INTO seller_cs_infos (id, seller_id, email, mobile_phone, landline_phone, created_at, updated_at)
VALUES (4, 4, 'cs4@example.com', NULL, '0324567890', NOW(), NOW());

-- 테스트 셀러 5: 추가 승인된 셀러 (페이징 테스트용)
INSERT INTO sellers (id, seller_name, logo_url, description, approval_status, created_at, updated_at)
VALUES (5, '테스트 셀러 5', 'https://example.com/logo5.png', '테스트 셀러 5 설명입니다.', 'APPROVED', NOW(), NOW());

INSERT INTO seller_business_infos (id, seller_id, registration_number, sale_report_number, representative, address_line1, address_line2, zip_code, created_at, updated_at)
VALUES (5, 5, '5678901234', '2024-부산해운대-0001', '최지영', '부산시 해운대구', '우동 300', '48099', NOW(), NOW());

INSERT INTO seller_cs_infos (id, seller_id, email, mobile_phone, landline_phone, created_at, updated_at)
VALUES (5, 5, 'cs5@example.com', '01056789012', '0515678901', NOW(), NOW());
