package com.ryuqq.setof.migration.core;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * CLI 실행기
 *
 * <p>애플리케이션 시작 시 명령줄 인자에 따라 동기화 모드를 실행합니다.
 *
 * <p><strong>사용법:</strong>
 *
 * <pre>
 * # 초기 마이그레이션 (전체 데이터)
 * java -jar migration.jar --mode=initial
 *
 * # 초기 마이그레이션 (특정 도메인만)
 * java -jar migration.jar --mode=initial --domain=member
 *
 * # 증분 동기화 모드 (상시 실행)
 * java -jar migration.jar --mode=sync
 *
 * # 상태 확인
 * java -jar migration.jar --mode=status
 *
 * # 특정 도메인 일시 중지
 * java -jar migration.jar --mode=pause --domain=member
 *
 * # 특정 도메인 재개
 * java -jar migration.jar --mode=resume --domain=member
 * </pre>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class SyncRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(SyncRunner.class);

    private final SyncScheduler scheduler;
    private final SyncStatusRepository statusRepository;

    public SyncRunner(SyncScheduler scheduler, SyncStatusRepository statusRepository) {
        this.scheduler = scheduler;
        this.statusRepository = statusRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        String mode = getArgValue(args, "mode", "sync");
        String domain = getArgValue(args, "domain", null);

        log.info("Migration runner started. mode={}, domain={}", mode, domain);

        switch (mode) {
            case "initial" -> runInitialMigration(domain);
            case "sync" -> runSyncMode();
            case "status" -> printStatus();
            case "pause" -> pauseDomain(domain);
            case "resume" -> resumeDomain(domain);
            default -> {
                log.error("Unknown mode: {}. Use: initial, sync, status, pause, resume", mode);
                System.exit(1);
            }
        }
    }

    private void runInitialMigration(String domain) {
        if (domain != null) {
            log.info("Running initial migration for domain: {}", domain);
            SyncResult result = scheduler.runInitialMigration(domain);
            printResult(result);
        } else {
            log.info("Running initial migration for all domains...");
            scheduler.runAllInitialMigrations();
        }

        log.info("Initial migration completed. Exiting...");
        System.exit(0);
    }

    private void runSyncMode() {
        log.info("Sync mode activated. Scheduler will run automatically.");
        log.info("Registered domains: {}", scheduler.getRegisteredDomains());
        printStatus();
        // 스케줄러가 자동으로 동기화를 수행하므로 종료하지 않음
    }

    private void printStatus() {
        log.info("=== Sync Status ===");
        List<SyncStatus> statuses = statusRepository.findAll();

        System.out.println();
        System.out.println(
                "┌──────────────────┬──────────┬─────────────────────────┬───────────┬───────────┐");
        System.out.println(
                "│ Domain           │ Status   │ Last Sync               │ Interval  │ Total    "
                        + " │");
        System.out.println(
                "├──────────────────┼──────────┼─────────────────────────┼───────────┼───────────┤");

        for (SyncStatus status : statuses) {
            System.out.printf(
                    "│ %-16s │ %-8s │ %-23s │ %4d min  │ %9d │%n",
                    status.domainName(),
                    status.status(),
                    status.lastSyncAt(),
                    status.syncIntervalMinutes(),
                    status.totalSyncedCount());
        }

        System.out.println(
                "└──────────────────┴──────────┴─────────────────────────┴───────────┴───────────┘");
        System.out.println();
    }

    private void pauseDomain(String domain) {
        if (domain == null) {
            log.error("Domain is required for pause mode. Use: --domain=member");
            System.exit(1);
        }

        statusRepository.updateStatus(domain, SyncStatusType.PAUSED);
        log.info("Domain paused: {}", domain);
        System.exit(0);
    }

    private void resumeDomain(String domain) {
        if (domain == null) {
            log.error("Domain is required for resume mode. Use: --domain=member");
            System.exit(1);
        }

        statusRepository.updateStatus(domain, SyncStatusType.ACTIVE);
        log.info("Domain resumed: {}", domain);
        System.exit(0);
    }

    private void printResult(SyncResult result) {
        if (result.isSuccessful()) {
            log.info("✅ {}", result.toSummary());
        } else {
            log.warn("⚠️ {}", result.toSummary());
        }
    }

    private String getArgValue(ApplicationArguments args, String name, String defaultValue) {
        List<String> values = args.getOptionValues(name);
        if (values == null || values.isEmpty()) {
            return defaultValue;
        }
        return values.get(0);
    }
}
