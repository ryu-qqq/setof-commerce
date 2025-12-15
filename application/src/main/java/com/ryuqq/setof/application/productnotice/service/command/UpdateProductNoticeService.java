package com.ryuqq.setof.application.productnotice.service.command;

import com.ryuqq.setof.application.productnotice.dto.command.UpdateProductNoticeCommand;
import com.ryuqq.setof.application.productnotice.factory.command.ProductNoticeCommandFactory;
import com.ryuqq.setof.application.productnotice.manager.command.ProductNoticePersistenceManager;
import com.ryuqq.setof.application.productnotice.manager.query.ProductNoticeReadManager;
import com.ryuqq.setof.application.productnotice.port.in.command.UpdateProductNoticeUseCase;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import com.ryuqq.setof.domain.productnotice.vo.NoticeItem;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 상품고시 수정 서비스
 *
 * <p>상품고시를 수정합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UpdateProductNoticeService implements UpdateProductNoticeUseCase {

    private final ProductNoticeReadManager productNoticeReadManager;
    private final ProductNoticePersistenceManager productNoticePersistenceManager;
    private final ProductNoticeCommandFactory productNoticeCommandFactory;

    public UpdateProductNoticeService(
            ProductNoticeReadManager productNoticeReadManager,
            ProductNoticePersistenceManager productNoticePersistenceManager,
            ProductNoticeCommandFactory productNoticeCommandFactory) {
        this.productNoticeReadManager = productNoticeReadManager;
        this.productNoticePersistenceManager = productNoticePersistenceManager;
        this.productNoticeCommandFactory = productNoticeCommandFactory;
    }

    @Override
    public void execute(UpdateProductNoticeCommand command) {
        ProductNotice productNotice = productNoticeReadManager.findById(command.productNoticeId());

        Instant now = productNoticeCommandFactory.now();
        List<NoticeItem> items = productNoticeCommandFactory.toItems(command.items());

        ProductNotice updated = productNotice.replaceItems(items, now);

        productNoticePersistenceManager.update(updated);
    }
}
