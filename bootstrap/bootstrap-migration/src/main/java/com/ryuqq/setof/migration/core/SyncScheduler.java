package com.ryuqq.setof.migration.core;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 동기화 스케줄러
 *
 * <p>등록된 모든 SyncService를 주기적으로 실행합니다. 각 도메인별로 설정된 주기(sync_interval_minutes)에 따라 동기화가 실행됩니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class SyncScheduler {

    private static final Logger log = LoggerFactory.getLogger(SyncScheduler.class);

    private final SyncStatusRepository statusRepository;
    private final Map<String, SyncService> syncServices;

    public SyncScheduler(SyncStatusRepository statusRepository, List<SyncService> syncServiceList) {
        this.statusRepository = statusRepository;
        this.syncServices =
                syncServiceList.stream()
                        .filter(SyncService::isEnabled)
                        .collect(Collectors.toMap(SyncService::getDomainName, Function.identity()));

        log.info("Registered sync services: {}", syncServices.keySet());
    }

    /**
     * 1분마다 실행되는 스케줄러
     *
     * <p>각 도메인별로 동기화 필요 여부를 확인하고 실행합니다.
     */
    @Scheduled(fixedRate = 60000) // 1분마다 체크
    public void scheduledSync() {
        log.debug("Checking sync status for all domains...");

        Instant now = Instant.now();
        List<SyncStatus> activeStatuses = statusRepository.findAllActive();

        for (SyncStatus status : activeStatuses) {
            if (status.needsSync(now)) {
                executeSyncForDomain(status);
            }
        }
    }

    /**
     * 특정 도메인 동기화 실행
     *
     * @param status 동기화 상태
     */
    private void executeSyncForDomain(SyncStatus status) {
        String domainName = status.domainName();
        SyncService service = syncServices.get(domainName);

        if (service == null) {
            log.warn("No sync service found for domain: {}", domainName);
            return;
        }

        log.info("Starting incremental sync for domain: {}", domainName);

        try {
            SyncResult result = service.incrementalSync(status.lastSyncAt());
            statusRepository.updateAfterSync(domainName, result);

            if (result.isSuccessful()) {
                log.info("Sync completed: {}", result.toSummary());
            } else {
                log.warn("Sync completed with issues: {}", result.toSummary());
            }
        } catch (Exception e) {
            log.error("Sync failed for domain: {}", domainName, e);
            SyncResult failureResult =
                    SyncResult.failure(domainName, Instant.now(), e.getMessage());
            statusRepository.updateAfterSync(domainName, failureResult);
        }
    }

    /**
     * 수동으로 특정 도메인 초기 마이그레이션 실행
     *
     * @param domainName 도메인명
     * @return 동기화 결과
     */
    public SyncResult runInitialMigration(String domainName) {
        SyncService service = syncServices.get(domainName);
        if (service == null) {
            throw new IllegalArgumentException("Unknown domain: " + domainName);
        }

        log.info("Starting initial migration for domain: {}", domainName);

        try {
            SyncResult result = service.initialMigration();
            statusRepository.updateAfterSync(domainName, result);
            log.info("Initial migration completed: {}", result.toSummary());
            return result;
        } catch (Exception e) {
            log.error("Initial migration failed for domain: {}", domainName, e);
            SyncResult failureResult =
                    SyncResult.failure(domainName, Instant.now(), e.getMessage());
            statusRepository.updateAfterSync(domainName, failureResult);
            return failureResult;
        }
    }

    /**
     * 수동으로 특정 도메인 증분 동기화 실행
     *
     * @param domainName 도메인명
     * @return 동기화 결과
     */
    public SyncResult runIncrementalSync(String domainName) {
        SyncService service = syncServices.get(domainName);
        if (service == null) {
            throw new IllegalArgumentException("Unknown domain: " + domainName);
        }

        SyncStatus status =
                statusRepository
                        .findByDomainName(domainName)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "No sync status for domain: " + domainName));

        log.info("Starting manual incremental sync for domain: {}", domainName);

        try {
            SyncResult result = service.incrementalSync(status.lastSyncAt());
            statusRepository.updateAfterSync(domainName, result);
            log.info("Incremental sync completed: {}", result.toSummary());
            return result;
        } catch (Exception e) {
            log.error("Incremental sync failed for domain: {}", domainName, e);
            SyncResult failureResult =
                    SyncResult.failure(domainName, Instant.now(), e.getMessage());
            statusRepository.updateAfterSync(domainName, failureResult);
            return failureResult;
        }
    }

    /** 모든 도메인 초기 마이그레이션 실행 */
    public void runAllInitialMigrations() {
        log.info("Starting initial migration for all domains...");

        for (String domainName : syncServices.keySet()) {
            runInitialMigration(domainName);
        }

        log.info("All initial migrations completed.");
    }

    /**
     * 등록된 도메인 목록 조회
     *
     * @return 도메인명 목록
     */
    public List<String> getRegisteredDomains() {
        return List.copyOf(syncServices.keySet());
    }
}
