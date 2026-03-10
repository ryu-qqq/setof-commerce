package com.ryuqq.setof.application.productnotice.internal;

import com.ryuqq.setof.application.productnotice.dto.command.RegisterProductNoticeCommand;
import com.ryuqq.setof.application.productnotice.dto.command.UpdateProductNoticeCommand;
import com.ryuqq.setof.application.productnotice.factory.ProductNoticeCommandFactory;
import com.ryuqq.setof.application.productnotice.manager.ProductNoticeReadManager;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import com.ryuqq.setof.domain.productnotice.vo.ProductNoticeUpdateData;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ProductNoticeCommandCoordinator - 상품고시 등록/수정 Coordinator.
 *
 * <p>Factory로 도메인 객체 생성 → Facade로 영속화를 조율합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ProductNoticeCommandCoordinator {

    private final ProductNoticeCommandFactory factory;
    private final ProductNoticeReadManager readManager;
    private final ProductNoticeCommandFacade facade;

    public ProductNoticeCommandCoordinator(
            ProductNoticeCommandFactory factory,
            ProductNoticeReadManager readManager,
            ProductNoticeCommandFacade facade) {
        this.factory = factory;
        this.readManager = readManager;
        this.facade = facade;
    }

    /**
     * 상품고시를 등록합니다.
     *
     * @param command 등록 커맨드
     * @return 생성된 상품고시 ID
     */
    @Transactional
    public Long register(RegisterProductNoticeCommand command) {
        ProductNotice notice = factory.createNewNotice(command);
        return facade.persistNoticeWithEntries(notice);
    }

    /**
     * 상품고시를 수정합니다.
     *
     * @param command 수정 커맨드
     */
    @Transactional
    public void update(UpdateProductNoticeCommand command) {
        ProductGroupId productGroupId = ProductGroupId.of(command.productGroupId());
        ProductNotice notice = readManager.getByProductGroupId(productGroupId);

        ProductNoticeUpdateData updateData = factory.createUpdateData(command);
        notice.update(updateData);

        facade.updateNoticeWithEntries(notice);
    }
}
