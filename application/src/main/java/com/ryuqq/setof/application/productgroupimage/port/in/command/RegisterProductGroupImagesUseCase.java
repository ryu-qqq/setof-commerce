package com.ryuqq.setof.application.productgroupimage.port.in.command;

import com.ryuqq.setof.application.productgroupimage.dto.command.RegisterProductGroupImagesCommand;

/** 상품그룹 이미지 등록 UseCase. */
public interface RegisterProductGroupImagesUseCase {

    /**
     * 상품그룹 이미지를 등록합니다.
     *
     * @param command 등록 커맨드
     */
    void execute(RegisterProductGroupImagesCommand command);
}
