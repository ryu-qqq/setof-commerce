package com.ryuqq.setof.migration.product;

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
 * Product 동기화 서비스
 *
 * <p>레거시 PRODUCT_GROUP, PRODUCT, PRODUCT_STOCK 데이터를 신규 product_groups, products, product_stocks
 * 테이블로 동기화합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class ProductSyncService implements SyncService {

    private static final Logger log = LoggerFactory.getLogger(ProductSyncService.class);
    private static final String DOMAIN_NAME = "product";
    private static final int DEFAULT_BATCH_SIZE = 500;
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private final LegacyProductRepository legacyRepository;
    private final ProductMigrationRepository migrationRepository;

    public ProductSyncService(
            LegacyProductRepository legacyRepository,
            ProductMigrationRepository migrationRepository) {
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

        long total = legacyRepository.countAllProductGroups();
        long syncedCount = 0;
        long skippedCount = 0;
        long failedCount = 0;
        long lastProductGroupId = 0;
        long processedCount = 0;

        while (true) {
            List<LegacyProductGroupDto> productGroups =
                    legacyRepository.findProductGroupsForMigration(
                            lastProductGroupId, DEFAULT_BATCH_SIZE);

            if (productGroups.isEmpty()) {
                break;
            }

            for (LegacyProductGroupDto productGroup : productGroups) {
                try {
                    SyncStatus status = syncSingleProductGroup(productGroup);
                    switch (status) {
                        case SYNCED -> syncedCount++;
                        case SKIPPED -> skippedCount++;
                        case UPDATED -> syncedCount++;
                    }
                } catch (Exception e) {
                    log.error(
                            "Failed to sync product group. id={}, error={}",
                            productGroup.productGroupId(),
                            e.getMessage(),
                            e);
                    failedCount++;
                }
                lastProductGroupId = productGroup.productGroupId();
            }

            processedCount += productGroups.size();
            log.info("Initial migration progress: {}/{}", processedCount, total);
        }

        if (failedCount > 0) {
            return SyncResult.partial(
                    DOMAIN_NAME,
                    syncedCount,
                    skippedCount,
                    failedCount,
                    startedAt,
                    "Some product groups failed to sync");
        }
        return SyncResult.success(DOMAIN_NAME, syncedCount, skippedCount, startedAt);
    }

    @Override
    public SyncResult incrementalSync(Instant lastSyncAt) {
        Instant startedAt = Instant.now();
        LocalDateTime lastSyncLocalTime = LocalDateTime.ofInstant(lastSyncAt, KST);

        log.info("Starting incremental sync for {} since {}", DOMAIN_NAME, lastSyncLocalTime);

        long syncedCount = 0;
        long skippedCount = 0;
        long failedCount = 0;

        // 1. 상품그룹 변경분 동기화
        List<LegacyProductGroupDto> changedProductGroups =
                legacyRepository.findProductGroupsModifiedAfter(
                        lastSyncLocalTime, DEFAULT_BATCH_SIZE);

        for (LegacyProductGroupDto productGroup : changedProductGroups) {
            try {
                SyncStatus status = syncSingleProductGroup(productGroup);
                switch (status) {
                    case SYNCED, UPDATED -> syncedCount++;
                    case SKIPPED -> skippedCount++;
                }
            } catch (Exception e) {
                log.error(
                        "Failed to sync product group. id={}, error={}",
                        productGroup.productGroupId(),
                        e.getMessage());
                failedCount++;
            }
        }

        // 2. 개별 상품(SKU) 변경분 동기화 (재고 변경 포함)
        List<LegacyProductDto> changedProducts =
                legacyRepository.findProductsModifiedAfter(lastSyncLocalTime, DEFAULT_BATCH_SIZE);

        for (LegacyProductDto product : changedProducts) {
            try {
                boolean synced = syncSingleProduct(product);
                if (synced) {
                    syncedCount++;
                } else {
                    skippedCount++;
                }
            } catch (Exception e) {
                log.error(
                        "Failed to sync product. id={}, error={}",
                        product.productId(),
                        e.getMessage());
                failedCount++;
            }
        }

        log.info(
                "Incremental sync completed. synced={}, skipped={}, failed={}",
                syncedCount,
                skippedCount,
                failedCount);

        if (failedCount > 0) {
            return SyncResult.partial(
                    DOMAIN_NAME,
                    syncedCount,
                    skippedCount,
                    failedCount,
                    startedAt,
                    "Some records failed to sync");
        }
        return SyncResult.success(DOMAIN_NAME, syncedCount, skippedCount, startedAt);
    }

    /**
     * 단일 상품그룹 동기화 (하위 상품 포함)
     *
     * @param legacyProductGroup 레거시 상품그룹 정보
     * @return 동기화 상태
     */
    @Transactional
    public SyncStatus syncSingleProductGroup(LegacyProductGroupDto legacyProductGroup) {
        Long legacyProductGroupId = legacyProductGroup.productGroupId();

        // 이미 존재하는지 확인
        if (migrationRepository.existsByLegacyProductGroupId(legacyProductGroupId)) {
            // 증분 동기화: UPDATE
            migrationRepository.updateProductGroup(legacyProductGroup);
            log.debug("Product group updated. legacyId={}", legacyProductGroupId);
            return SyncStatus.UPDATED;
        }

        // 신규 INSERT
        Long newProductGroupId = migrationRepository.insertProductGroupDirectly(legacyProductGroup);

        // 하위 상품(SKU) 동기화
        List<LegacyProductDto> products =
                legacyRepository.findProductsByProductGroupId(legacyProductGroupId);

        for (LegacyProductDto product : products) {
            Long newProductId =
                    migrationRepository.insertProductDirectly(newProductGroupId, product);

            // 재고 동기화
            migrationRepository.insertProductStock(newProductId, product.stockQuantity());
        }

        log.debug(
                "Product group synced. legacyId={}, newId={}, productCount={}",
                legacyProductGroupId,
                newProductGroupId,
                products.size());

        return SyncStatus.SYNCED;
    }

    /**
     * 단일 상품(SKU) 동기화 (증분 동기화용)
     *
     * @param legacyProduct 레거시 상품 정보
     * @return 동기화 성공 여부
     */
    @Transactional
    public boolean syncSingleProduct(LegacyProductDto legacyProduct) {
        Long legacyProductId = legacyProduct.productId();

        // 신규 상품 ID 조회
        Long productId = migrationRepository.findProductIdByLegacyId(legacyProductId);

        if (productId == null) {
            // 상품그룹이 먼저 동기화되어야 함
            Long productGroupId =
                    migrationRepository.findProductGroupIdByLegacyId(
                            legacyProduct.productGroupId());
            if (productGroupId == null) {
                log.warn(
                        "Product group not found for product. productId={}, productGroupId={}",
                        legacyProductId,
                        legacyProduct.productGroupId());
                return false;
            }

            // 신규 INSERT
            productId = migrationRepository.insertProductDirectly(productGroupId, legacyProduct);
            migrationRepository.insertProductStock(productId, legacyProduct.stockQuantity());
            log.debug("Product synced. legacyId={}, newId={}", legacyProductId, productId);
            return true;
        }

        // 기존 상품 UPDATE
        migrationRepository.updateProduct(legacyProduct);

        // 재고 UPDATE
        if (migrationRepository.existsProductStock(productId)) {
            migrationRepository.updateProductStock(productId, legacyProduct.stockQuantity());
        } else {
            migrationRepository.insertProductStock(productId, legacyProduct.stockQuantity());
        }

        log.debug("Product updated. legacyId={}, productId={}", legacyProductId, productId);
        return true;
    }

    private enum SyncStatus {
        SYNCED,
        SKIPPED,
        UPDATED
    }
}
