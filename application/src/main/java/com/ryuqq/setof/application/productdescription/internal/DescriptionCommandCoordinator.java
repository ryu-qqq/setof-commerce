package com.ryuqq.setof.application.productdescription.internal;

import com.ryuqq.setof.application.productdescription.dto.command.RegisterProductGroupDescriptionCommand;
import com.ryuqq.setof.application.productdescription.dto.command.UpdateProductGroupDescriptionCommand;
import com.ryuqq.setof.application.productdescription.factory.ProductGroupDescriptionCommandFactory;
import com.ryuqq.setof.application.productdescription.manager.ProductGroupDescriptionReadManager;
import com.ryuqq.setof.domain.productdescription.aggregate.DescriptionImage;
import com.ryuqq.setof.domain.productdescription.aggregate.ProductGroupDescription;
import com.ryuqq.setof.domain.productdescription.vo.DescriptionHtml;
import com.ryuqq.setof.domain.productdescription.vo.DescriptionUpdateData;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * DescriptionCommandCoordinator - 상세설명 등록/수정 Coordinator.
 *
 * <p>Factory로 도메인 객체 생성 → Facade로 영속화를 조율합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class DescriptionCommandCoordinator {

    private final ProductGroupDescriptionCommandFactory factory;
    private final ProductGroupDescriptionReadManager readManager;
    private final DescriptionCommandFacade facade;

    public DescriptionCommandCoordinator(
            ProductGroupDescriptionCommandFactory factory,
            ProductGroupDescriptionReadManager readManager,
            DescriptionCommandFacade facade) {
        this.factory = factory;
        this.readManager = readManager;
        this.facade = facade;
    }

    /**
     * 상세설명을 등록합니다.
     *
     * @param command 등록 커맨드
     * @return 생성된 상세설명 ID
     */
    @Transactional
    public Long register(RegisterProductGroupDescriptionCommand command) {
        ProductGroupDescription description = factory.createNewDescription(command);
        DescriptionHtml content = DescriptionHtml.of(command.content());
        List<DescriptionImage> images =
                factory.createNewImages(command.descriptionImages(), content);
        return facade.persistDescriptionWithImages(description, images);
    }

    /**
     * 상세설명을 수정합니다.
     *
     * @param command 수정 커맨드
     */
    @Transactional
    public void update(UpdateProductGroupDescriptionCommand command) {
        ProductGroupId productGroupId = ProductGroupId.of(command.productGroupId());
        ProductGroupDescription description = readManager.getByProductGroupId(productGroupId);

        DescriptionUpdateData updateData = factory.createUpdateData(command);
        description.update(updateData);

        facade.updateDescriptionWithImages(description, updateData.newImages());
    }
}
