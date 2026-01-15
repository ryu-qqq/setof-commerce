package com.ryuqq.setof.migration.member;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Member 마이그레이션 전용 Repository
 *
 * <p>마이그레이션 시 legacy_user_id를 포함하여 저장하기 위한 전용 Repository입니다. setofDataSource를 사용하여 신규 DB에 데이터를
 * 저장합니다.
 *
 * <p><strong>Domain VO 검증 우회:</strong> 레거시 데이터가 현재 도메인 규칙을 위반할 수 있으므로, Domain 객체를 거치지 않고 직접
 * INSERT합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Repository
public class MemberMigrationRepository {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private final JdbcTemplate setofJdbcTemplate;

    public MemberMigrationRepository(
            @Qualifier("setofJdbcTemplate") JdbcTemplate setofJdbcTemplate) {
        this.setofJdbcTemplate = setofJdbcTemplate;
    }

    /**
     * legacy_user_id로 이미 마이그레이션된 사용자인지 확인
     *
     * @param legacyUserId 레거시 사용자 ID
     * @return 이미 존재하면 true
     */
    public boolean existsByLegacyUserId(Long legacyUserId) {
        String sql = "SELECT COUNT(*) FROM members WHERE legacy_user_id = ?";
        Integer count = setofJdbcTemplate.queryForObject(sql, Integer.class, legacyUserId);
        return count != null && count > 0;
    }

    /**
     * 변환된 마이그레이션 데이터를 직접 INSERT
     *
     * @param data 마이그레이션 데이터
     */
    public void insertMemberDirectly(MemberMigrationData data) {
        String sql =
                """
                INSERT INTO members (
                    id, phone_number, email, password_hash, name, date_of_birth,
                    gender, provider, social_id, status,
                    privacy_consent, service_terms_consent, ad_consent,
                    withdrawal_reason, withdrawn_at,
                    created_at, updated_at, deleted_at, legacy_user_id
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        Timestamp now = Timestamp.from(java.time.Instant.now());

        setofJdbcTemplate.update(
                sql,
                data.memberId().toString(),
                data.phoneNumber(),
                data.email(),
                data.passwordHash(),
                data.name(),
                data.dateOfBirth(),
                data.gender(),
                data.provider(),
                data.socialPkId(),
                data.status(),
                data.privacyConsent(),
                data.serviceTermsConsent(),
                data.adConsent(),
                data.withdrawalReason(),
                toTimestamp(data.withdrawnAt()),
                toTimestamp(data.createdAt(), now),
                toTimestamp(data.updatedAt(), now),
                toTimestamp(data.deletedAt()),
                data.legacyUserId());
    }

    /**
     * 레거시 사용자를 직접 INSERT (Domain VO 검증 우회)
     *
     * @param newMemberId 새 UUID v7 ID
     * @param legacyUser 레거시 사용자 정보
     * @deprecated Use {@link #insertMemberDirectly(MemberMigrationData)} instead
     */
    @Deprecated
    public void insertMemberDirectly(UUID newMemberId, LegacyUserDto legacyUser) {
        String sql =
                """
                INSERT INTO members (
                    id, phone_number, email, password_hash, name, date_of_birth,
                    gender, provider, social_id, status,
                    privacy_consent, service_terms_consent, ad_consent,
                    withdrawal_reason, withdrawn_at,
                    created_at, updated_at, deleted_at, legacy_user_id
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        Timestamp now = Timestamp.from(java.time.Instant.now());

        setofJdbcTemplate.update(
                sql,
                newMemberId.toString(),
                legacyUser.phoneNumber(),
                legacyUser.email(),
                legacyUser.passwordHash(),
                legacyUser.name(),
                legacyUser.dateOfBirth(),
                legacyUser.gender(),
                legacyUser.provider() != null ? legacyUser.provider() : "LOCAL",
                legacyUser.socialPkId(),
                legacyUser.status() != null ? legacyUser.status() : "ACTIVE",
                legacyUser.privacyConsent(),
                legacyUser.serviceTermsConsent(),
                legacyUser.adConsent(),
                legacyUser.withdrawalReason(),
                toTimestamp(legacyUser.withdrawnAt()),
                toTimestamp(legacyUser.createdAt(), now),
                toTimestamp(legacyUser.updatedAt(), now),
                toTimestamp(legacyUser.deletedAt()),
                legacyUser.userId());
    }

    /**
     * 마지막으로 마이그레이션된 legacy_user_id 조회
     *
     * @return 마지막 legacy_user_id (없으면 0)
     */
    public long findLastMigratedLegacyUserId() {
        String sql =
                "SELECT COALESCE(MAX(legacy_user_id), 0) FROM members WHERE legacy_user_id IS NOT"
                        + " NULL";
        Long result = setofJdbcTemplate.queryForObject(sql, Long.class);
        return result != null ? result : 0L;
    }

    /**
     * 증분 동기화용 UPSERT (INSERT 또는 UPDATE)
     *
     * <p>legacy_user_id로 기존 레코드가 있으면 UPDATE, 없으면 INSERT합니다.
     *
     * @param data 마이그레이션 데이터
     * @return true면 INSERT, false면 UPDATE
     */
    public boolean upsertMember(MemberMigrationData data) {
        if (existsByLegacyUserId(data.legacyUserId())) {
            updateMemberByLegacyUserId(data);
            return false;
        } else {
            insertMemberDirectly(data);
            return true;
        }
    }

    /**
     * legacy_user_id로 기존 회원 정보 업데이트 (증분 동기화용)
     *
     * @param data 마이그레이션 데이터
     */
    public void updateMemberByLegacyUserId(MemberMigrationData data) {
        String sql =
                """
                UPDATE members SET
                    phone_number = ?,
                    email = ?,
                    password_hash = ?,
                    name = ?,
                    date_of_birth = ?,
                    gender = ?,
                    provider = ?,
                    social_id = ?,
                    status = ?,
                    privacy_consent = ?,
                    service_terms_consent = ?,
                    ad_consent = ?,
                    withdrawal_reason = ?,
                    withdrawn_at = ?,
                    updated_at = ?,
                    deleted_at = ?
                WHERE legacy_user_id = ?
                """;

        Timestamp now = Timestamp.from(java.time.Instant.now());

        setofJdbcTemplate.update(
                sql,
                data.phoneNumber(),
                data.email(),
                data.passwordHash(),
                data.name(),
                data.dateOfBirth(),
                data.gender(),
                data.provider(),
                data.socialPkId(),
                data.status(),
                data.privacyConsent(),
                data.serviceTermsConsent(),
                data.adConsent(),
                data.withdrawalReason(),
                toTimestamp(data.withdrawnAt()),
                toTimestamp(data.updatedAt(), now),
                toTimestamp(data.deletedAt()),
                data.legacyUserId());
    }

    /**
     * 마이그레이션된 회원 수 조회
     *
     * @return 마이그레이션된 회원 수
     */
    public long countMigratedMembers() {
        String sql = "SELECT COUNT(*) FROM members WHERE legacy_user_id IS NOT NULL";
        Long count = setofJdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0L;
    }

    private Timestamp toTimestamp(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Timestamp.from(localDateTime.atZone(KST).toInstant());
    }

    private Timestamp toTimestamp(LocalDateTime localDateTime, Timestamp defaultValue) {
        if (localDateTime == null) {
            return defaultValue;
        }
        return Timestamp.from(localDateTime.atZone(KST).toInstant());
    }
}
