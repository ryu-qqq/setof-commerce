package com.ryuqq.setof.migration.shippingaddress;

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
 * ShippingAddress 동기화 서비스
 *
 * <p>레거시 SHIPPING_ADDRESS 테이블 데이터를 신규 shipping_addresses 테이블로 동기화합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class ShippingAddressSyncService implements SyncService {

    private static final Logger log = LoggerFactory.getLogger(ShippingAddressSyncService.class);
    private static final String DOMAIN_NAME = "shipping_address";
    private static final int DEFAULT_BATCH_SIZE = 1000;
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private final LegacyShippingAddressRepository legacyRepository;
    private final ShippingAddressMigrationRepository migrationRepository;

    public ShippingAddressSyncService(
            LegacyShippingAddressRepository legacyRepository,
            ShippingAddressMigrationRepository migrationRepository) {
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
        long unmappedCount = 0;
        long offset = 0;

        while (offset < total) {
            List<LegacyShippingAddressDto> addresses =
                    legacyRepository.findShippingAddressesForMigration(offset, DEFAULT_BATCH_SIZE);

            if (addresses.isEmpty()) {
                break;
            }

            for (LegacyShippingAddressDto address : addresses) {
                try {
                    SyncStatus status = syncSingle(address);
                    switch (status) {
                        case SYNCED -> syncedCount++;
                        case SKIPPED -> skippedCount++;
                        case UNMAPPED -> unmappedCount++;
                    }
                } catch (Exception e) {
                    log.error(
                            "Failed to sync shipping address. id={}, error={}",
                            address.shippingAddressId(),
                            e.getMessage());
                    failedCount++;
                }
            }

            offset += DEFAULT_BATCH_SIZE;
            log.info("Initial migration progress: {}/{}", Math.min(offset, total), total);
        }

        long totalFailed = failedCount + unmappedCount;
        if (totalFailed > 0) {
            String errorMsg = String.format("failed=%d, unmapped=%d", failedCount, unmappedCount);
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

        List<LegacyShippingAddressDto> changedAddresses =
                legacyRepository.findModifiedAfter(lastSyncLocalTime, DEFAULT_BATCH_SIZE);

        long syncedCount = 0;
        long skippedCount = 0;
        long failedCount = 0;
        long unmappedCount = 0;

        for (LegacyShippingAddressDto address : changedAddresses) {
            try {
                SyncStatus status = syncSingle(address);
                switch (status) {
                    case SYNCED -> syncedCount++;
                    case SKIPPED -> skippedCount++;
                    case UNMAPPED -> unmappedCount++;
                }
            } catch (Exception e) {
                log.error(
                        "Failed to sync shipping address. id={}, error={}",
                        address.shippingAddressId(),
                        e.getMessage());
                failedCount++;
            }
        }

        log.info(
                "Incremental sync completed. synced={}, skipped={}, unmapped={}, failed={}",
                syncedCount,
                skippedCount,
                unmappedCount,
                failedCount);

        long totalFailed = failedCount + unmappedCount;
        if (totalFailed > 0) {
            String errorMsg = String.format("failed=%d, unmapped=%d", failedCount, unmappedCount);
            return SyncResult.partial(
                    DOMAIN_NAME, syncedCount, skippedCount, totalFailed, startedAt, errorMsg);
        }
        return SyncResult.success(DOMAIN_NAME, syncedCount, skippedCount, startedAt);
    }

    /** 단일 배송지 동기화 */
    @Transactional
    public SyncStatus syncSingle(LegacyShippingAddressDto legacyAddress) {
        // 레거시 USER_ID → 신규 member_id 조회
        String memberId = migrationRepository.findMemberIdByLegacyUserId(legacyAddress.userId());
        if (memberId == null) {
            log.debug("Member not found for legacy user. legacyUserId={}", legacyAddress.userId());
            return SyncStatus.UNMAPPED;
        }

        // 이미 존재하는지 확인
        if (migrationRepository.existsByMemberIdAndAddressName(
                memberId, legacyAddress.shippingAddressName())) {
            log.debug("Shipping address already exists. memberId={}", memberId);
            return SyncStatus.SKIPPED;
        }

        // 직접 INSERT
        migrationRepository.insertShippingAddressDirectly(memberId, legacyAddress);

        log.debug(
                "Shipping address synced. legacyId={}, memberId={}",
                legacyAddress.shippingAddressId(),
                memberId);

        return SyncStatus.SYNCED;
    }

    private enum SyncStatus {
        SYNCED,
        SKIPPED,
        UNMAPPED
    }
}
