package com.ryuqq.setof.migration.shippingaddress;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * ShippingAddress 마이그레이션 전용 Repository
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class ShippingAddressMigrationRepository {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private final JdbcTemplate jdbcTemplate;

    public ShippingAddressMigrationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 레거시 USER_ID로 신규 member_id 조회
     *
     * @param legacyUserId 레거시 사용자 ID
     * @return 신규 member_id (없으면 null)
     */
    public String findMemberIdByLegacyUserId(Long legacyUserId) {
        String sql = "SELECT id FROM members WHERE legacy_user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, legacyUserId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * member_id와 address_name으로 이미 존재하는지 확인
     *
     * @param memberId 회원 ID
     * @param addressName 배송지명
     * @return 이미 존재하면 true
     */
    public boolean existsByMemberIdAndAddressName(String memberId, String addressName) {
        String sql =
                "SELECT COUNT(*) FROM shipping_addresses WHERE member_id = ? AND address_name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, memberId, addressName);
        return count != null && count > 0;
    }

    /**
     * 배송지 직접 INSERT
     *
     * @param memberId 신규 member_id
     * @param legacyAddress 레거시 배송지 정보
     */
    public void insertShippingAddressDirectly(
            String memberId, LegacyShippingAddressDto legacyAddress) {
        String sql =
                """
                INSERT INTO shipping_addresses (
                    member_id, address_name, receiver_name, receiver_phone,
                    zip_code, road_address, jibun_address, detail_address,
                    delivery_request, is_default, created_at, updated_at, deleted_at
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        Timestamp now = Timestamp.from(Instant.now());
        boolean isDefault = "Y".equalsIgnoreCase(legacyAddress.defaultYn());

        jdbcTemplate.update(
                sql,
                memberId,
                legacyAddress.shippingAddressName(),
                legacyAddress.receiverName(),
                legacyAddress.phoneNumber(),
                legacyAddress.zipCode(),
                legacyAddress.addressLine1(),
                null, // jibun_address
                legacyAddress.addressLine2(),
                legacyAddress.deliveryRequest(),
                isDefault,
                toTimestamp(legacyAddress.createdAt(), now),
                toTimestamp(legacyAddress.modifiedAt(), now),
                null // deleted_at
                );
    }

    private Timestamp toTimestamp(LocalDateTime localDateTime, Timestamp defaultValue) {
        if (localDateTime == null) {
            return defaultValue;
        }
        return Timestamp.from(localDateTime.atZone(KST).toInstant());
    }
}
