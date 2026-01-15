package com.ryuqq.setof.migration.core.checkpoint;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 마이그레이션 체크포인트 Repository
 *
 * <p>migration_checkpoint 테이블에 접근하여 마이그레이션 진행 상태를 관리합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class MigrationCheckpointRepository {

    private static final Logger log = LoggerFactory.getLogger(MigrationCheckpointRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public MigrationCheckpointRepository(
            @Qualifier("migrationJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 도메인명으로 체크포인트 조회
     *
     * @param domainName 도메인명
     * @return 체크포인트 (없으면 empty)
     */
    public Optional<MigrationCheckpoint> findByDomainName(String domainName) {
        String sql = "SELECT * FROM migration_checkpoint WHERE domain_name = ?";
        List<MigrationCheckpoint> results =
                jdbcTemplate.query(sql, new MigrationCheckpointRowMapper(), domainName);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    /**
     * 모든 체크포인트 조회
     *
     * @return 전체 체크포인트 목록
     */
    public List<MigrationCheckpoint> findAll() {
        String sql = "SELECT * FROM migration_checkpoint ORDER BY domain_name";
        return jdbcTemplate.query(sql, new MigrationCheckpointRowMapper());
    }

    /**
     * 마이그레이션 가능한 체크포인트 조회
     *
     * @return PENDING, FAILED, PAUSED 상태의 체크포인트 목록
     */
    public List<MigrationCheckpoint> findMigratable() {
        String sql =
                """
                SELECT * FROM migration_checkpoint
                WHERE status IN ('PENDING', 'FAILED', 'PAUSED')
                ORDER BY domain_name
                """;
        return jdbcTemplate.query(sql, new MigrationCheckpointRowMapper());
    }

    /**
     * 마이그레이션 시작 시 상태 업데이트
     *
     * @param domainName 도메인명
     * @param legacyTotalCount 레거시 전체 건수
     */
    @Transactional
    public void startMigration(String domainName, long legacyTotalCount) {
        String sql =
                """
                UPDATE migration_checkpoint SET
                    status = 'RUNNING',
                    legacy_total_count = ?,
                    last_executed_at = NOW(),
                    error_message = NULL
                WHERE domain_name = ?
                """;
        int updated = jdbcTemplate.update(sql, legacyTotalCount, domainName);
        log.info(
                "Migration started. domain={}, legacyTotalCount={}, updated={}",
                domainName,
                legacyTotalCount,
                updated);
    }

    /**
     * 청크 처리 완료 후 체크포인트 업데이트
     *
     * @param domainName 도메인명
     * @param lastMigratedId 마지막으로 처리한 레거시 PK
     * @param chunkCount 이번 청크에서 처리한 건수
     */
    @Transactional
    public void updateCheckpoint(String domainName, long lastMigratedId, long chunkCount) {
        String sql =
                """
                UPDATE migration_checkpoint SET
                    last_migrated_id = ?,
                    migrated_count = migrated_count + ?
                WHERE domain_name = ?
                """;
        int updated = jdbcTemplate.update(sql, lastMigratedId, chunkCount, domainName);
        log.debug(
                "Checkpoint updated. domain={}, lastMigratedId={}, chunkCount={}, updated={}",
                domainName,
                lastMigratedId,
                chunkCount,
                updated);
    }

    /**
     * 마이그레이션 완료 처리
     *
     * @param domainName 도메인명
     * @param executionTimeMs 총 실행 시간 (ms)
     */
    @Transactional
    public void completeMigration(String domainName, long executionTimeMs) {
        String sql =
                """
                UPDATE migration_checkpoint SET
                    status = 'COMPLETED',
                    last_completed_at = NOW(),
                    execution_time_ms = ?
                WHERE domain_name = ?
                """;
        int updated = jdbcTemplate.update(sql, executionTimeMs, domainName);
        log.info(
                "Migration completed. domain={}, executionTimeMs={}, updated={}",
                domainName,
                executionTimeMs,
                updated);
    }

    /**
     * 마이그레이션 실패 처리
     *
     * @param domainName 도메인명
     * @param errorMessage 에러 메시지
     */
    @Transactional
    public void failMigration(String domainName, String errorMessage) {
        String sql =
                """
                UPDATE migration_checkpoint SET
                    status = 'FAILED',
                    error_message = ?
                WHERE domain_name = ?
                """;
        int updated = jdbcTemplate.update(sql, errorMessage, domainName);
        log.error(
                "Migration failed. domain={}, error={}, updated={}",
                domainName,
                errorMessage,
                updated);
    }

    /**
     * 마이그레이션 일시 중지
     *
     * @param domainName 도메인명
     */
    @Transactional
    public void pauseMigration(String domainName) {
        String sql =
                """
                UPDATE migration_checkpoint SET
                    status = 'PAUSED'
                WHERE domain_name = ?
                """;
        int updated = jdbcTemplate.update(sql, domainName);
        log.info("Migration paused. domain={}, updated={}", domainName, updated);
    }

    /**
     * 체크포인트 리셋 (처음부터 다시 시작)
     *
     * @param domainName 도메인명
     */
    @Transactional
    public void resetCheckpoint(String domainName) {
        String sql =
                """
                UPDATE migration_checkpoint SET
                    last_migrated_id = 0,
                    migrated_count = 0,
                    legacy_total_count = NULL,
                    status = 'PENDING',
                    error_message = NULL,
                    last_executed_at = NULL,
                    last_completed_at = NULL,
                    execution_time_ms = NULL
                WHERE domain_name = ?
                """;
        int updated = jdbcTemplate.update(sql, domainName);
        log.info("Checkpoint reset. domain={}, updated={}", domainName, updated);
    }

    /**
     * 증분 동기화 완료 후 체크포인트 업데이트
     *
     * @param domainName 도메인명
     * @param lastSyncedAt 마지막 동기화 기준 시점
     * @param syncedCount 이번 배치에서 동기화된 건수
     * @param skippedCount 스킵된 건수
     */
    @Transactional
    public void updateIncrementalCheckpoint(
            String domainName, Instant lastSyncedAt, int syncedCount, int skippedCount) {
        String sql =
                """
                UPDATE migration_checkpoint SET
                    last_synced_at = ?,
                    last_batch_size = ?,
                    migrated_count = migrated_count + ?,
                    skipped_count = skipped_count + ?,
                    sync_mode = 'INCREMENTAL'
                WHERE domain_name = ?
                """;
        int updated =
                jdbcTemplate.update(
                        sql,
                        Timestamp.from(lastSyncedAt),
                        syncedCount,
                        syncedCount,
                        skippedCount,
                        domainName);
        log.debug(
                "Incremental checkpoint updated. domain={}, lastSyncedAt={}, syncedCount={},"
                        + " skippedCount={}",
                domainName,
                lastSyncedAt,
                syncedCount,
                skippedCount);
    }

    /**
     * 증분 동기화를 위한 마지막 동기화 시점 조회
     *
     * @param domainName 도메인명
     * @return 마지막 동기화 시점 (없으면 empty)
     */
    public Optional<Instant> findLastSyncedAt(String domainName) {
        String sql = "SELECT last_synced_at FROM migration_checkpoint WHERE domain_name = ?";
        List<Timestamp> results = jdbcTemplate.queryForList(sql, Timestamp.class, domainName);
        if (results.isEmpty() || results.get(0) == null) {
            return Optional.empty();
        }
        return Optional.of(results.get(0).toInstant());
    }

    /**
     * 초기 마이그레이션 완료 후 증분 동기화 모드로 전환
     *
     * @param domainName 도메인명
     */
    @Transactional
    public void switchToIncrementalMode(String domainName) {
        String sql =
                """
                UPDATE migration_checkpoint SET
                    sync_mode = 'INCREMENTAL',
                    last_synced_at = NOW(),
                    status = 'COMPLETED'
                WHERE domain_name = ?
                """;
        int updated = jdbcTemplate.update(sql, domainName);
        log.info("Switched to incremental mode. domain={}, updated={}", domainName, updated);
    }

    /**
     * 실패 카운트 증가
     *
     * @param domainName 도메인명
     * @param count 실패 건수
     */
    @Transactional
    public void incrementFailedCount(String domainName, int count) {
        String sql =
                """
                UPDATE migration_checkpoint SET
                    failed_count = failed_count + ?
                WHERE domain_name = ?
                """;
        jdbcTemplate.update(sql, count, domainName);
    }

    private static class MigrationCheckpointRowMapper implements RowMapper<MigrationCheckpoint> {

        @Override
        public MigrationCheckpoint mapRow(ResultSet rs, int rowNum) throws SQLException {
            String syncModeStr = rs.getString("sync_mode");
            SyncMode syncMode =
                    syncModeStr != null ? SyncMode.valueOf(syncModeStr) : SyncMode.INITIAL;

            return new MigrationCheckpoint(
                    rs.getLong("id"),
                    rs.getString("domain_name"),
                    rs.getLong("last_migrated_id"),
                    rs.getLong("migrated_count"),
                    rs.getObject("legacy_total_count", Long.class),
                    MigrationCheckpointStatus.valueOf(rs.getString("status")),
                    rs.getString("error_message"),
                    toInstant(rs.getTimestamp("last_executed_at")),
                    toInstant(rs.getTimestamp("last_completed_at")),
                    rs.getObject("execution_time_ms", Long.class),
                    toInstant(rs.getTimestamp("last_synced_at")),
                    syncMode,
                    rs.getInt("sync_interval_seconds"),
                    rs.getInt("last_batch_size"),
                    rs.getLong("skipped_count"),
                    rs.getLong("failed_count"),
                    toInstant(rs.getTimestamp("created_at")),
                    toInstant(rs.getTimestamp("updated_at")));
        }

        private Instant toInstant(Timestamp timestamp) {
            return timestamp != null ? timestamp.toInstant() : null;
        }
    }
}
