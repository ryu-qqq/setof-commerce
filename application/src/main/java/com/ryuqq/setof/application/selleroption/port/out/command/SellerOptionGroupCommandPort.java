package com.ryuqq.setof.application.selleroption.port.out.command;

import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionGroup;
import java.util.List;

/**
 * SellerOptionGroupCommandPort - 셀러 옵션 그룹 Command 출력 포트.
 *
 * <p>Adapter가 구현할 출력 포트입니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
public interface SellerOptionGroupCommandPort {

    /**
     * 셀러 옵션 그룹을 저장합니다.
     *
     * @param sellerOptionGroup 저장할 셀러 옵션 그룹 도메인 객체
     * @return 저장된 셀러 옵션 그룹 ID
     */
    Long persist(SellerOptionGroup sellerOptionGroup);

    /**
     * 셀러 옵션 그룹 목록을 일괄 저장합니다.
     *
     * @param sellerOptionGroups 저장할 셀러 옵션 그룹 도메인 객체 목록
     */
    void persistAll(List<SellerOptionGroup> sellerOptionGroups);
}
