package com.ryuqq.setof.application.productgroup.port.in.command;

import com.ryuqq.setof.application.productgroup.dto.command.RegisterProductGroupCommand;

/**
 * 상품그룹 전체 등록 유스케이스.
 *
 * <p>상품그룹 기본정보, 이미지, 옵션, 상세설명, 고시, 상품을 한번에 등록합니다.
 */
public interface RegisterProductGroupFullUseCase {

    /**
     * 상품그룹 전체 등록을 실행합니다.
     *
     * @param command 상품그룹 전체 등록 커맨드
     * @return 저장된 상품그룹 ID
     */
    Long execute(RegisterProductGroupCommand command);
}
