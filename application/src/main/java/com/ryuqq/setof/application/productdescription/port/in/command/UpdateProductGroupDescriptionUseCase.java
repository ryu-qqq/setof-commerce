package com.ryuqq.setof.application.productdescription.port.in.command;

import com.ryuqq.setof.application.productdescription.dto.command.UpdateProductGroupDescriptionCommand;

/** 상품그룹 상세설명 수정 UseCase. */
public interface UpdateProductGroupDescriptionUseCase {

    /**
     * 상세설명을 수정합니다.
     *
     * @param command 수정 커맨드
     */
    void execute(UpdateProductGroupDescriptionCommand command);
}
