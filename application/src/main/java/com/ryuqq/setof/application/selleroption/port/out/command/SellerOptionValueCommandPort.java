package com.ryuqq.setof.application.selleroption.port.out.command;

import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionValue;
import java.util.List;

/**
 * SellerOptionValueCommandPort - 셀러 옵션 값 Command 출력 포트.
 *
 * <p>Adapter가 구현할 출력 포트입니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public interface SellerOptionValueCommandPort {

    /**
     * 셀러 옵션 값을 저장합니다.
     *
     * @param sellerOptionValue 저장할 셀러 옵션 값 도메인 객체
     * @return 저장된 셀러 옵션 값 ID
     */
    Long persist(SellerOptionValue sellerOptionValue);

    /**
     * 셀러 옵션 값 목록을 일괄 저장합니다.
     *
     * @param sellerOptionValues 저장할 셀러 옵션 값 도메인 객체 목록
     * @return 저장된 셀러 옵션 값 ID 목록
     */
    List<Long> persistAll(List<SellerOptionValue> sellerOptionValues);

    /**
     * 특정 옵션 그룹에 속한 셀러 옵션 값 목록을 일괄 저장합니다.
     *
     * @param sellerOptionGroupId 셀러 옵션 그룹 ID
     * @param sellerOptionValues 저장할 셀러 옵션 값 도메인 객체 목록
     * @return 저장된 셀러 옵션 값 ID 목록
     */
    List<Long> persistAllForGroup(
            Long sellerOptionGroupId, List<SellerOptionValue> sellerOptionValues);
}
