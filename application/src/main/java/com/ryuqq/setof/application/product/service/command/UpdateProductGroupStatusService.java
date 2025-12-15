package com.ryuqq.setof.application.product.service.command;

import com.ryuqq.setof.application.product.dto.command.UpdateProductGroupStatusCommand;
import com.ryuqq.setof.application.product.factory.command.ProductGroupCommandFactory;
import com.ryuqq.setof.application.product.manager.command.ProductGroupPersistenceManager;
import com.ryuqq.setof.application.product.manager.query.ProductGroupReadManager;
import com.ryuqq.setof.application.product.port.in.command.UpdateProductGroupStatusUseCase;
import com.ryuqq.setof.domain.product.aggregate.ProductGroup;
import com.ryuqq.setof.domain.product.vo.ProductGroupStatus;
import java.time.Instant;
import org.springframework.stereotype.Service;

/**
 * 상품그룹 상태 변경 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>ProductGroupReadManager로 기존 ProductGroup 조회
 *   <li>셀러 소유권 검증
 *   <li>상태 변경
 *   <li>ProductGroupPersistenceManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UpdateProductGroupStatusService implements UpdateProductGroupStatusUseCase {

    private final ProductGroupReadManager productGroupReadManager;
    private final ProductGroupPersistenceManager productGroupPersistenceManager;
    private final ProductGroupCommandFactory productGroupCommandFactory;

    public UpdateProductGroupStatusService(
            ProductGroupReadManager productGroupReadManager,
            ProductGroupPersistenceManager productGroupPersistenceManager,
            ProductGroupCommandFactory productGroupCommandFactory) {
        this.productGroupReadManager = productGroupReadManager;
        this.productGroupPersistenceManager = productGroupPersistenceManager;
        this.productGroupCommandFactory = productGroupCommandFactory;
    }

    @Override
    public void execute(UpdateProductGroupStatusCommand command) {
        ProductGroup productGroup = productGroupReadManager.findById(command.productGroupId());

        // 셀러 소유권 검증
        validateOwnership(productGroup, command.sellerId());

        ProductGroupStatus newStatus = ProductGroupStatus.valueOf(command.status());
        Instant now = productGroupCommandFactory.now();

        ProductGroup updated = applyStatusChange(productGroup, newStatus, now);
        productGroupPersistenceManager.persist(updated);
    }

    private void validateOwnership(ProductGroup productGroup, Long sellerId) {
        if (!productGroup.getSellerIdValue().equals(sellerId)) {
            throw new IllegalArgumentException("해당 상품그룹의 상태 변경 권한이 없습니다");
        }
    }

    private ProductGroup applyStatusChange(
            ProductGroup productGroup, ProductGroupStatus newStatus, Instant now) {
        return switch (newStatus) {
            case ACTIVE -> productGroup.activate(now);
            case INACTIVE -> productGroup.deactivate(now);
            case DELETED -> productGroup.delete(now);
        };
    }
}
