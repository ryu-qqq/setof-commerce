package com.ryuqq.setof.batch.legacy.notification;

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
 * 알림톡 배치 Job 실행 컨트롤러
 *
 * @author development-team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/batch/notification")
public class NotificationJobController {

    private static final Logger log = LoggerFactory.getLogger(NotificationJobController.class);

    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;

    private final Job alimTalkNotifyJob;
    private final Job scheduleAutoCancelJob;
    private final Job scheduleVBankCancelJob;

    public NotificationJobController(
            JobLauncher jobLauncher,
            JobExplorer jobExplorer,
            @Qualifier("alimTalkNotifyJob") Job alimTalkNotifyJob,
            @Qualifier("scheduleAutoCancelJob") Job scheduleAutoCancelJob,
            @Qualifier("scheduleVBankCancelJob") Job scheduleVBankCancelJob) {
        this.jobLauncher = jobLauncher;
        this.jobExplorer = jobExplorer;
        this.alimTalkNotifyJob = alimTalkNotifyJob;
        this.scheduleAutoCancelJob = scheduleAutoCancelJob;
        this.scheduleVBankCancelJob = scheduleVBankCancelJob;
    }

    /** 알림톡 발송 Job 실행 스케줄: 5분마다 */
    @PostMapping("/alim-talk/run")
    public ResponseEntity<Map<String, Object>> runAlimTalkNotifyJob() {
        return runJob(alimTalkNotifyJob, "alim-talk");
    }

    /** 자동 취소 알림 스케줄 Job 실행 스케줄: 매시 */
    @PostMapping("/schedule-auto-cancel/run")
    public ResponseEntity<Map<String, Object>> runScheduleAutoCancelJob() {
        return runJob(scheduleAutoCancelJob, "schedule-auto-cancel");
    }

    /** 가상계좌 만료 알림 스케줄 Job 실행 스케줄: 15분마다 */
    @PostMapping("/schedule-vbank-cancel/run")
    public ResponseEntity<Map<String, Object>> runScheduleVBankCancelJob() {
        return runJob(scheduleVBankCancelJob, "schedule-vbank-cancel");
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
        log.info("Notification job [{}] execution requested", jobType);
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
                    "Notification job [{}] started: executionId={}", jobType, jobExecution.getId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Failed to start notification job [{}]", jobType, e);
            response.put("success", false);
            response.put("jobType", jobType);
            response.put("message", "Failed to start job: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    private String getJobName(String jobType) {
        return switch (jobType) {
            case "alim-talk" -> "alimTalkNotifyJob";
            case "schedule-auto-cancel" -> "scheduleAutoCancelJob";
            case "schedule-vbank-cancel" -> "scheduleVBankCancelJob";
            default -> null;
        };
    }
}
