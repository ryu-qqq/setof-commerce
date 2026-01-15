package com.ryuqq.setof.migration.member;

import com.ryuqq.setof.migration.core.checkpoint.MigrationCheckpointRepository;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

/**
 * Member 증분 동기화 Writer
 *
 * <p>레거시에서 수정된 데이터를 신규 DB에 UPSERT하고 체크포인트를 업데이트합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public class MemberIncrementalSyncItemWriter implements ItemWriter<MemberMigrationData> {

    private static final Logger log =
            LoggerFactory.getLogger(MemberIncrementalSyncItemWriter.class);
    private static final String DOMAIN_NAME = "member";
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private final MemberMigrationRepository memberMigrationRepository;
    private final MigrationCheckpointRepository checkpointRepository;

    public MemberIncrementalSyncItemWriter(
            MemberMigrationRepository memberMigrationRepository,
            MigrationCheckpointRepository checkpointRepository) {
        this.memberMigrationRepository = memberMigrationRepository;
        this.checkpointRepository = checkpointRepository;
    }

    @Override
    public void write(Chunk<? extends MemberMigrationData> chunk) {
        if (chunk.isEmpty()) {
            return;
        }

        var items = chunk.getItems();
        log.info("Incremental sync: processing {} members", items.size());

        int insertedCount = 0;
        int updatedCount = 0;
        int skippedCount = 0;
        LocalDateTime maxUpdatedAt = null;

        for (MemberMigrationData data : items) {
            try {
                boolean isInsert = memberMigrationRepository.upsertMember(data);
                if (isInsert) {
                    insertedCount++;
                } else {
                    updatedCount++;
                }

                // 최대 updatedAt 추적 (체크포인트용)
                if (data.updatedAt() != null) {
                    if (maxUpdatedAt == null || data.updatedAt().isAfter(maxUpdatedAt)) {
                        maxUpdatedAt = data.updatedAt();
                    }
                }
            } catch (Exception e) {
                log.error(
                        "Failed to upsert member: legacyId={}, error={}",
                        data.legacyUserId(),
                        e.getMessage());
                skippedCount++;
                checkpointRepository.incrementFailedCount(DOMAIN_NAME, 1);
            }
        }

        // 증분 동기화 체크포인트 업데이트
        if (maxUpdatedAt != null) {
            Instant lastSyncedAt = maxUpdatedAt.atZone(KST).toInstant();
            checkpointRepository.updateIncrementalCheckpoint(
                    DOMAIN_NAME, lastSyncedAt, insertedCount + updatedCount, skippedCount);
        }

        log.info(
                "Incremental sync completed. inserted={}, updated={}, skipped={}",
                insertedCount,
                updatedCount,
                skippedCount);
    }
}
