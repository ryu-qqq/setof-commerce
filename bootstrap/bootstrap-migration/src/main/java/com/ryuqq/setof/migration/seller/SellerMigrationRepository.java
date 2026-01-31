package com.ryuqq.setof.migration.seller;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Seller 마이그레이션 Repository
 *
 * <p>신규 DB의 seller_applications 테이블에 데이터를 저장합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class SellerMigrationRepository {

    private final JdbcTemplate setofJdbcTemplate;

    public SellerMigrationRepository(
            @Qualifier("setofJdbcTemplate") JdbcTemplate setofJdbcTemplate) {
        this.setofJdbcTemplate = setofJdbcTemplate;
    }

    /**
     * SellerApplication 배치 저장
     *
     * @param dataList 마이그레이션 데이터 목록
     */
    public void batchInsertSellerApplications(List<SellerApplicationMigrationData> dataList) {
        String sql =
                """
INSERT INTO seller_applications (
    seller_name,
    display_name,
    logo_url,
    description,
    registration_number,
    company_name,
    representative,
    sale_report_number,
    business_zip_code,
    business_base_address,
    business_detail_address,
    cs_phone_number,
    cs_email,
    address_type,
    address_name,
    address_zip_code,
    address_base_address,
    address_detail_address,
    contact_name,
    contact_phone_number,
    agreed_at,
    status,
    applied_at,
    processed_at,
    processed_by,
    created_at,
    updated_at
) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
ON DUPLICATE KEY UPDATE
    updated_at = VALUES(updated_at)
""";

        setofJdbcTemplate.batchUpdate(
                sql,
                dataList,
                dataList.size(),
                (PreparedStatement ps, SellerApplicationMigrationData data) -> {
                    ps.setString(1, data.sellerName());
                    ps.setString(2, data.displayName());
                    ps.setString(3, data.logoUrl());
                    ps.setString(4, data.description());
                    ps.setString(5, data.registrationNumber());
                    ps.setString(6, data.companyName());
                    ps.setString(7, data.representative());
                    ps.setString(8, data.saleReportNumber());
                    ps.setString(9, data.businessZipCode());
                    ps.setString(10, data.businessBaseAddress());
                    ps.setString(11, data.businessDetailAddress());
                    ps.setString(12, data.csPhoneNumber());
                    ps.setString(13, data.csEmail());
                    ps.setString(14, data.addressType());
                    ps.setString(15, data.addressName());
                    ps.setString(16, data.addressZipCode());
                    ps.setString(17, data.addressBaseAddress());
                    ps.setString(18, data.addressDetailAddress());
                    ps.setString(19, data.contactName());
                    ps.setString(20, data.contactPhoneNumber());
                    ps.setTimestamp(21, Timestamp.from(data.agreedAt()));
                    ps.setString(22, data.status());
                    ps.setTimestamp(23, Timestamp.from(data.appliedAt()));
                    ps.setTimestamp(24, Timestamp.from(data.processedAt()));
                    ps.setString(25, data.processedBy());
                    ps.setTimestamp(26, Timestamp.from(data.createdAt()));
                    ps.setTimestamp(27, Timestamp.from(data.updatedAt()));
                });
    }

    /**
     * 사업자등록번호로 기존 신청 조회 (중복 체크용)
     *
     * @param registrationNumber 사업자등록번호
     * @return 존재 여부
     */
    public boolean existsByRegistrationNumber(String registrationNumber) {
        String sql = "SELECT COUNT(*) FROM seller_applications WHERE registration_number = ?";
        Long count = setofJdbcTemplate.queryForObject(sql, Long.class, registrationNumber);
        return count != null && count > 0;
    }
}
