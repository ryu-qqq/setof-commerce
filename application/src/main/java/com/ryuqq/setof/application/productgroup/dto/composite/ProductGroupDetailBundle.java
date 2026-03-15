package com.ryuqq.setof.application.productgroup.dto.composite;

import com.ryuqq.setof.application.productgroupdescription.dto.response.DescriptionImageResult;
import com.ryuqq.setof.application.productnotice.dto.response.ProductNoticeEntryResult;
import com.ryuqq.setof.domain.product.aggregate.Product;
import com.ryuqq.setof.domain.productgroup.aggregate.ProductGroup;
import java.util.List;

/**
 * 상품 그룹 상세 조회 번들 DTO.
 *
 * <p>ReadFacade에서 조회한 모든 데이터를 묶어 Service로 전달합니다. Service는 이 번들을 Assembler에 넘겨 최종 결과를 조립합니다.
 *
 * <p>1:1 관계(Description header, Notice header, ShippingPolicy, RefundPolicy)는 queryResult에 통합되어 있고,
 * 1:N 관계(Images+Variants, NoticeEntries, DescriptionImages, Products)는 별도 배치 조회 결과입니다.
 *
 * @param queryResult Composition 쿼리 결과 (1:1 통합: 기본 정보 + 정책 + description/notice 헤더)
 * @param imageResults 이미지 + Variant URL 래핑 객체
 * @param group ProductGroup Aggregate (옵션 그룹 구조용)
 * @param products 상품 목록
 * @param noticeEntries 고시정보 항목 목록
 * @param descriptionImages 상세설명 이미지 목록
 */
public record ProductGroupDetailBundle(
        ProductGroupDetailCompositeQueryResult queryResult,
        ProductGroupDetailImageResults imageResults,
        ProductGroup group,
        List<Product> products,
        List<ProductNoticeEntryResult> noticeEntries,
        List<DescriptionImageResult> descriptionImages) {

    public ProductGroupDetailBundle {
        noticeEntries = noticeEntries != null ? List.copyOf(noticeEntries) : List.of();
        descriptionImages = descriptionImages != null ? List.copyOf(descriptionImages) : List.of();
    }
}
