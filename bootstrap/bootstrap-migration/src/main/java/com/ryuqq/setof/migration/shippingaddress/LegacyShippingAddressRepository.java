package com.ryuqq.setof.migration.shippingaddress;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * 레거시 SHIPPING_ADDRESS 테이블 조회 Repository
 *
 * <p>레거시 DB의 SHIPPING_ADDRESS 테이블에서 데이터를 조회합니다. 조회 전용이며 쓰기 작업은 수행하지 않습니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class LegacyShippingAddressRepository {

    private final JdbcTemplate legacyJdbcTemplate;

    public LegacyShippingAddressRepository(
            @Qualifier("legacyJdbcTemplate") JdbcTemplate legacyJdbcTemplate) {
        this.legacyJdbcTemplate = legacyJdbcTemplate;
    }

    /**
     * 마이그레이션 대상 배송지 목록 조회
     *
     * @param offset 시작 오프셋
     * @param limit 조회 개수
     * @return 레거시 배송지 목록
     */
    public List<LegacyShippingAddressDto> findShippingAddressesForMigration(
            long offset, int limit) {
        String sql =
                """
                SELECT
                    SHIPPING_ADDRESS_ID,
                    USER_ID,
                    RECEIVER_NAME,
                    SHIPPING_ADDRESS_NAME,
                    ADDRESS_LINE1,
                    ADDRESS_LINE2,
                    ZIP_CODE,
                    COUNTRY,
                    DELIVERY_REQUEST,
                    PHONE_NUMBER,
                    DEFAULT_YN,
                    CREATE_AT,
                    MODIFIED_AT
                FROM SHIPPING_ADDRESS
                ORDER BY SHIPPING_ADDRESS_ID
                LIMIT ? OFFSET ?
                """;

        return legacyJdbcTemplate.query(sql, new LegacyShippingAddressRowMapper(), limit, offset);
    }

    /**
     * 전체 배송지 수 조회
     *
     * @return 전체 배송지 수
     */
    public long countAll() {
        String sql = "SELECT COUNT(*) FROM SHIPPING_ADDRESS";
        Long count = legacyJdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0L;
    }

    /**
     * 특정 시간 이후 수정된 배송지 목록 조회 (증분 동기화용)
     *
     * @param modifiedAfter 이 시간 이후 수정된 배송지만 조회
     * @param limit 최대 조회 개수
     * @return 수정된 배송지 목록
     */
    public List<LegacyShippingAddressDto> findModifiedAfter(
            LocalDateTime modifiedAfter, int limit) {
        String sql =
                """
                SELECT
                    SHIPPING_ADDRESS_ID,
                    USER_ID,
                    RECEIVER_NAME,
                    SHIPPING_ADDRESS_NAME,
                    ADDRESS_LINE1,
                    ADDRESS_LINE2,
                    ZIP_CODE,
                    COUNTRY,
                    DELIVERY_REQUEST,
                    PHONE_NUMBER,
                    DEFAULT_YN,
                    CREATE_AT,
                    MODIFIED_AT
                FROM SHIPPING_ADDRESS
                WHERE MODIFIED_AT > ?
                ORDER BY MODIFIED_AT
                LIMIT ?
                """;

        return legacyJdbcTemplate.query(
                sql, new LegacyShippingAddressRowMapper(), modifiedAfter, limit);
    }

    private static class LegacyShippingAddressRowMapper
            implements RowMapper<LegacyShippingAddressDto> {
        @Override
        public LegacyShippingAddressDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new LegacyShippingAddressDto(
                    rs.getLong("SHIPPING_ADDRESS_ID"),
                    rs.getLong("USER_ID"),
                    rs.getString("RECEIVER_NAME"),
                    rs.getString("SHIPPING_ADDRESS_NAME"),
                    rs.getString("ADDRESS_LINE1"),
                    rs.getString("ADDRESS_LINE2"),
                    rs.getString("ZIP_CODE"),
                    rs.getString("COUNTRY"),
                    rs.getString("DELIVERY_REQUEST"),
                    rs.getString("PHONE_NUMBER"),
                    rs.getString("DEFAULT_YN"),
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
