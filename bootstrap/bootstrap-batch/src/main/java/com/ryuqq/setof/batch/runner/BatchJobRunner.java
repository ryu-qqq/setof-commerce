package com.ryuqq.setof.batch.runner;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 환경변수 기반 배치 Job 실행기
 *
 * <p>ECS RunTask + EventBridge 스케줄링 환경에서 사용
 * JOB_NAME 환경변수로 실행할 Job을 지정하면 해당 Job 실행 후 컨테이너 종료
 *
 * <p>사용 예시:
 * <pre>
 * JOB_NAME=trackingShipmentJob → 배송 추적 Job 실행
 * JOB_NAME=syncSellicOrderJob → Sellic 주문 동기화 Job 실행
 * JOB_NAME=alimTalkNotifyJob → 알림톡 발송 Job 실행
 * JOB_NAME=updateCompletedOrdersJob → 완료 주문 상태 업데이트 Job 실행
 * </pre>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
@Profile("!local")  // local 환경 제외, prod 환경에서 활성화
public class BatchJobRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(BatchJobRunner.class);

    private static final Map<String, String> JOB_DESCRIPTIONS = Map.ofEntries(
            Map.entry("trackingShipmentJob", "배송 추적"),
            Map.entry("syncSellicOrderJob", "Sellic 주문 동기화"),
            Map.entry("alimTalkNotifyJob", "알림톡 발송"),
            Map.entry("scheduleAutoCancelJob", "자동 취소 예약 알림"),
            Map.entry("scheduleVBankCancelJob", "가상계좌 취소 예약 알림"),
            Map.entry("updateRejectedOrdersJob", "거부 주문 상태 업데이트"),
            Map.entry("updateCompletedOrdersJob", "완료 주문 상태 업데이트"),
            Map.entry("updateSettlementOrdersJob", "정산 주문 상태 업데이트"),
            Map.entry("updateCancelRequestOrdersJob", "취소 요청 주문 처리"),
            Map.entry("updatePaymentFailOrdersJob", "결제 실패 주문 처리"),
            Map.entry("updateVBankFailOrdersJob", "가상계좌 결제 실패 주문 처리")
    );

    private final JobLauncher jobLauncher;
    private final ApplicationContext applicationContext;

    @Value("${JOB_NAME:#{null}}")
    private String jobName;

    public BatchJobRunner(JobLauncher jobLauncher, ApplicationContext applicationContext) {
        this.jobLauncher = jobLauncher;
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (jobName == null || jobName.isBlank()) {
            log.info("JOB_NAME 환경변수가 설정되지 않음. 대기 모드로 실행됩니다.");
            return;
        }

        log.info("========================================");
        log.info("배치 Job 실행 시작");
        log.info("JOB_NAME: {}", jobName);
        log.info("설명: {}", JOB_DESCRIPTIONS.getOrDefault(jobName, "알 수 없는 Job"));
        log.info("========================================");

        int exitCode = 0;
        try {
            Job job = applicationContext.getBean(jobName, Job.class);

            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .addString("triggeredBy", "EventBridge")
                    .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(job, jobParameters);

            log.info("========================================");
            log.info("배치 Job 실행 완료");
            log.info("Job 이름: {}", jobName);
            log.info("실행 ID: {}", jobExecution.getId());
            log.info("상태: {}", jobExecution.getStatus());
            log.info("시작 시간: {}", jobExecution.getStartTime());
            log.info("종료 시간: {}", jobExecution.getEndTime());
            log.info("Exit Status: {}", jobExecution.getExitStatus().getExitCode());
            log.info("========================================");

            jobExecution.getStepExecutions().forEach(step -> {
                log.info("Step [{}]: read={}, write={}, skip={}, status={}",
                        step.getStepName(),
                        step.getReadCount(),
                        step.getWriteCount(),
                        step.getSkipCount(),
                        step.getStatus());
            });

            if (jobExecution.getStatus().isUnsuccessful()) {
                log.error("Job 실행 실패: {}", jobExecution.getExitStatus().getExitDescription());
                exitCode = 1;
            }

        } catch (Exception e) {
            log.error("Job 실행 중 오류 발생: {}", jobName, e);
            exitCode = 1;
        } finally {
            log.info("컨테이너 종료 (exitCode={})", exitCode);
            SpringApplication.exit(applicationContext, () -> exitCode);
        }
    }
}
