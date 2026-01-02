package com.ryuqq.setof.migration.refundaccount;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * 레거시 REFUND_ACCOUNT 테이블 조회 Repository
 *
 * <p>레거시 DB의 REFUND_ACCOUNT 테이블에서 데이터를 조회합니다. 조회 전용이며 쓰기 작업은 수행하지 않습니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class LegacyRefundAccountRepository {

    private final JdbcTemplate legacyJdbcTemplate;

    public LegacyRefundAccountRepository(
            @Qualifier("legacyJdbcTemplate") JdbcTemplate legacyJdbcTemplate) {
        this.legacyJdbcTemplate = legacyJdbcTemplate;
    }

    /**
     * 마이그레이션 대상 환불계좌 목록 조회
     *
     * @param offset 시작 오프셋
     * @param limit 조회 개수
     * @return 레거시 환불계좌 목록
     */
    public List<LegacyRefundAccountDto> findRefundAccountsForMigration(long offset, int limit) {
        String sql =
                """
                SELECT
                    REFUND_ACCOUNT_ID,
                    USER_ID,
                    BANK_NAME,
                    ACCOUNT_NUMBER,
                    ACCOUNT_HOLDER_NAME,
                    CREATE_AT,
                    MODIFIED_AT
                FROM REFUND_ACCOUNT
                ORDER BY REFUND_ACCOUNT_ID
                LIMIT ? OFFSET ?
                """;

        return legacyJdbcTemplate.query(sql, new LegacyRefundAccountRowMapper(), limit, offset);
    }

    /**
     * 전체 환불계좌 수 조회
     *
     * @return 전체 환불계좌 수
     */
    public long countAll() {
        String sql = "SELECT COUNT(*) FROM REFUND_ACCOUNT";
        Long count = legacyJdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0L;
    }

    /**
     * 특정 시간 이후 수정된 환불계좌 목록 조회 (증분 동기화용)
     *
     * @param modifiedAfter 이 시간 이후 수정된 환불계좌만 조회
     * @param limit 최대 조회 개수
     * @return 수정된 환불계좌 목록
     */
    public List<LegacyRefundAccountDto> findModifiedAfter(LocalDateTime modifiedAfter, int limit) {
        String sql =
                """
                SELECT
                    REFUND_ACCOUNT_ID,
                    USER_ID,
                    BANK_NAME,
                    ACCOUNT_NUMBER,
                    ACCOUNT_HOLDER_NAME,
                    CREATE_AT,
                    MODIFIED_AT
                FROM REFUND_ACCOUNT
                WHERE MODIFIED_AT > ?
                ORDER BY MODIFIED_AT
                LIMIT ?
                """;

        return legacyJdbcTemplate.query(
                sql, new LegacyRefundAccountRowMapper(), modifiedAfter, limit);
    }

    private static class LegacyRefundAccountRowMapper implements RowMapper<LegacyRefundAccountDto> {
        @Override
        public LegacyRefundAccountDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new LegacyRefundAccountDto(
                    rs.getLong("REFUND_ACCOUNT_ID"),
                    rs.getLong("USER_ID"),
                    rs.getString("BANK_NAME"),
                    rs.getString("ACCOUNT_NUMBER"),
                    rs.getString("ACCOUNT_HOLDER_NAME"),
                    getLocalDateTime(rs, "CREATE_AT"),
                    getLocalDateTime(rs, "MODIFIED_AT"));
        }

        private LocalDateTime getLocalDateTime(ResultSet rs, String columnName)
                throws SQLException {
            java.sql.Timestamp timestamp = rs.getTimestamp(columnName);
            return timestamp != null ? timestamp.toLocalDateTime() : null;
        }
    }
}
