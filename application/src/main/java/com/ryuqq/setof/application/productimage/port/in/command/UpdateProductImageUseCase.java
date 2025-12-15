package com.ryuqq.setof.application.productimage.port.in.command;

import com.ryuqq.setof.application.productimage.dto.command.UpdateProductImageCommand;

/**
 * 상품이미지 수정 UseCase
 *
 * @author development-team
 * @since 1.0.0
 */
public interface UpdateProductImageUseCase {

    /**
     * 상품이미지 수정
     *
     * @param command 수정 Command
     */
    void update(UpdateProductImageCommand command);
}
