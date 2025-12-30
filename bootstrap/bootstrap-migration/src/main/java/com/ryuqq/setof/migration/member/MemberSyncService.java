package com.ryuqq.setof.migration.member;

import com.github.f4b6a3.uuid.UuidCreator;
import com.ryuqq.setof.migration.core.SyncResult;
import com.ryuqq.setof.migration.core.SyncService;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Member 동기화 서비스
 *
 * <p>레거시 Users 테이블 데이터를 신규 members 테이블로 동기화합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class MemberSyncService implements SyncService {

    private static final Logger log = LoggerFactory.getLogger(MemberSyncService.class);
    private static final String DOMAIN_NAME = "member";
    private static final int DEFAULT_BATCH_SIZE = 1000;
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private final LegacyUserRepository legacyUserRepository;
    private final MemberMigrationRepository memberMigrationRepository;

    public MemberSyncService(
            LegacyUserRepository legacyUserRepository,
            MemberMigrationRepository memberMigrationRepository) {
        this.legacyUserRepository = legacyUserRepository;
        this.memberMigrationRepository = memberMigrationRepository;
    }

    @Override
    public String getDomainName() {
        return DOMAIN_NAME;
    }

    @Override
    public SyncResult initialMigration() {
        Instant startedAt = Instant.now();
        log.info("Starting initial migration for {}", DOMAIN_NAME);

        long total = legacyUserRepository.countAllUsers();
        long syncedCount = 0;
        long skippedCount = 0;
        long failedCount = 0;
        long offset = 0;

        while (offset < total) {
            List<LegacyUserDto> users =
                    legacyUserRepository.findUsersForMigration(offset, DEFAULT_BATCH_SIZE);

            if (users.isEmpty()) {
                break;
            }

            for (LegacyUserDto legacyUser : users) {
                try {
                    boolean migrated = syncSingleUser(legacyUser);
                    if (migrated) {
                        syncedCount++;
                    } else {
                        skippedCount++;
                    }
                } catch (Exception e) {
                    log.error(
                            "Failed to sync user. userId={}, error={}",
                            legacyUser.userId(),
                            e.getMessage());
                    failedCount++;
                }
            }

            offset += DEFAULT_BATCH_SIZE;
            log.info("Initial migration progress: {}/{}", Math.min(offset, total), total);
        }

        if (failedCount > 0) {
            return SyncResult.partial(
                    DOMAIN_NAME,
                    syncedCount,
                    skippedCount,
                    failedCount,
                    startedAt,
                    "Some records failed to sync");
        }
        return SyncResult.success(DOMAIN_NAME, syncedCount, skippedCount, startedAt);
    }

    @Override
    public SyncResult incrementalSync(Instant lastSyncAt) {
        Instant startedAt = Instant.now();
        LocalDateTime lastSyncLocalTime = LocalDateTime.ofInstant(lastSyncAt, KST);

        log.info("Starting incremental sync for {} since {}", DOMAIN_NAME, lastSyncLocalTime);

        List<LegacyUserDto> changedUsers =
                legacyUserRepository.findUsersModifiedAfter(lastSyncLocalTime, DEFAULT_BATCH_SIZE);

        long syncedCount = 0;
        long skippedCount = 0;
        long failedCount = 0;

        for (LegacyUserDto legacyUser : changedUsers) {
            try {
                boolean synced = syncSingleUser(legacyUser);
                if (synced) {
                    syncedCount++;
                } else {
                    skippedCount++;
                }
            } catch (Exception e) {
                log.error(
                        "Failed to sync user. userId={}, error={}",
                        legacyUser.userId(),
                        e.getMessage());
                failedCount++;
            }
        }

        log.info(
                "Incremental sync completed. synced={}, skipped={}, failed={}",
                syncedCount,
                skippedCount,
                failedCount);

        if (failedCount > 0) {
            return SyncResult.partial(
                    DOMAIN_NAME,
                    syncedCount,
                    skippedCount,
                    failedCount,
                    startedAt,
                    "Some records failed to sync");
        }
        return SyncResult.success(DOMAIN_NAME, syncedCount, skippedCount, startedAt);
    }

    /**
     * 단일 사용자 동기화
     *
     * @param legacyUser 레거시 사용자 정보
     * @return 신규 동기화 여부 (이미 존재하면 false)
     */
    @Transactional
    public boolean syncSingleUser(LegacyUserDto legacyUser) {
        // 이미 마이그레이션된 사용자인지 확인
        if (memberMigrationRepository.existsByLegacyUserId(legacyUser.userId())) {
            // TODO: 증분 동기화 시 UPDATE 로직 추가 가능
            log.debug("User already exists. legacyUserId={}", legacyUser.userId());
            return false;
        }

        // UUID v7 생성
        UUID newMemberId = UuidCreator.getTimeOrderedEpoch();

        // 직접 INSERT (Domain VO 검증 우회)
        memberMigrationRepository.insertMemberDirectly(newMemberId, legacyUser);

        log.debug("User synced. legacyUserId={}, newMemberId={}", legacyUser.userId(), newMemberId);

        return true;
    }
}
