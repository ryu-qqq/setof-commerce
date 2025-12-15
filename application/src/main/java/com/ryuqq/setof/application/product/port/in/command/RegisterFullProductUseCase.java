package com.ryuqq.setof.application.product.port.in.command;

import com.ryuqq.setof.application.product.dto.command.RegisterFullProductCommand;

/**
 * 전체 상품 등록 UseCase
 *
 * <p>상품그룹 + SKU + 이미지 + 설명 + 고시 일괄 등록
 *
 * @author development-team
 * @since 1.0.0
 */
public interface RegisterFullProductUseCase {

    /**
     * 상품 전체 등록 (ProductGroup + SKUs + Images + Description + Notice)
     *
     * @param command 전체 등록 Command
     * @return 생성된 ProductGroup ID
     */
    Long registerFullProduct(RegisterFullProductCommand command);
}
