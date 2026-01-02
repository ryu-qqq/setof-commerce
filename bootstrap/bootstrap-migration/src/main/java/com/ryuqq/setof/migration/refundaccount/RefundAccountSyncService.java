package com.ryuqq.setof.migration.refundaccount;

import com.ryuqq.setof.migration.core.SyncResult;
import com.ryuqq.setof.migration.core.SyncService;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * RefundAccount 동기화 서비스
 *
 * <p>레거시 REFUND_ACCOUNT 테이블 데이터를 신규 refund_accounts 테이블로 동기화합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class RefundAccountSyncService implements SyncService {

    private static final Logger log = LoggerFactory.getLogger(RefundAccountSyncService.class);
    private static final String DOMAIN_NAME = "refund_account";
    private static final int DEFAULT_BATCH_SIZE = 1000;
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private final LegacyRefundAccountRepository legacyRepository;
    private final RefundAccountMigrationRepository migrationRepository;

    public RefundAccountSyncService(
            LegacyRefundAccountRepository legacyRepository,
            RefundAccountMigrationRepository migrationRepository) {
        this.legacyRepository = legacyRepository;
        this.migrationRepository = migrationRepository;
    }

    @Override
    public String getDomainName() {
        return DOMAIN_NAME;
    }

    @Override
    public SyncResult initialMigration() {
        Instant startedAt = Instant.now();
        log.info("Starting initial migration for {}", DOMAIN_NAME);

        long total = legacyRepository.countAll();
        long syncedCount = 0;
        long skippedCount = 0;
        long failedCount = 0;
        long unmappedMemberCount = 0;
        long unmappedBankCount = 0;
        long offset = 0;

        while (offset < total) {
            List<LegacyRefundAccountDto> accounts =
                    legacyRepository.findRefundAccountsForMigration(offset, DEFAULT_BATCH_SIZE);

            if (accounts.isEmpty()) {
                break;
            }

            for (LegacyRefundAccountDto account : accounts) {
                try {
                    SyncStatus status = syncSingle(account);
                    switch (status) {
                        case SYNCED -> syncedCount++;
                        case SKIPPED -> skippedCount++;
                        case UNMAPPED_MEMBER -> unmappedMemberCount++;
                        case UNMAPPED_BANK -> unmappedBankCount++;
                    }
                } catch (Exception e) {
                    log.error(
                            "Failed to sync refund account. id={}, error={}",
                            account.refundAccountId(),
                            e.getMessage());
                    failedCount++;
                }
            }

            offset += DEFAULT_BATCH_SIZE;
            log.info("Initial migration progress: {}/{}", Math.min(offset, total), total);
        }

        long totalFailed = failedCount + unmappedMemberCount + unmappedBankCount;
        if (totalFailed > 0) {
            String errorMsg =
                    String.format(
                            "failed=%d, unmappedMember=%d, unmappedBank=%d",
                            failedCount, unmappedMemberCount, unmappedBankCount);
            return SyncResult.partial(
                    DOMAIN_NAME, syncedCount, skippedCount, totalFailed, startedAt, errorMsg);
        }
        return SyncResult.success(DOMAIN_NAME, syncedCount, skippedCount, startedAt);
    }

    @Override
    public SyncResult incrementalSync(Instant lastSyncAt) {
        Instant startedAt = Instant.now();
        LocalDateTime lastSyncLocalTime = LocalDateTime.ofInstant(lastSyncAt, KST);

        log.info("Starting incremental sync for {} since {}", DOMAIN_NAME, lastSyncLocalTime);

        List<LegacyRefundAccountDto> changedAccounts =
                legacyRepository.findModifiedAfter(lastSyncLocalTime, DEFAULT_BATCH_SIZE);

        long syncedCount = 0;
        long skippedCount = 0;
        long failedCount = 0;
        long unmappedMemberCount = 0;
        long unmappedBankCount = 0;

        for (LegacyRefundAccountDto account : changedAccounts) {
            try {
                SyncStatus status = syncSingle(account);
                switch (status) {
                    case SYNCED -> syncedCount++;
                    case SKIPPED -> skippedCount++;
                    case UNMAPPED_MEMBER -> unmappedMemberCount++;
                    case UNMAPPED_BANK -> unmappedBankCount++;
                }
            } catch (Exception e) {
                log.error(
                        "Failed to sync refund account. id={}, error={}",
                        account.refundAccountId(),
                        e.getMessage());
                failedCount++;
            }
        }

        log.info(
                "Incremental sync completed. synced={}, skipped={}, unmapped(member/bank)={}/{},"
                        + " failed={}",
                syncedCount,
                skippedCount,
                unmappedMemberCount,
                unmappedBankCount,
                failedCount);

        long totalFailed = failedCount + unmappedMemberCount + unmappedBankCount;
        if (totalFailed > 0) {
            String errorMsg =
                    String.format(
                            "failed=%d, unmappedMember=%d, unmappedBank=%d",
                            failedCount, unmappedMemberCount, unmappedBankCount);
            return SyncResult.partial(
                    DOMAIN_NAME, syncedCount, skippedCount, totalFailed, startedAt, errorMsg);
        }
        return SyncResult.success(DOMAIN_NAME, syncedCount, skippedCount, startedAt);
    }

    /** 단일 환불계좌 동기화 */
    @Transactional
    public SyncStatus syncSingle(LegacyRefundAccountDto legacyAccount) {
        // 레거시 USER_ID → 신규 member_id 조회
        String memberId = migrationRepository.findMemberIdByLegacyUserId(legacyAccount.userId());
        if (memberId == null) {
            log.debug("Member not found for legacy user. legacyUserId={}", legacyAccount.userId());
            return SyncStatus.UNMAPPED_MEMBER;
        }

        // 레거시 BANK_NAME → 신규 bank_id 조회
        Long bankId = migrationRepository.findBankIdByBankName(legacyAccount.bankName());
        if (bankId == null) {
            log.debug("Bank not found for bank name. bankName={}", legacyAccount.bankName());
            return SyncStatus.UNMAPPED_BANK;
        }

        // 이미 존재하는지 확인
        if (migrationRepository.existsByMemberId(memberId)) {
            log.debug("Refund account already exists. memberId={}", memberId);
            return SyncStatus.SKIPPED;
        }

        // 직접 INSERT
        migrationRepository.insertRefundAccountDirectly(memberId, bankId, legacyAccount);

        log.debug(
                "Refund account synced. legacyId={}, memberId={}",
                legacyAccount.refundAccountId(),
                memberId);

        return SyncStatus.SYNCED;
    }

    private enum SyncStatus {
        SYNCED,
        SKIPPED,
        UNMAPPED_MEMBER,
        UNMAPPED_BANK
    }
}
