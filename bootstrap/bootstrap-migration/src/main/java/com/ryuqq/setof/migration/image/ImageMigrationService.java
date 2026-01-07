package com.ryuqq.setof.migration.image;

import com.ryuqq.setof.application.common.port.out.FileStoragePort;
import com.ryuqq.setof.application.common.port.out.FileStoragePort.ExternalDownloadRequest;
import com.ryuqq.setof.application.common.port.out.FileStoragePort.ExternalDownloadResponse;
import com.ryuqq.setof.migration.core.SyncResult;
import com.ryuqq.setof.migration.core.SyncService;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 이미지 마이그레이션 서비스
 *
 * <p>CloudFront URL을 새 CDN URL로 마이그레이션합니다.
 *
 * <p><strong>마이그레이션 흐름:</strong>
 *
 * <ol>
 *   <li>product_group ID 커서 기반 조회
 *   <li>각 상품그룹의 이미지 조회 (product_group_image + detail_description)
 *   <li>CloudFront URL → S3 직접 URL 변환
 *   <li>FileFlow externalDownload API로 새 CDN 업로드
 *   <li>레거시 DB의 URL을 새 CDN URL로 업데이트
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class ImageMigrationService implements SyncService {

    private static final Logger log = LoggerFactory.getLogger(ImageMigrationService.class);

    private static final String DOMAIN_NAME = "image";
    private static final int DEFAULT_BATCH_SIZE = 100;
    private static final String CATEGORY_PRODUCTS = "products/images";
    private static final String OLD_CDN_DOMAIN = "https://d3fej89xf1vai5.cloudfront.net";
    private static final String NEW_CDN_DOMAIN = "https://cdn.set-of.com";

    private final LegacyImageRepository legacyImageRepository;
    private final ImageMigrationRepository imageMigrationRepository;
    private final FileStoragePort fileStoragePort;
    private final ImageMigrationConfig config;

    public ImageMigrationService(
            LegacyImageRepository legacyImageRepository,
            ImageMigrationRepository imageMigrationRepository,
            FileStoragePort fileStoragePort,
            ImageMigrationConfig config) {
        this.legacyImageRepository = legacyImageRepository;
        this.imageMigrationRepository = imageMigrationRepository;
        this.fileStoragePort = fileStoragePort;
        this.config = config;
    }

    @Override
    public String getDomainName() {
        return DOMAIN_NAME;
    }

    /**
     * 전체 이미지 마이그레이션 실행
     *
     * <p>모든 CloudFront 이미지를 새 CDN으로 마이그레이션합니다.
     *
     * @return 마이그레이션 결과
     */
    @Override
    public SyncResult initialMigration() {
        Instant startedAt = Instant.now();
        log.info("이미지 마이그레이션 시작");

        long totalProductGroups = legacyImageRepository.countAllMigrationTargetProductGroups();
        long totalImages = legacyImageRepository.countAllMigrationTargetImages();
        log.info("마이그레이션 대상: 상품그룹 {}개, 이미지 {}개", totalProductGroups, totalImages);

        AtomicLong syncedCount = new AtomicLong(0);
        AtomicLong failedCount = new AtomicLong(0);
        AtomicLong skippedCount = new AtomicLong(0);
        long lastProductGroupId = 0;
        int batchSize = config.getBatchSize() > 0 ? config.getBatchSize() : DEFAULT_BATCH_SIZE;

        while (true) {
            List<Long> productGroupIds =
                    legacyImageRepository.findProductGroupIdsForMigration(
                            lastProductGroupId, batchSize);

            if (productGroupIds.isEmpty()) {
                break;
            }

            for (Long productGroupId : productGroupIds) {
                try {
                    SyncStatus status = migrateProductGroupImages(productGroupId);
                    switch (status) {
                        case SYNCED -> syncedCount.incrementAndGet();
                        case SKIPPED -> skippedCount.incrementAndGet();
                        case FAILED -> failedCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    log.error("상품그룹 이미지 마이그레이션 실패: productGroupId={}", productGroupId, e);
                    failedCount.incrementAndGet();
                }

                lastProductGroupId = productGroupId;
            }

            log.info(
                    "배치 처리 완료: lastId={}, synced={}, failed={}, skipped={}",
                    lastProductGroupId,
                    syncedCount.get(),
                    failedCount.get(),
                    skippedCount.get());
        }

        log.info(
                "이미지 마이그레이션 완료: synced={}, failed={}, skipped={}",
                syncedCount.get(),
                failedCount.get(),
                skippedCount.get());

        if (failedCount.get() > 0) {
            return SyncResult.partial(
                    DOMAIN_NAME,
                    syncedCount.get(),
                    skippedCount.get(),
                    failedCount.get(),
                    startedAt,
                    Instant.now());
        }

        return SyncResult.success(
                DOMAIN_NAME, syncedCount.get(), skippedCount.get(), startedAt, Instant.now());
    }

    /**
     * 단일 상품그룹의 이미지 마이그레이션
     *
     * @param productGroupId 상품그룹 ID
     * @return 동기화 상태
     */
    public SyncStatus migrateProductGroupImages(Long productGroupId) {
        List<LegacyImageDto> images =
                legacyImageRepository.findAllImagesByProductGroupId(productGroupId);

        if (images.isEmpty()) {
            log.debug("마이그레이션 대상 이미지 없음: productGroupId={}", productGroupId);
            return SyncStatus.SKIPPED;
        }

        log.debug("이미지 마이그레이션 시작: productGroupId={}, imageCount={}", productGroupId, images.size());

        int successCount = 0;
        int failCount = 0;

        for (LegacyImageDto image : images) {
            try {
                boolean migrated = migrateImage(image);
                if (migrated) {
                    successCount++;
                } else {
                    failCount++;
                }
            } catch (Exception e) {
                log.error("이미지 마이그레이션 실패: imageId={}, url={}", image.id(), image.imageUrl(), e);
                failCount++;
            }
        }

        imageMigrationRepository.recordMigrationComplete(productGroupId, successCount);

        log.debug(
                "상품그룹 이미지 마이그레이션 완료: productGroupId={}, success={}, failed={}",
                productGroupId,
                successCount,
                failCount);

        if (failCount > 0 && successCount == 0) {
            return SyncStatus.FAILED;
        }

        return SyncStatus.SYNCED;
    }

    /**
     * 단일 이미지 마이그레이션
     *
     * @param image 이미지 정보
     * @return 성공 여부
     */
    private boolean migrateImage(LegacyImageDto image) {
        if (!image.isCloudFrontUrl()) {
            log.debug("CloudFront URL 아님, 스킵: {}", image.imageUrl());
            return true; // 이미 마이그레이션된 것으로 간주
        }

        // CloudFront URL → S3 직접 URL 변환
        String s3DirectUrl = image.toS3DirectUrl(config.getS3BucketName(), config.getS3Region());

        // FileFlow로 외부 URL 다운로드 → 새 CDN 업로드
        ExternalDownloadRequest request =
                ExternalDownloadRequest.of(
                        s3DirectUrl, CATEGORY_PRODUCTS, extractFilename(image.imageUrl()));

        ExternalDownloadResponse response = fileStoragePort.downloadFromExternalUrl(request);

        if (!response.success()) {
            log.error(
                    "FileFlow 외부 다운로드 실패: originalUrl={}, error={}",
                    image.imageUrl(),
                    response.errorMessage());
            return false;
        }

        // 레거시 DB URL 업데이트
        String newCdnUrl = response.newCdnUrl();
        int updatedCount = imageMigrationRepository.updateImageUrl(image, newCdnUrl);

        if (updatedCount == 0) {
            log.warn("URL 업데이트 실패: imageId={}", image.id());
            return false;
        }

        log.debug("이미지 마이그레이션 성공: {} → {}", image.imageUrl(), newCdnUrl);
        return true;
    }

    /** URL에서 파일명 추출 */
    private String extractFilename(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        int lastSlash = url.lastIndexOf('/');
        if (lastSlash < 0 || lastSlash >= url.length() - 1) {
            return null;
        }
        String filename = url.substring(lastSlash + 1);
        // 쿼리 파라미터 제거
        int queryIndex = filename.indexOf('?');
        if (queryIndex > 0) {
            filename = filename.substring(0, queryIndex);
        }
        return filename;
    }

    /** 증분 동기화 (이미지 마이그레이션에서는 미지원) */
    @Override
    public SyncResult incrementalSync(Instant lastSyncAt) {
        log.warn("이미지 마이그레이션은 증분 동기화를 지원하지 않습니다. initialMigration()을 사용하세요.");
        return SyncResult.skipped(DOMAIN_NAME, "증분 동기화 미지원");
    }

    /**
     * 마이그레이션 진행 상황 조회
     *
     * @return 진행 상황 정보
     */
    public MigrationProgress getProgress() {
        long totalProductGroups = legacyImageRepository.countAllMigrationTargetProductGroups();
        long totalImages = legacyImageRepository.countAllMigrationTargetImages();

        return new MigrationProgress(totalProductGroups, totalImages);
    }

    /** 마이그레이션 진행 상황 */
    public record MigrationProgress(long remainingProductGroups, long remainingImages) {}

    /** 개별 마이그레이션 작업 결과 상태 */
    public enum SyncStatus {
        /** 마이그레이션 완료 */
        SYNCED,
        /** 마이그레이션 대상 없음 (스킵) */
        SKIPPED,
        /** 마이그레이션 실패 */
        FAILED
    }
}
