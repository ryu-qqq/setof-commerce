package com.ryuqq.setof.migration.seller;

import com.ryuqq.setof.migration.core.checkpoint.MigrationCheckpointRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

/**
 * Seller 데이터 Writer
 *
 * <p>변환된 SellerApplication 데이터를 신규 DB에 저장합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public class SellerItemWriter implements ItemWriter<SellerApplicationMigrationData> {

    private static final Logger log = LoggerFactory.getLogger(SellerItemWriter.class);
    private static final String DOMAIN_NAME = "seller";

    private final SellerMigrationRepository sellerMigrationRepository;
    private final MigrationCheckpointRepository checkpointRepository;

    public SellerItemWriter(
            SellerMigrationRepository sellerMigrationRepository,
            MigrationCheckpointRepository checkpointRepository) {
        this.sellerMigrationRepository = sellerMigrationRepository;
        this.checkpointRepository = checkpointRepository;
    }

    @Override
    public void write(Chunk<? extends SellerApplicationMigrationData> chunk) {
        List<SellerApplicationMigrationData> items =
                chunk.getItems().stream()
                        .map(item -> (SellerApplicationMigrationData) item)
                        .toList();

        if (items.isEmpty()) {
            return;
        }

        // 배치 저장
        sellerMigrationRepository.batchInsertSellerApplications(items);

        // 체크포인트 업데이트 (마지막 처리된 레거시 ID와 처리 건수)
        long lastSellerId = items.get(items.size() - 1).legacySellerId();
        checkpointRepository.updateCheckpoint(DOMAIN_NAME, lastSellerId, items.size());

        log.info("Written {} seller applications, lastSellerId={}", items.size(), lastSellerId);
    }
}
