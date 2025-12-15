package com.ryuqq.setof.application.productdescription.port.in.command;

import com.ryuqq.setof.application.productdescription.dto.command.UpdateProductDescriptionCommand;

/**
 * 상품설명 수정 UseCase
 *
 * <p>상품설명을 수정합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateProductDescriptionUseCase {

    /**
     * 상품설명 수정
     *
     * @param command 수정 Command
     */
    void execute(UpdateProductDescriptionCommand command);
}
