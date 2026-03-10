package com.ryuqq.setof.application.productgroup.port.in.command;

import com.ryuqq.setof.application.productgroup.dto.command.UpdateProductGroupFullCommand;

/**
 * 상품그룹 전체 수정 유스케이스.
 *
 * <p>상품그룹 기본정보, 이미지, 옵션, 상세설명, 고시, 상품을 한번에 수정합니다.
 */
public interface UpdateProductGroupFullUseCase {

    /**
     * 상품그룹 전체 수정을 실행합니다.
     *
     * @param command 상품그룹 전체 수정 커맨드
     */
    void execute(UpdateProductGroupFullCommand command);
}
