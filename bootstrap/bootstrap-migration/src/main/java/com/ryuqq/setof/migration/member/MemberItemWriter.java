package com.ryuqq.setof.migration.member;

import com.ryuqq.setof.migration.core.checkpoint.MigrationCheckpointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

/**
 * Member 데이터 Writer
 *
 * <p>변환된 Member 데이터를 신규 DB에 저장하고 체크포인트를 업데이트합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public class MemberItemWriter implements ItemWriter<MemberMigrationData> {

    private static final Logger log = LoggerFactory.getLogger(MemberItemWriter.class);
    private static final String DOMAIN_NAME = "member";

    private final MemberMigrationRepository memberMigrationRepository;
    private final MigrationCheckpointRepository checkpointRepository;

    public MemberItemWriter(
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
        log.info("Writing {} members to new database", items.size());

        // 배치 INSERT
        for (MemberMigrationData data : items) {
            try {
                // 이미 마이그레이션된 사용자인지 확인
                if (memberMigrationRepository.existsByLegacyUserId(data.legacyUserId())) {
                    log.debug("Skipping already migrated user: legacyId={}", data.legacyUserId());
                    continue;
                }

                memberMigrationRepository.insertMemberDirectly(data);
            } catch (Exception e) {
                log.error(
                        "Failed to insert member: legacyId={}, error={}",
                        data.legacyUserId(),
                        e.getMessage());
                throw e;
            }
        }

        // 체크포인트 업데이트 (마지막 아이템의 레거시 ID)
        MemberMigrationData lastItem = items.get(items.size() - 1);
        checkpointRepository.updateCheckpoint(DOMAIN_NAME, lastItem.legacyUserId(), items.size());

        log.info(
                "Chunk written successfully. lastMigratedId={}, count={}",
                lastItem.legacyUserId(),
                items.size());
    }
}
