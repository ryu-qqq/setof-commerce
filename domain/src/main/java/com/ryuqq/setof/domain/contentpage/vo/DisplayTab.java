package com.ryuqq.setof.domain.contentpage.vo;

import java.util.List;

/**
 * DisplayTab - 탭 컴포넌트의 개별 탭 VO.
 *
 * @param tabId 탭 ID (nullable, 신규 시 null)
 * @param tabName 탭 이름
 * @param displayOrder 노출 순서
 * @param fixedProducts 고정 상품 목록
 * @param autoProducts 자동 상품 목록
 * @author ryu-qqq
 * @since 1.1.0
 */
public record DisplayTab(
        Long tabId,
        String tabName,
        int displayOrder,
        List<ProductSlot> fixedProducts,
        List<ProductSlot> autoProducts) {}
