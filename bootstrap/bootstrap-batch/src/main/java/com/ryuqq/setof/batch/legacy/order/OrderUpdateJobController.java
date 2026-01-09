package com.ryuqq.setof.batch.legacy.order;

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
import org.springframework.web.bind.annotation.*;

/**
 * 주문 업데이트 배치 Job 실행 컨트롤러
 *
 * @author development-team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/batch/order")
public class OrderUpdateJobController {

    private static final Logger log = LoggerFactory.getLogger(OrderUpdateJobController.class);

    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;

    private final Job updateRejectedOrdersJob;
    private final Job updateCompletedOrdersJob;
    private final Job updateSettlementOrdersJob;
    private final Job updateCancelRequestOrdersJob;
    private final Job updatePaymentFailOrdersJob;
    private final Job updateVBankFailOrdersJob;

    public OrderUpdateJobController(
            JobLauncher jobLauncher,
            JobExplorer jobExplorer,
            @Qualifier("updateRejectedOrdersJob") Job updateRejectedOrdersJob,
            @Qualifier("updateCompletedOrdersJob") Job updateCompletedOrdersJob,
            @Qualifier("updateSettlementOrdersJob") Job updateSettlementOrdersJob,
            @Qualifier("updateCancelRequestOrdersJob") Job updateCancelRequestOrdersJob,
            @Qualifier("updatePaymentFailOrdersJob") Job updatePaymentFailOrdersJob,
            @Qualifier("updateVBankFailOrdersJob") Job updateVBankFailOrdersJob) {
        this.jobLauncher = jobLauncher;
        this.jobExplorer = jobExplorer;
        this.updateRejectedOrdersJob = updateRejectedOrdersJob;
        this.updateCompletedOrdersJob = updateCompletedOrdersJob;
        this.updateSettlementOrdersJob = updateSettlementOrdersJob;
        this.updateCancelRequestOrdersJob = updateCancelRequestOrdersJob;
        this.updatePaymentFailOrdersJob = updatePaymentFailOrdersJob;
        this.updateVBankFailOrdersJob = updateVBankFailOrdersJob;
    }

    /** 거부된 주문 업데이트 Job 실행 */
    @PostMapping("/rejected/run")
    public ResponseEntity<Map<String, Object>> runRejectedOrdersJob() {
        return runJob(updateRejectedOrdersJob, "rejected");
    }

    /** 완료 처리 주문 업데이트 Job 실행 */
    @PostMapping("/completed/run")
    public ResponseEntity<Map<String, Object>> runCompletedOrdersJob() {
        return runJob(updateCompletedOrdersJob, "completed");
    }

    /** 정산 처리 주문 업데이트 Job 실행 */
    @PostMapping("/settlement/run")
    public ResponseEntity<Map<String, Object>> runSettlementOrdersJob() {
        return runJob(updateSettlementOrdersJob, "settlement");
    }

    /** 취소 요청 주문 처리 Job 실행 */
    @PostMapping("/cancel-request/run")
    public ResponseEntity<Map<String, Object>> runCancelRequestOrdersJob() {
        return runJob(updateCancelRequestOrdersJob, "cancel-request");
    }

    /** 결제 실패 주문 처리 Job 실행 */
    @PostMapping("/payment-fail/run")
    public ResponseEntity<Map<String, Object>> runPaymentFailOrdersJob() {
        return runJob(updatePaymentFailOrdersJob, "payment-fail");
    }

    /** 가상계좌 결제 실패 주문 처리 Job 실행 */
    @PostMapping("/vbank-fail/run")
    public ResponseEntity<Map<String, Object>> runVBankFailOrdersJob() {
        return runJob(updateVBankFailOrdersJob, "vbank-fail");
    }

    /** Job 실행 상태 조회 */
    @GetMapping("/{jobType}/status")
    public ResponseEntity<Map<String, Object>> getJobStatus(@PathVariable String jobType) {
        String jobName = getJobName(jobType);
        if (jobName == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Unknown job type: " + jobType));
        }

        Map<String, Object> response = new HashMap<>();
        try {
            var lastJobInstance = jobExplorer.getLastJobInstance(jobName);
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
            log.error("Failed to get job status for {}", jobType, e);
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    private ResponseEntity<Map<String, Object>> runJob(Job job, String jobType) {
        log.info("Order update job [{}] execution requested", jobType);
        Map<String, Object> response = new HashMap<>();

        try {
            JobParameters jobParameters =
                    new JobParametersBuilder()
                            .addLong("timestamp", System.currentTimeMillis())
                            .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(job, jobParameters);

            response.put("success", true);
            response.put("jobType", jobType);
            response.put("jobExecutionId", jobExecution.getId());
            response.put("status", jobExecution.getStatus().toString());
            response.put("startTime", jobExecution.getStartTime());
            response.put("message", "Job started successfully");

            log.info(
                    "Order update job [{}] started: executionId={}", jobType, jobExecution.getId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Failed to start order update job [{}]", jobType, e);
            response.put("success", false);
            response.put("jobType", jobType);
            response.put("message", "Failed to start job: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    private String getJobName(String jobType) {
        return switch (jobType) {
            case "rejected" -> "updateRejectedOrdersJob";
            case "completed" -> "updateCompletedOrdersJob";
            case "settlement" -> "updateSettlementOrdersJob";
            case "cancel-request" -> "updateCancelRequestOrdersJob";
            case "payment-fail" -> "updatePaymentFailOrdersJob";
            case "vbank-fail" -> "updateVBankFailOrdersJob";
            default -> null;
        };
    }
}
