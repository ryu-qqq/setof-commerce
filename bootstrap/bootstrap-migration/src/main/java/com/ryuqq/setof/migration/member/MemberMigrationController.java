package com.ryuqq.setof.migration.member;

import com.ryuqq.setof.migration.member.MemberMigrationService.MigrationResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Member 마이그레이션 API
 *
 * <p>수동으로 마이그레이션을 트리거하거나 진행 상황을 확인하는 API입니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/migration/members")
public class MemberMigrationController {

    private final MemberMigrationService memberMigrationService;
    private final LegacyUserRepository legacyUserRepository;
    private final MemberMigrationRepository memberMigrationRepository;

    public MemberMigrationController(
            MemberMigrationService memberMigrationService,
            LegacyUserRepository legacyUserRepository,
            MemberMigrationRepository memberMigrationRepository) {
        this.memberMigrationService = memberMigrationService;
        this.legacyUserRepository = legacyUserRepository;
        this.memberMigrationRepository = memberMigrationRepository;
    }

    /**
     * 마이그레이션 실행
     *
     * @param batchSize 배치 크기 (기본값 1000)
     * @return 마이그레이션 결과
     */
    @PostMapping("/run")
    public ResponseEntity<MigrationResultResponse> runMigration(
            @RequestParam(defaultValue = "1000") int batchSize) {
        MigrationResult result = memberMigrationService.migrateAll(batchSize);
        return ResponseEntity.ok(MigrationResultResponse.from(result));
    }

    /**
     * 마이그레이션 진행 상황 조회
     *
     * @return 진행 상황
     */
    @GetMapping("/status")
    public ResponseEntity<MigrationStatusResponse> getStatus() {
        long totalLegacyUsers = legacyUserRepository.countAllUsers();
        long lastMigratedId = memberMigrationRepository.findLastMigratedLegacyUserId();
        long remainingUsers = legacyUserRepository.countUsersAfter(lastMigratedId);
        long migratedUsers = totalLegacyUsers - remainingUsers;

        double progressPercent =
                totalLegacyUsers > 0 ? (migratedUsers * 100.0 / totalLegacyUsers) : 100.0;

        return ResponseEntity.ok(
                new MigrationStatusResponse(
                        totalLegacyUsers,
                        migratedUsers,
                        remainingUsers,
                        lastMigratedId,
                        progressPercent));
    }

    /** 마이그레이션 결과 응답 */
    public record MigrationResultResponse(
            long total,
            long migrated,
            long skipped,
            long failed,
            double successRate,
            boolean successful) {

        public static MigrationResultResponse from(MigrationResult result) {
            return new MigrationResultResponse(
                    result.total(),
                    result.migrated(),
                    result.skipped(),
                    result.failed(),
                    result.successRate(),
                    result.isSuccessful());
        }
    }

    /** 마이그레이션 진행 상황 응답 */
    public record MigrationStatusResponse(
            long totalLegacyUsers,
            long migratedUsers,
            long remainingUsers,
            long lastMigratedLegacyUserId,
            double progressPercent) {}
}
