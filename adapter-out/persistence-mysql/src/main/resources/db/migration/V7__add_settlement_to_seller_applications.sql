-- V7: seller_applications 테이블에 정산 정보 컬럼 추가
-- 입점 신청 시 정산 계좌 정보를 함께 받기 위한 스키마 변경

ALTER TABLE seller_applications
    ADD COLUMN bank_code VARCHAR(10) NULL COMMENT '은행 코드' AFTER contact_phone_number,
    ADD COLUMN bank_name VARCHAR(50) NOT NULL DEFAULT '' COMMENT '은행명' AFTER bank_code,
    ADD COLUMN account_number VARCHAR(30) NOT NULL DEFAULT '' COMMENT '계좌번호' AFTER bank_name,
    ADD COLUMN account_holder_name VARCHAR(50) NOT NULL DEFAULT '' COMMENT '예금주' AFTER account_number,
    ADD COLUMN settlement_cycle VARCHAR(20) NOT NULL DEFAULT 'MONTHLY' COMMENT '정산 주기 (WEEKLY, BIWEEKLY, MONTHLY)' AFTER account_holder_name,
    ADD COLUMN settlement_day INT NOT NULL DEFAULT 1 COMMENT '정산일 (1-31)' AFTER settlement_cycle;

-- 기존 데이터가 없다면 DEFAULT 제약 제거 (선택 사항 - 프로덕션에서는 데이터 마이그레이션 후 진행)
-- ALTER TABLE seller_applications
--     ALTER COLUMN bank_name DROP DEFAULT,
--     ALTER COLUMN account_number DROP DEFAULT,
--     ALTER COLUMN account_holder_name DROP DEFAULT,
--     ALTER COLUMN settlement_cycle DROP DEFAULT,
--     ALTER COLUMN settlement_day DROP DEFAULT;
