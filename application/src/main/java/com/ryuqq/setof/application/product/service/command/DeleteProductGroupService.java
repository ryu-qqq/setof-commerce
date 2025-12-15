package com.ryuqq.setof.application.product.service.command;

import com.ryuqq.setof.application.product.dto.command.DeleteProductGroupCommand;
import com.ryuqq.setof.application.product.factory.command.ProductGroupCommandFactory;
import com.ryuqq.setof.application.product.manager.command.ProductGroupPersistenceManager;
import com.ryuqq.setof.application.product.manager.query.ProductGroupReadManager;
import com.ryuqq.setof.application.product.port.in.command.DeleteProductGroupUseCase;
import com.ryuqq.setof.domain.product.aggregate.ProductGroup;
import java.time.Instant;
import org.springframework.stereotype.Service;

/**
 * 상품그룹 삭제 서비스
 *
 * <p>처리 순서:
 *
 * <ol>
 *   <li>ProductGroupReadManager로 기존 ProductGroup 조회
 *   <li>셀러 소유권 검증
 *   <li>Soft Delete 처리
 *   <li>ProductGroupPersistenceManager로 저장
 * </ol>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class DeleteProductGroupService implements DeleteProductGroupUseCase {

    private final ProductGroupReadManager productGroupReadManager;
    private final ProductGroupPersistenceManager productGroupPersistenceManager;
    private final ProductGroupCommandFactory productGroupCommandFactory;

    public DeleteProductGroupService(
            ProductGroupReadManager productGroupReadManager,
            ProductGroupPersistenceManager productGroupPersistenceManager,
            ProductGroupCommandFactory productGroupCommandFactory) {
        this.productGroupReadManager = productGroupReadManager;
        this.productGroupPersistenceManager = productGroupPersistenceManager;
        this.productGroupCommandFactory = productGroupCommandFactory;
    }

    @Override
    public void execute(DeleteProductGroupCommand command) {
        ProductGroup productGroup = productGroupReadManager.findById(command.productGroupId());

        // 셀러 소유권 검증
        validateOwnership(productGroup, command.sellerId());

        Instant now = productGroupCommandFactory.now();
        ProductGroup deleted = productGroup.delete(now);

        productGroupPersistenceManager.persist(deleted);
    }

    private void validateOwnership(ProductGroup productGroup, Long sellerId) {
        if (!productGroup.getSellerIdValue().equals(sellerId)) {
            throw new IllegalArgumentException("해당 상품그룹의 삭제 권한이 없습니다");
        }
    }
}
