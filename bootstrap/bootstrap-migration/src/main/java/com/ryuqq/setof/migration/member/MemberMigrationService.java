package com.ryuqq.setof.migration.member;

import com.github.f4b6a3.uuid.UuidCreator;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Member 마이그레이션 서비스
 *
 * <p>레거시 Users 테이블 데이터를 신규 members 테이블로 마이그레이션합니다.
 *
 * <p><strong>마이그레이션 전략:</strong>
 *
 * <ul>
 *   <li>배치 단위로 처리 (기본 1000건)
 *   <li>UUID v7 신규 ID 생성
 *   <li>legacy_user_id에 레거시 ID 저장
 *   <li>이미 마이그레이션된 데이터는 스킵
 *   <li>Domain VO 검증 우회 (레거시 데이터 호환성)
 * </ul>
 *
 * <p><strong>주의:</strong> 마이그레이션은 Domain 검증을 우회합니다. 레거시 데이터가 현재 도메인 규칙을 위반할 수 있기 때문입니다. (예: 이름 길이,
 * 전화번호 형식 등)
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class MemberMigrationService {

    private static final Logger log = LoggerFactory.getLogger(MemberMigrationService.class);
    private static final int DEFAULT_BATCH_SIZE = 1000;

    private final LegacyUserRepository legacyUserRepository;
    private final MemberMigrationRepository memberMigrationRepository;

    public MemberMigrationService(
            LegacyUserRepository legacyUserRepository,
            MemberMigrationRepository memberMigrationRepository) {
        this.legacyUserRepository = legacyUserRepository;
        this.memberMigrationRepository = memberMigrationRepository;
    }

    /**
     * 전체 마이그레이션 실행
     *
     * @return 마이그레이션 결과
     */
    public MigrationResult migrateAll() {
        return migrateAll(DEFAULT_BATCH_SIZE);
    }

    /**
     * 전체 마이그레이션 실행 (배치 크기 지정)
     *
     * @param batchSize 배치 크기
     * @return 마이그레이션 결과
     */
    public MigrationResult migrateAll(int batchSize) {
        long totalUsers = legacyUserRepository.countAllUsers();
        log.info("Starting member migration. Total users: {}", totalUsers);

        long migratedCount = 0;
        long skippedCount = 0;
        long failedCount = 0;
        long offset = 0;

        while (offset < totalUsers) {
            List<LegacyUserDto> users =
                    legacyUserRepository.findUsersForMigration(offset, batchSize);

            if (users.isEmpty()) {
                break;
            }

            for (LegacyUserDto legacyUser : users) {
                try {
                    boolean migrated = migrateSingleUser(legacyUser);
                    if (migrated) {
                        migratedCount++;
                    } else {
                        skippedCount++;
                    }
                } catch (Exception e) {
                    log.error(
                            "Failed to migrate user. userId={}, error={}",
                            legacyUser.userId(),
                            e.getMessage());
                    failedCount++;
                }
            }

            offset += batchSize;
            log.info(
                    "Migration progress: {}/{} ({} migrated, {} skipped, {} failed)",
                    Math.min(offset, totalUsers),
                    totalUsers,
                    migratedCount,
                    skippedCount,
                    failedCount);
        }

        log.info(
                "Member migration completed. Migrated: {}, Skipped: {}, Failed: {}",
                migratedCount,
                skippedCount,
                failedCount);

        return new MigrationResult(totalUsers, migratedCount, skippedCount, failedCount);
    }

    /**
     * 단일 사용자 마이그레이션
     *
     * @param legacyUser 레거시 사용자 정보
     * @return 마이그레이션 수행 여부 (이미 존재하면 false)
     */
    @Transactional
    public boolean migrateSingleUser(LegacyUserDto legacyUser) {
        // 이미 마이그레이션된 사용자인지 확인
        if (memberMigrationRepository.existsByLegacyUserId(legacyUser.userId())) {
            log.debug("User already migrated. legacyUserId={}", legacyUser.userId());
            return false;
        }

        // UUID v7 생성
        UUID newMemberId = UuidCreator.getTimeOrderedEpoch();

        // 직접 INSERT (Domain VO 검증 우회)
        memberMigrationRepository.insertMemberDirectly(newMemberId, legacyUser);

        log.debug(
                "User migrated successfully. legacyUserId={}, newMemberId={}",
                legacyUser.userId(),
                newMemberId);

        return true;
    }

    /** 마이그레이션 결과 */
    public record MigrationResult(long total, long migrated, long skipped, long failed) {

        public boolean isSuccessful() {
            return failed == 0;
        }

        public double successRate() {
            if (total == 0) {
                return 100.0;
            }
            return (migrated + skipped) * 100.0 / total;
        }
    }
}
