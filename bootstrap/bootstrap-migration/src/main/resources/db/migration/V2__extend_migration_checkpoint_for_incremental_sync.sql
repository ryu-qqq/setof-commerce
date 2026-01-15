-- ============================================
-- Migration Checkpoint 테이블 확장
-- Purpose: 증분 동기화(Incremental Sync) 지원
-- ============================================

-- 1. last_synced_at 컬럼 추가 (증분 동기화의 시작점)
ALTER TABLE migration_checkpoint
    ADD COLUMN last_synced_at TIMESTAMP NULL COMMENT '마지막 증분 동기화 기준 시점 (레거시 UPDATE_DATE)';

-- 2. sync_mode 컬럼 추가 (동기화 모드)
ALTER TABLE migration_checkpoint
    ADD COLUMN sync_mode VARCHAR(20) NOT NULL DEFAULT 'INITIAL' COMMENT 'INITIAL(초기), INCREMENTAL(증분), FULL(전체)';

-- 3. 동기화 간격 설정 컬럼 추가
ALTER TABLE migration_checkpoint
    ADD COLUMN sync_interval_seconds INT NOT NULL DEFAULT 300 COMMENT '증분 동기화 간격 (초)';

-- 4. 마지막 배치 크기 기록
ALTER TABLE migration_checkpoint
    ADD COLUMN last_batch_size INT NOT NULL DEFAULT 0 COMMENT '마지막 배치에서 처리된 건수';

-- 5. 스킵된 레코드 수 기록
ALTER TABLE migration_checkpoint
    ADD COLUMN skipped_count BIGINT NOT NULL DEFAULT 0 COMMENT '중복/스킵된 누적 건수';

-- 6. 실패한 레코드 수 기록
ALTER TABLE migration_checkpoint
    ADD COLUMN failed_count BIGINT NOT NULL DEFAULT 0 COMMENT '실패한 누적 건수';

-- 7. 인덱스 추가
CREATE INDEX idx_migration_checkpoint_sync_mode ON migration_checkpoint (sync_mode);
CREATE INDEX idx_migration_checkpoint_last_synced ON migration_checkpoint (last_synced_at);

-- ============================================
-- 도메인별 sync_interval 초기값 설정
-- ============================================
UPDATE migration_checkpoint SET sync_interval_seconds = 300 WHERE domain_name = 'member';           -- 5분
UPDATE migration_checkpoint SET sync_interval_seconds = 60  WHERE domain_name = 'product';          -- 1분
UPDATE migration_checkpoint SET sync_interval_seconds = 60  WHERE domain_name = 'product_group';    -- 1분
UPDATE migration_checkpoint SET sync_interval_seconds = 60  WHERE domain_name = 'order';            -- 1분
UPDATE migration_checkpoint SET sync_interval_seconds = 60  WHERE domain_name = 'payment';          -- 1분
UPDATE migration_checkpoint SET sync_interval_seconds = 300 WHERE domain_name = 'shipping_address'; -- 5분
UPDATE migration_checkpoint SET sync_interval_seconds = 300 WHERE domain_name = 'refund_account';   -- 5분
UPDATE migration_checkpoint SET sync_interval_seconds = 1800 WHERE domain_name = 'review';          -- 30분
UPDATE migration_checkpoint SET sync_interval_seconds = 1800 WHERE domain_name = 'qna';             -- 30분
