package com.ryuqq.setof.application.seller.factory.query;

import com.ryuqq.setof.application.seller.dto.query.SellerSearchQuery;
import com.ryuqq.setof.domain.seller.query.criteria.SellerSearchCriteria;
import org.springframework.stereotype.Component;

/**
 * SellerQueryFactory - 셀러 검색 조건 Criteria 생성 팩토리
 *
 * <p>Application Layer의 Query DTO를 Domain Layer의 Criteria로 변환합니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>Query DTO → Criteria 변환
 *   <li>검색 조건 정규화/검증
 *   <li>기본값 설정
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class SellerQueryFactory {

    /**
     * SellerSearchQuery로부터 Criteria 생성
     *
     * <p>기본 셀러 목록 조회 조건을 변환합니다.
     *
     * @param query 셀러 검색 Query DTO
     * @return SellerSearchCriteria 인스턴스
     */
    public SellerSearchCriteria create(SellerSearchQuery query) {
        return SellerSearchCriteria.of(
                query.sellerName(), query.approvalStatus(), query.page(), query.size());
    }

    /**
     * 간단한 조건으로 Criteria 생성
     *
     * @param sellerName 셀러명
     * @param approvalStatus 승인 상태
     * @param page 페이지 번호
     * @param pageSize 페이지 크기
     * @return SellerSearchCriteria 인스턴스
     */
    public SellerSearchCriteria create(
            String sellerName, String approvalStatus, int page, int pageSize) {
        return SellerSearchCriteria.of(sellerName, approvalStatus, page, pageSize);
    }
}
