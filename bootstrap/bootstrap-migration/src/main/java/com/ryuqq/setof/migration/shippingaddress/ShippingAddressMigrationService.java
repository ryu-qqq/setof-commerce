package com.ryuqq.setof.migration.shippingaddress;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ShippingAddress 마이그레이션 서비스
 *
 * <p>레거시 SHIPPING_ADDRESS 테이블 데이터를 신규 shipping_addresses 테이블로 마이그레이션합니다.
 *
 * <p><strong>마이그레이션 전략:</strong>
 *
 * <ul>
 *   <li>배치 단위로 처리 (기본 1000건)
 *   <li>레거시 USER_ID → 신규 member_id 매핑 (legacy_user_id 사용)
 *   <li>이미 마이그레이션된 데이터는 스킵
 *   <li>매핑되지 않는 USER_ID는 스킵 (Member 먼저 마이그레이션 필요)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class ShippingAddressMigrationService {

    private static final Logger log =
            LoggerFactory.getLogger(ShippingAddressMigrationService.class);
    private static final int DEFAULT_BATCH_SIZE = 1000;

    private final LegacyShippingAddressRepository legacyRepository;
    private final ShippingAddressMigrationRepository migrationRepository;

    public ShippingAddressMigrationService(
            LegacyShippingAddressRepository legacyRepository,
            ShippingAddressMigrationRepository migrationRepository) {
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
        log.info("Starting shipping address migration. Total: {}", total);

        long migratedCount = 0;
        long skippedCount = 0;
        long failedCount = 0;
        long unmappedCount = 0;
        long offset = 0;

        while (offset < total) {
            List<LegacyShippingAddressDto> addresses =
                    legacyRepository.findShippingAddressesForMigration(offset, batchSize);

            if (addresses.isEmpty()) {
                break;
            }

            for (LegacyShippingAddressDto address : addresses) {
                try {
                    MigrationStatus status = migrateSingle(address);
                    switch (status) {
                        case MIGRATED -> migratedCount++;
                        case SKIPPED -> skippedCount++;
                        case UNMAPPED -> unmappedCount++;
                    }
                } catch (Exception e) {
                    log.error(
                            "Failed to migrate shipping address. id={}, error={}",
                            address.shippingAddressId(),
                            e.getMessage());
                    failedCount++;
                }
            }

            offset += batchSize;
            log.info(
                    "Migration progress: {}/{} (migrated={}, skipped={}, unmapped={}, failed={})",
                    Math.min(offset, total),
                    total,
                    migratedCount,
                    skippedCount,
                    unmappedCount,
                    failedCount);
        }

        log.info(
                "Shipping address migration completed. migrated={}, skipped={}, unmapped={},"
                        + " failed={}",
                migratedCount,
                skippedCount,
                unmappedCount,
                failedCount);

        return new MigrationResult(total, migratedCount, skippedCount, unmappedCount, failedCount);
    }

    /**
     * 단일 배송지 마이그레이션
     *
     * @param legacyAddress 레거시 배송지 정보
     * @return 마이그레이션 상태
     */
    @Transactional
    public MigrationStatus migrateSingle(LegacyShippingAddressDto legacyAddress) {
        // 레거시 USER_ID → 신규 member_id 조회
        String memberId = migrationRepository.findMemberIdByLegacyUserId(legacyAddress.userId());
        if (memberId == null) {
            log.debug("Member not found for legacy user. legacyUserId={}", legacyAddress.userId());
            return MigrationStatus.UNMAPPED;
        }

        // 이미 존재하는지 확인 (member_id + address_name)
        if (migrationRepository.existsByMemberIdAndAddressName(
                memberId, legacyAddress.shippingAddressName())) {
            log.debug(
                    "Shipping address already exists. memberId={}, addressName={}",
                    memberId,
                    legacyAddress.shippingAddressName());
            return MigrationStatus.SKIPPED;
        }

        // 직접 INSERT
        migrationRepository.insertShippingAddressDirectly(memberId, legacyAddress);

        log.debug(
                "Shipping address migrated. legacyId={}, memberId={}",
                legacyAddress.shippingAddressId(),
                memberId);

        return MigrationStatus.MIGRATED;
    }

    /** 마이그레이션 상태 */
    public enum MigrationStatus {
        MIGRATED,
        SKIPPED,
        UNMAPPED
    }

    /** 마이그레이션 결과 */
    public record MigrationResult(
            long total, long migrated, long skipped, long unmapped, long failed) {

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
