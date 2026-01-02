package com.ryuqq.setof.migration.refundaccount;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * RefundAccount 마이그레이션 서비스
 *
 * <p>레거시 REFUND_ACCOUNT 테이블 데이터를 신규 refund_accounts 테이블로 마이그레이션합니다.
 *
 * <p><strong>마이그레이션 전략:</strong>
 *
 * <ul>
 *   <li>배치 단위로 처리 (기본 1000건)
 *   <li>레거시 USER_ID → 신규 member_id 매핑 (legacy_user_id 사용)
 *   <li>레거시 BANK_NAME → 신규 bank_id 매핑 (banks 테이블 조회)
 *   <li>이미 마이그레이션된 데이터는 스킵
 *   <li>매핑되지 않는 USER_ID 또는 BANK_NAME은 스킵
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class RefundAccountMigrationService {

    private static final Logger log = LoggerFactory.getLogger(RefundAccountMigrationService.class);
    private static final int DEFAULT_BATCH_SIZE = 1000;

    private final LegacyRefundAccountRepository legacyRepository;
    private final RefundAccountMigrationRepository migrationRepository;

    public RefundAccountMigrationService(
            LegacyRefundAccountRepository legacyRepository,
            RefundAccountMigrationRepository migrationRepository) {
        this.legacyRepository = legacyRepository;
        this.migrationRepository = migrationRepository;
    }

    /**
     * 전체 마이그레이션 실행
     *
     * @return 마이그레이션 결과
     */
    public MigrationResult migrateAll() {
        return migrateAll(DEFAULT_BATCH_SIZE);
    }

    /**
     * 전체 마이그레이션 실행 (배치 크기 지정)
     *
     * @param batchSize 배치 크기
     * @return 마이그레이션 결과
     */
    public MigrationResult migrateAll(int batchSize) {
        long total = legacyRepository.countAll();
        log.info("Starting refund account migration. Total: {}", total);

        long migratedCount = 0;
        long skippedCount = 0;
        long failedCount = 0;
        long unmappedMemberCount = 0;
        long unmappedBankCount = 0;
        long offset = 0;

        while (offset < total) {
            List<LegacyRefundAccountDto> accounts =
                    legacyRepository.findRefundAccountsForMigration(offset, batchSize);

            if (accounts.isEmpty()) {
                break;
            }

            for (LegacyRefundAccountDto account : accounts) {
                try {
                    MigrationStatus status = migrateSingle(account);
                    switch (status) {
                        case MIGRATED -> migratedCount++;
                        case SKIPPED -> skippedCount++;
                        case UNMAPPED_MEMBER -> unmappedMemberCount++;
                        case UNMAPPED_BANK -> unmappedBankCount++;
                    }
                } catch (Exception e) {
                    log.error(
                            "Failed to migrate refund account. id={}, error={}",
                            account.refundAccountId(),
                            e.getMessage());
                    failedCount++;
                }
            }

            offset += batchSize;
            log.info(
                    "Migration progress: {}/{} (migrated={}, skipped={}, unmappedMember={},"
                            + " unmappedBank={}, failed={})",
                    Math.min(offset, total),
                    total,
                    migratedCount,
                    skippedCount,
                    unmappedMemberCount,
                    unmappedBankCount,
                    failedCount);
        }

        log.info(
                "Refund account migration completed. migrated={}, skipped={}, unmappedMember={},"
                        + " unmappedBank={}, failed={}",
                migratedCount,
                skippedCount,
                unmappedMemberCount,
                unmappedBankCount,
                failedCount);

        return new MigrationResult(
                total,
                migratedCount,
                skippedCount,
                unmappedMemberCount,
                unmappedBankCount,
                failedCount);
    }

    /**
     * 단일 환불계좌 마이그레이션
     *
     * @param legacyAccount 레거시 환불계좌 정보
     * @return 마이그레이션 상태
     */
    @Transactional
    public MigrationStatus migrateSingle(LegacyRefundAccountDto legacyAccount) {
        // 레거시 USER_ID → 신규 member_id 조회
        String memberId = migrationRepository.findMemberIdByLegacyUserId(legacyAccount.userId());
        if (memberId == null) {
            log.debug("Member not found for legacy user. legacyUserId={}", legacyAccount.userId());
            return MigrationStatus.UNMAPPED_MEMBER;
        }

        // 레거시 BANK_NAME → 신규 bank_id 조회
        Long bankId = migrationRepository.findBankIdByBankName(legacyAccount.bankName());
        if (bankId == null) {
            log.debug("Bank not found for bank name. bankName={}", legacyAccount.bankName());
            return MigrationStatus.UNMAPPED_BANK;
        }

        // 이미 존재하는지 확인 (member_id)
        if (migrationRepository.existsByMemberId(memberId)) {
            log.debug("Refund account already exists. memberId={}", memberId);
            return MigrationStatus.SKIPPED;
        }

        // 직접 INSERT
        migrationRepository.insertRefundAccountDirectly(memberId, bankId, legacyAccount);

        log.debug(
                "Refund account migrated. legacyId={}, memberId={}",
                legacyAccount.refundAccountId(),
                memberId);

        return MigrationStatus.MIGRATED;
    }

    /** 마이그레이션 상태 */
    public enum MigrationStatus {
        MIGRATED,
        SKIPPED,
        UNMAPPED_MEMBER,
        UNMAPPED_BANK
    }

    /** 마이그레이션 결과 */
    public record MigrationResult(
            long total,
            long migrated,
            long skipped,
            long unmappedMember,
            long unmappedBank,
            long failed) {

        public boolean isSuccessful() {
            return failed == 0;
        }

        public double successRate() {
            if (total == 0) {
                return 100.0;
            }
            return (migrated + skipped) * 100.0 / total;
        }
    }
}
