package com.ryuqq.setof.migration.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * 동기화 상태 Repository
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class SyncStatusRepository {

    private final JdbcTemplate jdbcTemplate;

    public SyncStatusRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 도메인명으로 상태 조회
     *
     * @param domainName 도메인명
     * @return 동기화 상태
     */
    public Optional<SyncStatus> findByDomainName(String domainName) {
        String sql = "SELECT * FROM sync_status WHERE domain_name = ?";
        List<SyncStatus> results = jdbcTemplate.query(sql, new SyncStatusRowMapper(), domainName);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    /**
     * 모든 상태 조회
     *
     * @return 전체 동기화 상태 목록
     */
    public List<SyncStatus> findAll() {
        String sql = "SELECT * FROM sync_status ORDER BY domain_name";
        return jdbcTemplate.query(sql, new SyncStatusRowMapper());
    }

    /**
     * 활성화된 상태만 조회
     *
     * @return 활성화된 동기화 상태 목록
     */
    public List<SyncStatus> findAllActive() {
        String sql = "SELECT * FROM sync_status WHERE status = 'ACTIVE' ORDER BY domain_name";
        return jdbcTemplate.query(sql, new SyncStatusRowMapper());
    }

    /**
     * 동기화 완료 후 상태 업데이트
     *
     * @param domainName 도메인명
     * @param result 동기화 결과
     */
    public void updateAfterSync(String domainName, SyncResult result) {
        String sql =
                """
                UPDATE sync_status SET
                    last_sync_at = ?,
                    last_synced_count = ?,
                    total_synced_count = total_synced_count + ?,
                    error_message = ?
                WHERE domain_name = ?
                """;

        jdbcTemplate.update(
                sql,
                Timestamp.from(result.completedAt()),
                result.syncedCount(),
                result.syncedCount(),
                result.errorMessage(),
                domainName);
    }

    /**
     * 상태 변경
     *
     * @param domainName 도메인명
     * @param status 새 상태
     */
    public void updateStatus(String domainName, SyncStatusType status) {
        String sql = "UPDATE sync_status SET status = ? WHERE domain_name = ?";
        jdbcTemplate.update(sql, status.name(), domainName);
    }

    /**
     * 동기화 주기 변경
     *
     * @param domainName 도메인명
     * @param intervalMinutes 새 주기 (분)
     */
    public void updateSyncInterval(String domainName, int intervalMinutes) {
        String sql = "UPDATE sync_status SET sync_interval_minutes = ? WHERE domain_name = ?";
        jdbcTemplate.update(sql, intervalMinutes, domainName);
    }

    /**
     * 초기 마이그레이션을 위해 last_sync_at 리셋
     *
     * @param domainName 도메인명
     */
    public void resetLastSyncAt(String domainName) {
        String sql =
                "UPDATE sync_status SET last_sync_at = '1970-01-01 00:00:00' WHERE domain_name = ?";
        jdbcTemplate.update(sql, domainName);
    }

    private static class SyncStatusRowMapper implements RowMapper<SyncStatus> {
        @Override
        public SyncStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
            Timestamp lastSyncAt = rs.getTimestamp("last_sync_at");
            return new SyncStatus(
                    rs.getLong("id"),
                    rs.getString("domain_name"),
                    lastSyncAt != null ? lastSyncAt.toInstant() : Instant.EPOCH,
                    rs.getLong("last_synced_count"),
                    rs.getLong("total_synced_count"),
                    SyncStatusType.valueOf(rs.getString("status")),
                    rs.getInt("sync_interval_minutes"),
                    rs.getString("error_message"));
        }
    }
}
