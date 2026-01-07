package com.ryuqq.setof.migration.image;

import com.ryuqq.setof.migration.core.SyncResult;
import com.ryuqq.setof.migration.image.ImageMigrationService.MigrationProgress;
import com.ryuqq.setof.migration.image.ImageMigrationService.SyncStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 이미지 마이그레이션 API
 *
 * <p>CloudFront 이미지를 새 CDN으로 마이그레이션하는 API입니다.
 *
 * <p><strong>API 목록:</strong>
 *
 * <ul>
 *   <li>POST /api/migration/images/run - 전체 마이그레이션 실행
 *   <li>POST /api/migration/images/run/{productGroupId} - 단일 상품그룹 마이그레이션
 *   <li>GET /api/migration/images/status - 마이그레이션 진행 상황 조회
 *   <li>GET /api/migration/images/config - 마이그레이션 설정 조회
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/migration/images")
public class ImageMigrationController {

    private static final Logger log = LoggerFactory.getLogger(ImageMigrationController.class);

    private final ImageMigrationService imageMigrationService;
    private final ImageMigrationConfig config;

    public ImageMigrationController(
            ImageMigrationService imageMigrationService, ImageMigrationConfig config) {
        this.imageMigrationService = imageMigrationService;
        this.config = config;
    }

    /**
     * 전체 이미지 마이그레이션 실행
     *
     * <p>모든 CloudFront 이미지를 새 CDN으로 마이그레이션합니다. 이 작업은 시간이 오래 걸릴 수 있습니다.
     *
     * @param dryRun true면 실제 마이그레이션 없이 대상만 확인
     * @return 마이그레이션 결과
     */
    @PostMapping("/run")
    public ResponseEntity<MigrationResultResponse> runMigration(
            @RequestParam(defaultValue = "false") boolean dryRun) {

        if (!config.isEnabled()) {
            log.warn("이미지 마이그레이션이 비활성화되어 있습니다. migration.image.enabled=true로 설정하세요.");
            return ResponseEntity.badRequest()
                    .body(MigrationResultResponse.error("마이그레이션이 비활성화되어 있습니다."));
        }

        if (dryRun) {
            MigrationProgress progress = imageMigrationService.getProgress();
            return ResponseEntity.ok(
                    MigrationResultResponse.dryRun(
                            progress.remainingProductGroups(), progress.remainingImages()));
        }

        log.info("전체 이미지 마이그레이션 시작 요청");
        SyncResult result = imageMigrationService.initialMigration();

        return ResponseEntity.ok(MigrationResultResponse.from(result));
    }

    /**
     * 단일 상품그룹 이미지 마이그레이션
     *
     * @param productGroupId 상품그룹 ID
     * @return 마이그레이션 결과
     */
    @PostMapping("/run/{productGroupId}")
    public ResponseEntity<SingleMigrationResponse> runSingleMigration(
            @PathVariable Long productGroupId) {

        if (!config.isEnabled()) {
            return ResponseEntity.badRequest()
                    .body(SingleMigrationResponse.error("마이그레이션이 비활성화되어 있습니다."));
        }

        log.info("단일 상품그룹 이미지 마이그레이션 시작: productGroupId={}", productGroupId);
        SyncStatus status = imageMigrationService.migrateProductGroupImages(productGroupId);

        return ResponseEntity.ok(SingleMigrationResponse.from(productGroupId, status));
    }

    /**
     * 마이그레이션 진행 상황 조회
     *
     * @return 진행 상황
     */
    @GetMapping("/status")
    public ResponseEntity<MigrationStatusResponse> getStatus() {
        MigrationProgress progress = imageMigrationService.getProgress();

        return ResponseEntity.ok(
                new MigrationStatusResponse(
                        progress.remainingProductGroups(),
                        progress.remainingImages(),
                        config.isEnabled()));
    }

    /**
     * 마이그레이션 설정 조회
     *
     * @return 현재 설정
     */
    @GetMapping("/config")
    public ResponseEntity<ImageMigrationConfig> getConfig() {
        return ResponseEntity.ok(config);
    }

    /** 마이그레이션 결과 응답 */
    public record MigrationResultResponse(
            long syncedCount,
            long skippedCount,
            long failedCount,
            boolean successful,
            String message,
            boolean dryRun) {

        public static MigrationResultResponse from(SyncResult result) {
            return new MigrationResultResponse(
                    result.syncedCount(),
                    result.skippedCount(),
                    result.failedCount(),
                    result.isSuccess(),
                    result.isSuccess() ? "마이그레이션 완료" : "일부 실패",
                    false);
        }

        public static MigrationResultResponse dryRun(long productGroups, long images) {
            return new MigrationResultResponse(
                    0,
                    0,
                    0,
                    true,
                    String.format("DRY RUN: %d개 상품그룹, %d개 이미지 마이그레이션 대상", productGroups, images),
                    true);
        }

        public static MigrationResultResponse error(String message) {
            return new MigrationResultResponse(0, 0, 0, false, message, false);
        }
    }

    /** 단일 마이그레이션 결과 응답 */
    public record SingleMigrationResponse(
            Long productGroupId, String status, boolean successful, String message) {

        public static SingleMigrationResponse from(Long productGroupId, SyncStatus status) {
            return new SingleMigrationResponse(
                    productGroupId,
                    status.name(),
                    status == SyncStatus.SYNCED,
                    switch (status) {
                        case SYNCED -> "마이그레이션 완료";
                        case SKIPPED -> "마이그레이션 대상 없음";
                        case FAILED -> "마이그레이션 실패";
                        default -> "알 수 없는 상태";
                    });
        }

        public static SingleMigrationResponse error(String message) {
            return new SingleMigrationResponse(null, "ERROR", false, message);
        }
    }

    /** 마이그레이션 상태 응답 */
    public record MigrationStatusResponse(
            long remainingProductGroups, long remainingImages, boolean enabled) {}
}
