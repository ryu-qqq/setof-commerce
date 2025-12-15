package com.ryuqq.setof.application.product.port.in.command;

import com.ryuqq.setof.application.product.dto.command.UpdateFullProductCommand;

/**
 * 전체 상품 수정 UseCase
 *
 * <p>상품그룹 + SKU + 이미지 + 설명 + 고시 일괄 수정
 *
 * <p>변경 감지(Diff) 후 변경분만 업데이트
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateFullProductUseCase {

    /**
     * 상품 전체 수정 (Diff 비교 후 변경분만 업데이트)
     *
     * @param command 전체 수정 Command
     */
    void updateFullProduct(UpdateFullProductCommand command);
}
