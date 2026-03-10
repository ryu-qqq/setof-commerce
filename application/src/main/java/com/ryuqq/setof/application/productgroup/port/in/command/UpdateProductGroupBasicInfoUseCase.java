package com.ryuqq.setof.application.productgroup.port.in.command;

import com.ryuqq.setof.application.productgroup.dto.command.UpdateProductGroupBasicInfoCommand;

/**
 * 상품그룹 기본정보 수정 유스케이스.
 *
 * <p>상품그룹의 이름, 브랜드, 카테고리, 배송정책, 환불정책을 수정합니다.
 */
public interface UpdateProductGroupBasicInfoUseCase {

    /**
     * 상품그룹 기본정보 수정을 실행합니다.
     *
     * @param command 기본정보 수정 커맨드
     */
    void execute(UpdateProductGroupBasicInfoCommand command);
}
