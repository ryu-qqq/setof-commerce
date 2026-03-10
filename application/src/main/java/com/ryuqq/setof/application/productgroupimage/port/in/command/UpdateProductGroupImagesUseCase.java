package com.ryuqq.setof.application.productgroupimage.port.in.command;

import com.ryuqq.setof.application.productgroupimage.dto.command.UpdateProductGroupImagesCommand;

/** 상품그룹 이미지 수정 UseCase. */
public interface UpdateProductGroupImagesUseCase {

    /**
     * 상품그룹 이미지를 수정합니다 (전체 교체).
     *
     * @param command 수정 커맨드
     */
    void execute(UpdateProductGroupImagesCommand command);
}
