package com.ryuqq.setof.batch.legacy.shipment;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 배송 추적 배치 Job 실행 컨트롤러
 *
 * @author development-team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/batch/shipment")
public class TrackingShipmentJobController {

    private static final Logger log = LoggerFactory.getLogger(TrackingShipmentJobController.class);
    private static final String JOB_NAME = "trackingShipmentJob";

    private final JobLauncher jobLauncher;
    private final Job trackingShipmentJob;
    private final JobExplorer jobExplorer;

    public TrackingShipmentJobController(
            JobLauncher jobLauncher,
            @Qualifier("trackingShipmentJob") Job trackingShipmentJob,
            JobExplorer jobExplorer) {
        this.jobLauncher = jobLauncher;
        this.trackingShipmentJob = trackingShipmentJob;
        this.jobExplorer = jobExplorer;
    }

    /** 배송 추적 Job 실행 */
    @PostMapping("/run")
    public ResponseEntity<Map<String, Object>> runJob() {
        log.info("Tracking shipment job execution requested");

        Map<String, Object> response = new HashMap<>();

        try {
            JobParameters jobParameters =
                    new JobParametersBuilder()
                            .addLong("timestamp", System.currentTimeMillis())
                            .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(trackingShipmentJob, jobParameters);

            response.put("success", true);
            response.put("jobExecutionId", jobExecution.getId());
            response.put("status", jobExecution.getStatus().toString());
            response.put("startTime", jobExecution.getStartTime());
            response.put("message", "Job started successfully");

            log.info("Tracking shipment job started: executionId={}", jobExecution.getId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Failed to start tracking shipment job", e);
            response.put("success", false);
            response.put("message", "Failed to start job: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /** 마지막 Job 실행 상태 조회 */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getJobStatus() {
        Map<String, Object> response = new HashMap<>();

        try {
            var lastJobInstance = jobExplorer.getLastJobInstance(JOB_NAME);
            if (lastJobInstance == null) {
                response.put("message", "No job execution found");
                return ResponseEntity.ok(response);
            }

            var lastExecution = jobExplorer.getLastJobExecution(lastJobInstance);
            if (lastExecution != null) {
                response.put("jobExecutionId", lastExecution.getId());
                response.put("status", lastExecution.getStatus().toString());
                response.put("startTime", lastExecution.getStartTime());
                response.put("endTime", lastExecution.getEndTime());
                response.put("exitStatus", lastExecution.getExitStatus().getExitCode());

                // Step 실행 정보
                lastExecution
                        .getStepExecutions()
                        .forEach(
                                step -> {
                                    Map<String, Object> stepInfo = new HashMap<>();
                                    stepInfo.put("stepName", step.getStepName());
                                    stepInfo.put("readCount", step.getReadCount());
                                    stepInfo.put("writeCount", step.getWriteCount());
                                    stepInfo.put("skipCount", step.getSkipCount());
                                    stepInfo.put("status", step.getStatus().toString());
                                    response.put("stepExecution", stepInfo);
                                });
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Failed to get job status", e);
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
