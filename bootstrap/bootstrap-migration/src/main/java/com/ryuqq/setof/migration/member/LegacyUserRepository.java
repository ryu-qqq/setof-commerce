package com.ryuqq.setof.migration.member;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * 레거시 Users 테이블 조회 Repository
 *
 * <p>레거시 DB의 Users 테이블에서 데이터를 조회합니다. 조회 전용이며 쓰기 작업은 수행하지 않습니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class LegacyUserRepository {

    private final JdbcTemplate legacyJdbcTemplate;

    public LegacyUserRepository(@Qualifier("legacyJdbcTemplate") JdbcTemplate legacyJdbcTemplate) {
        this.legacyJdbcTemplate = legacyJdbcTemplate;
    }

    /**
     * 마이그레이션 대상 사용자 목록 조회
     *
     * <p>지정된 오프셋부터 limit 개수만큼 사용자를 조회합니다.
     *
     * @param offset 시작 오프셋
     * @param limit 조회 개수
     * @return 레거시 사용자 목록
     */
    public List<LegacyUserDto> findUsersForMigration(long offset, int limit) {
        String sql =
                """
                SELECT
                    USER_ID,
                    SOCIAL_PK_ID,
                    PHONE_NUMBER,
                    EMAIL,
                    PASSWORD_HASH,
                    NAME,
                    DATE_OF_BIRTH,
                    GENDER,
                    PROVIDER,
                    STATUS,
                    PRIVACY_CONSENT,
                    SERVICE_TERMS_CONSENT,
                    AD_CONSENT,
                    WITHDRAWAL_REASON,
                    WITHDRAWN_AT,
                    CREATED_AT,
                    UPDATED_AT,
                    DELETED_AT
                FROM USERS
                ORDER BY USER_ID
                LIMIT ? OFFSET ?
                """;

        return legacyJdbcTemplate.query(sql, new LegacyUserRowMapper(), limit, offset);
    }

    /**
     * 전체 사용자 수 조회
     *
     * @return 전체 사용자 수
     */
    public long countAllUsers() {
        String sql = "SELECT COUNT(*) FROM USERS";
        Long count = legacyJdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0L;
    }

    /**
     * 특정 사용자 ID 이후의 사용자 수 조회 (마이그레이션 진행률 확인용)
     *
     * @param lastMigratedUserId 마지막으로 마이그레이션된 사용자 ID
     * @return 남은 사용자 수
     */
    public long countUsersAfter(long lastMigratedUserId) {
        String sql = "SELECT COUNT(*) FROM USERS WHERE USER_ID > ?";
        Long count = legacyJdbcTemplate.queryForObject(sql, Long.class, lastMigratedUserId);
        return count != null ? count : 0L;
    }

    /**
     * PK 기반 사용자 목록 조회 (마이그레이션용)
     *
     * <p>지정된 ID보다 큰 사용자를 PK 순서대로 조회합니다. 체크포인트 기반 마이그레이션에 사용됩니다.
     *
     * @param afterId 이 ID보다 큰 사용자만 조회
     * @param limit 조회 개수
     * @return 레거시 사용자 목록
     */
    public List<LegacyUserDto> findUsersAfterIdOrdered(long afterId, int limit) {
        String sql =
                """
                SELECT
                    USER_ID,
                    SOCIAL_PK_ID,
                    PHONE_NUMBER,
                    EMAIL,
                    PASSWORD_HASH,
                    NAME,
                    DATE_OF_BIRTH,
                    GENDER,
                    PROVIDER,
                    STATUS,
                    PRIVACY_CONSENT,
                    SERVICE_TERMS_CONSENT,
                    AD_CONSENT,
                    WITHDRAWAL_REASON,
                    WITHDRAWN_AT,
                    CREATED_AT,
                    UPDATED_AT,
                    DELETED_AT
                FROM USERS
                WHERE USER_ID > ?
                ORDER BY USER_ID ASC
                LIMIT ?
                """;

        return legacyJdbcTemplate.query(sql, new LegacyUserRowMapper(), afterId, limit);
    }

    /**
     * 특정 시간 이후 수정된 사용자 목록 조회 (증분 동기화용)
     *
     * @param modifiedAfter 이 시간 이후 수정된 사용자만 조회
     * @param limit 최대 조회 개수
     * @return 수정된 사용자 목록
     */
    public List<LegacyUserDto> findUsersModifiedAfter(LocalDateTime modifiedAfter, int limit) {
        String sql =
                """
                SELECT
                    USER_ID,
                    SOCIAL_PK_ID,
                    PHONE_NUMBER,
                    EMAIL,
                    PASSWORD_HASH,
                    NAME,
                    DATE_OF_BIRTH,
                    GENDER,
                    PROVIDER,
                    STATUS,
                    PRIVACY_CONSENT,
                    SERVICE_TERMS_CONSENT,
                    AD_CONSENT,
                    WITHDRAWAL_REASON,
                    WITHDRAWN_AT,
                    CREATED_AT,
                    UPDATED_AT,
                    DELETED_AT
                FROM USERS
                WHERE UPDATED_AT > ?
                ORDER BY UPDATED_AT
                LIMIT ?
                """;

        return legacyJdbcTemplate.query(sql, new LegacyUserRowMapper(), modifiedAfter, limit);
    }

    private static class LegacyUserRowMapper implements RowMapper<LegacyUserDto> {
        @Override
        public LegacyUserDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new LegacyUserDto(
                    rs.getLong("USER_ID"),
                    rs.getString("SOCIAL_PK_ID"),
                    rs.getString("PHONE_NUMBER"),
                    rs.getString("EMAIL"),
                    rs.getString("PASSWORD_HASH"),
                    rs.getString("NAME"),
                    getLocalDate(rs, "DATE_OF_BIRTH"),
                    rs.getString("GENDER"),
                    rs.getString("PROVIDER"),
                    rs.getString("STATUS"),
                    rs.getBoolean("PRIVACY_CONSENT"),
                    rs.getBoolean("SERVICE_TERMS_CONSENT"),
                    rs.getBoolean("AD_CONSENT"),
                    rs.getString("WITHDRAWAL_REASON"),
                    getLocalDateTime(rs, "WITHDRAWN_AT"),
                    getLocalDateTime(rs, "CREATED_AT"),
                    getLocalDateTime(rs, "UPDATED_AT"),
                    getLocalDateTime(rs, "DELETED_AT"));
        }

        private LocalDate getLocalDate(ResultSet rs, String columnName) throws SQLException {
            java.sql.Date date = rs.getDate(columnName);
            return date != null ? date.toLocalDate() : null;
        }

        private LocalDateTime getLocalDateTime(ResultSet rs, String columnName)
                throws SQLException {
            java.sql.Timestamp timestamp = rs.getTimestamp(columnName);
            return timestamp != null ? timestamp.toLocalDateTime() : null;
        }
    }
}
