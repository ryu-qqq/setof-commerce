package com.ryuqq.setof.application.productdescription.port.in.command;

import com.ryuqq.setof.application.productdescription.dto.command.RegisterProductGroupDescriptionCommand;

/** 상품그룹 상세설명 등록 UseCase. */
public interface RegisterProductGroupDescriptionUseCase {

    /**
     * 상세설명을 등록합니다.
     *
     * @param command 등록 커맨드
     * @return 생성된 상세설명 ID
     */
    Long execute(RegisterProductGroupDescriptionCommand command);
}
