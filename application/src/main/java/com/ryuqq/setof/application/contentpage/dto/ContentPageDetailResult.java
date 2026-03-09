package com.ryuqq.setof.application.contentpage.dto;

import com.ryuqq.setof.domain.contentpage.aggregate.ContentPage;
import com.ryuqq.setof.domain.contentpage.aggregate.DisplayComponent;
import com.ryuqq.setof.domain.contentpage.vo.ComponentProductBundle;
import java.util.List;

/**
 * ContentPageDetailResult - 콘텐츠 페이지 상세 조회 결과.
 *
 * <p>ContentPage 메타 + DisplayComponent 목록 + 상품 썸네일 번들을 조합한 ReadFacade 결과 객체.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ContentPageDetailResult(
        ContentPage contentPage,
        List<DisplayComponent> displayComponents,
        ComponentProductBundle productBundle) {}
