package com.ryuqq.setof.application.discount.port.in.command;

import com.ryuqq.setof.application.discount.dto.command.CreateDiscountPolicyCommand;
import java.util.List;

/**
 * 엑셀 업로드 기반 할인 정책 일괄 생성 UseCase.
 *
 * <p>엑셀에서 파싱된 할인 정책 목록을 일괄 생성합니다. 각 Command는 독립적으로 처리되며 생성된 정책 ID 목록을 반환합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface CreateDiscountPoliciesFromExcelUseCase {

    /**
     * 엑셀에서 파싱된 정책 목록을 일괄 생성합니다.
     *
     * @param commands 정책 생성 Command 목록
     * @return 생성된 정책 ID 목록
     */
    List<Long> execute(List<CreateDiscountPolicyCommand> commands);
}
