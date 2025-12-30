package com.ryuqq.setof.migration.refundaccount;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * RefundAccount 마이그레이션 전용 Repository
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class RefundAccountMigrationRepository {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private final JdbcTemplate jdbcTemplate;

    public RefundAccountMigrationRepository(JdbcTemplate jdbcTemplate) {
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
     * 은행명으로 bank_id 조회
     *
     * @param bankName 은행명
     * @return bank_id (없으면 null)
     */
    public Long findBankIdByBankName(String bankName) {
        String sql = "SELECT id FROM banks WHERE bank_name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Long.class, bankName);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * member_id로 이미 존재하는지 확인
     *
     * @param memberId 회원 ID
     * @return 이미 존재하면 true
     */
    public boolean existsByMemberId(String memberId) {
        String sql = "SELECT COUNT(*) FROM refund_accounts WHERE member_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, memberId);
        return count != null && count > 0;
    }

    /**
     * 환불계좌 직접 INSERT
     *
     * @param memberId 신규 member_id
     * @param bankId 은행 ID
     * @param legacyAccount 레거시 환불계좌 정보
     */
    public void insertRefundAccountDirectly(
            String memberId, Long bankId, LegacyRefundAccountDto legacyAccount) {
        String sql =
                """
                INSERT INTO refund_accounts (
                    member_id, bank_id, account_number, account_holder_name,
                    is_verified, verified_at, created_at, updated_at, deleted_at
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        Timestamp now = Timestamp.from(Instant.now());

        jdbcTemplate.update(
                sql,
                memberId,
                bankId,
                legacyAccount.accountNumber(),
                legacyAccount.accountHolderName(),
                false, // is_verified - 기본값 false
                null, // verified_at - 기본값 null
                toTimestamp(legacyAccount.createdAt(), now),
                toTimestamp(legacyAccount.modifiedAt(), now),
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
