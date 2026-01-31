package com.ryuqq.setof.migration.seller;

import com.ryuqq.setof.migration.core.checkpoint.MigrationCheckpoint;
import com.ryuqq.setof.migration.core.checkpoint.MigrationCheckpointRepository;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Seller 마이그레이션 Job REST Controller
 *
 * <p>마이그레이션 Job 실행 및 상태 조회를 위한 REST API를 제공합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/migration/seller")
public class SellerMigrationJobController {

    private static final Logger log = LoggerFactory.getLogger(SellerMigrationJobController.class);
    private static final String DOMAIN_NAME = "seller";

    private final JobLauncher jobLauncher;
    private final Job sellerMigrationJob;
    private final MigrationCheckpointRepository checkpointRepository;
    private final LegacySellerRepository legacySellerRepository;

    public SellerMigrationJobController(
            JobLauncher jobLauncher,
            @Qualifier("sellerMigrationJob") Job sellerMigrationJob,
            MigrationCheckpointRepository checkpointRepository,
            LegacySellerRepository legacySellerRepository) {
        this.jobLauncher = jobLauncher;
        this.sellerMigrationJob = sellerMigrationJob;
        this.checkpointRepository = checkpointRepository;
        this.legacySellerRepository = legacySellerRepository;
    }

    /**
     * 마이그레이션 시작/재개
     *
     * @return 실행 결과
     */
    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startMigration() {
        try {
            JobParameters params =
                    new JobParametersBuilder()
                            .addLong("startTime", System.currentTimeMillis())
                            .toJobParameters();

            var execution = jobLauncher.run(sellerMigrationJob, params);

            Map<String, Object> response = new HashMap<>();
            response.put("jobId", execution.getJobId());
            response.put("status", execution.getStatus().toString());
            response.put("startTime", execution.getStartTime());

            log.info("Seller migration job started: {}", execution.getJobId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to start seller migration job", e);
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * 마이그레이션 상태 조회
     *
     * @return 상태 정보
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> response = new HashMap<>();

        var checkpoint = checkpointRepository.findByDomainName(DOMAIN_NAME);
        long totalCount = legacySellerRepository.countAllSellers();

        if (checkpoint.isPresent()) {
            MigrationCheckpoint cp = checkpoint.get();
            long remaining = legacySellerRepository.countSellersAfter(cp.lastMigratedId());
            double progress =
                    totalCount > 0 ? ((double) (totalCount - remaining) / totalCount) * 100 : 0;

            response.put("status", cp.status().name());
            response.put("lastMigratedId", cp.lastMigratedId());
            response.put("totalCount", totalCount);
            response.put("migratedCount", cp.migratedCount());
            response.put("remainingCount", remaining);
            response.put("progress", String.format("%.2f%%", progress));
            response.put("syncMode", cp.syncMode().name());
            response.put("lastExecutedAt", cp.lastExecutedAt());
            response.put("lastCompletedAt", cp.lastCompletedAt());
        } else {
            response.put("status", "NOT_STARTED");
            response.put("totalCount", totalCount);
            response.put("migratedCount", 0);
            response.put("remainingCount", totalCount);
            response.put("progress", "0.00%");
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 체크포인트 초기화 (주의: 처음부터 다시 시작)
     *
     * @return 결과
     */
    @PostMapping("/reset")
    public ResponseEntity<Map<String, Object>> resetCheckpoint() {
        checkpointRepository.resetCheckpoint(DOMAIN_NAME);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Checkpoint reset successfully");
        response.put("domain", DOMAIN_NAME);

        log.warn("Seller migration checkpoint has been reset");
        return ResponseEntity.ok(response);
    }
}
