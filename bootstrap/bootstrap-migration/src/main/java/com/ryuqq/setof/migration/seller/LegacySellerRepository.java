package com.ryuqq.setof.migration.seller;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * 레거시 Seller 테이블 조회 Repository
 *
 * <p>레거시 DB의 seller, seller_business_info, seller_shipping_info 테이블에서 데이터를 조회합니다. 조회 전용이며 쓰기 작업은
 * 수행하지 않습니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class LegacySellerRepository {

    private final JdbcTemplate legacyJdbcTemplate;

    public LegacySellerRepository(
            @Qualifier("legacyJdbcTemplate") JdbcTemplate legacyJdbcTemplate) {
        this.legacyJdbcTemplate = legacyJdbcTemplate;
    }

    /**
     * 전체 셀러 수 조회
     *
     * @return 전체 셀러 수
     */
    public long countAllSellers() {
        String sql = "SELECT COUNT(*) FROM seller";
        Long count = legacyJdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0L;
    }

    /**
     * 특정 셀러 ID 이후의 셀러 수 조회 (마이그레이션 진행률 확인용)
     *
     * @param lastMigratedSellerId 마지막으로 마이그레이션된 셀러 ID
     * @return 남은 셀러 수
     */
    public long countSellersAfter(long lastMigratedSellerId) {
        String sql = "SELECT COUNT(*) FROM seller WHERE SELLER_ID > ?";
        Long count = legacyJdbcTemplate.queryForObject(sql, Long.class, lastMigratedSellerId);
        return count != null ? count : 0L;
    }

    /**
     * PK 기반 셀러 목록 조회 (마이그레이션용)
     *
     * <p>지정된 ID보다 큰 셀러를 PK 순서대로 조회합니다. 3개 테이블을 LEFT JOIN하여 조회합니다.
     *
     * @param afterId 이 ID보다 큰 셀러만 조회
     * @param limit 조회 개수
     * @return 레거시 셀러 목록
     */
    public List<LegacySellerDto> findSellersAfterIdOrdered(long afterId, int limit) {
        String sql =
                """
                SELECT
                    s.SELLER_ID,
                    s.SELLER_NAME,
                    s.SELLER_LOGO_URL,
                    s.SELLER_DESCRIPTION,
                    s.COMMISSION_RATE,
                    s.APPROVAL_STATUS,
                    s.DELETE_YN,
                    s.INSERT_DATE,
                    s.UPDATE_DATE,
                    bi.REGISTRATION_NUMBER,
                    bi.SALE_REPORT_NUMBER,
                    bi.REPRESENTATIVE,
                    bi.COMPANY_NAME,
                    bi.BUSINESS_ADDRESS_ZIP_CODE,
                    bi.BUSINESS_ADDRESS_LINE1,
                    bi.BUSINESS_ADDRESS_LINE2,
                    bi.BANK_NAME,
                    bi.ACCOUNT_NUMBER,
                    bi.ACCOUNT_HOLDER_NAME,
                    bi.CS_PHONE_NUMBER,
                    bi.CS_EMAIL,
                    si.RETURN_ADDRESS_ZIP_CODE,
                    si.RETURN_ADDRESS_LINE1,
                    si.RETURN_ADDRESS_LINE2
                FROM seller s
                LEFT JOIN seller_business_info bi ON s.SELLER_ID = bi.SELLER_ID
                LEFT JOIN seller_shipping_info si ON s.SELLER_ID = si.SELLER_ID
                WHERE s.SELLER_ID > ?
                ORDER BY s.SELLER_ID ASC
                LIMIT ?
                """;

        return legacyJdbcTemplate.query(sql, new LegacySellerRowMapper(), afterId, limit);
    }

    private static class LegacySellerRowMapper implements RowMapper<LegacySellerDto> {
        @Override
        public LegacySellerDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new LegacySellerDto(
                    rs.getLong("SELLER_ID"),
                    rs.getString("SELLER_NAME"),
                    rs.getString("SELLER_LOGO_URL"),
                    rs.getString("SELLER_DESCRIPTION"),
                    getBigDecimal(rs, "COMMISSION_RATE"),
                    rs.getString("APPROVAL_STATUS"),
                    rs.getString("DELETE_YN"),
                    getLocalDateTime(rs, "INSERT_DATE"),
                    getLocalDateTime(rs, "UPDATE_DATE"),
                    rs.getString("REGISTRATION_NUMBER"),
                    rs.getString("SALE_REPORT_NUMBER"),
                    rs.getString("REPRESENTATIVE"),
                    rs.getString("COMPANY_NAME"),
                    rs.getString("BUSINESS_ADDRESS_ZIP_CODE"),
                    rs.getString("BUSINESS_ADDRESS_LINE1"),
                    rs.getString("BUSINESS_ADDRESS_LINE2"),
                    rs.getString("BANK_NAME"),
                    rs.getString("ACCOUNT_NUMBER"),
                    rs.getString("ACCOUNT_HOLDER_NAME"),
                    rs.getString("CS_PHONE_NUMBER"),
                    rs.getString("CS_EMAIL"),
                    rs.getString("RETURN_ADDRESS_ZIP_CODE"),
                    rs.getString("RETURN_ADDRESS_LINE1"),
                    rs.getString("RETURN_ADDRESS_LINE2"));
        }

        private BigDecimal getBigDecimal(ResultSet rs, String columnName) throws SQLException {
            BigDecimal value = rs.getBigDecimal(columnName);
            return value != null ? value : BigDecimal.ZERO;
        }

        private LocalDateTime getLocalDateTime(ResultSet rs, String columnName)
                throws SQLException {
            java.sql.Timestamp timestamp = rs.getTimestamp(columnName);
            return timestamp != null ? timestamp.toLocalDateTime() : null;
        }
    }
}
