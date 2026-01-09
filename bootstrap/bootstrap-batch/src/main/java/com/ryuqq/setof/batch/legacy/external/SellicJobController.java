package com.ryuqq.setof.batch.legacy.external;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** SELLIC 배치 Job REST API 컨트롤러 */
@RestController
@RequestMapping("/api/batch/sellic")
public class SellicJobController {

    private static final Logger log = LoggerFactory.getLogger(SellicJobController.class);

    private final JobLauncher jobLauncher;
    private final Job syncSellicOrderJob;

    public SellicJobController(JobLauncher jobLauncher, Job syncSellicOrderJob) {
        this.jobLauncher = jobLauncher;
        this.syncSellicOrderJob = syncSellicOrderJob;
    }

    @PostMapping("/order/run")
    public ResponseEntity<Map<String, Object>> runOrderSyncJob() {
        log.info("SELLIC order sync job execution requested");

        try {
            JobParameters params =
                    new JobParametersBuilder()
                            .addLong("timestamp", System.currentTimeMillis())
                            .toJobParameters();

            JobExecution execution = jobLauncher.run(syncSellicOrderJob, params);

            Map<String, Object> response = new HashMap<>();
            response.put("jobExecutionId", execution.getId());
            response.put("success", true);
            response.put("startTime", LocalDateTime.now().toString());
            response.put("message", "Job started successfully");
            response.put("status", execution.getStatus().toString());

            log.info("SELLIC order sync job started: executionId={}", execution.getId());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Failed to start SELLIC order sync job", e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());

            return ResponseEntity.internalServerError().body(response);
        }
    }
}
