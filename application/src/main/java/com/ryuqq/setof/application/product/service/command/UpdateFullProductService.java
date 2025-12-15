package com.ryuqq.setof.application.product.service.command;

import com.ryuqq.setof.application.product.dto.command.UpdateFullProductCommand;
import com.ryuqq.setof.application.product.facade.ProductModificationFacade;
import com.ryuqq.setof.application.product.manager.query.ProductGroupReadManager;
import com.ryuqq.setof.application.product.port.in.command.UpdateFullProductUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 전체 상품 수정 Service
 *
 * <p>상품그룹 + SKU + 이미지 + 설명 + 고시 일괄 수정
 *
 * <p>Diff 비교 후 변경분만 업데이트합니다.
 *
 * <p>트랜잭션 경계를 관리합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class UpdateFullProductService implements UpdateFullProductUseCase {

    private final ProductModificationFacade modificationFacade;
    private final ProductGroupReadManager productGroupReadManager;

    public UpdateFullProductService(
            ProductModificationFacade modificationFacade,
            ProductGroupReadManager productGroupReadManager) {
        this.modificationFacade = modificationFacade;
        this.productGroupReadManager = productGroupReadManager;
    }

    /**
     * 상품 전체 수정
     *
     * @param command 전체 수정 Command
     */
    @Override
    @Transactional
    public void updateFullProduct(UpdateFullProductCommand command) {
        // 존재 여부 검증 (없으면 예외 발생)
        productGroupReadManager.findById(command.productGroupId());

        modificationFacade.updateAll(command);
    }
}
