package com.ryuqq.setof.application.order.factory.query;

import com.ryuqq.setof.application.order.dto.query.GetAdminOrdersQuery;
import com.ryuqq.setof.application.order.dto.query.GetOrdersQuery;
import com.ryuqq.setof.domain.order.query.criteria.OrderSearchCriteria;
import org.springframework.stereotype.Component;

/**
 * OrderQueryFactory - 주문 검색 조건 Criteria 생성 팩토리
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
 * <p><strong>지원 Query 타입:</strong>
 *
 * <ul>
 *   <li>GetOrdersQuery - 회원별 주문 목록 조회 (User API)
 *   <li>GetAdminOrdersQuery - 셀러별 주문 목록 조회 (Admin API)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class OrderQueryFactory {

    /**
     * GetOrdersQuery로부터 Criteria 생성 (회원용)
     *
     * <p>User API에서 사용하는 회원별 주문 목록 조회 조건을 변환합니다.
     *
     * @param query 회원별 주문 조회 Query DTO
     * @return OrderSearchCriteria (memberId 기반)
     */
    public OrderSearchCriteria create(GetOrdersQuery query) {
        return OrderSearchCriteria.ofMember(
                query.memberId(),
                query.orderStatuses(),
                query.startDate(),
                query.endDate(),
                query.sortBy(),
                query.sortDirection(),
                query.lastOrderId(),
                query.pageSize());
    }

    /**
     * GetAdminOrdersQuery로부터 Criteria 생성 (관리자용)
     *
     * <p>Admin API에서 사용하는 셀러별 주문 목록 조회 조건을 변환합니다.
     *
     * @param query 관리자용 주문 조회 Query DTO
     * @return OrderSearchCriteria (sellerId 기반)
     */
    public OrderSearchCriteria create(GetAdminOrdersQuery query) {
        return OrderSearchCriteria.ofAdmin(
                query.sellerId(),
                query.orderStatuses(),
                query.searchKeyword(),
                query.startDate(),
                query.endDate(),
                query.sortBy(),
                query.sortDirection(),
                query.lastOrderId(),
                query.pageSize());
    }

    /**
     * 회원 ID로 기본 Criteria 생성
     *
     * <p>간단한 회원별 주문 조회 시 사용합니다.
     *
     * @param memberId 회원 ID
     * @param pageSize 페이지 크기
     * @return OrderSearchCriteria (기본 정렬, 필터 없음)
     */
    public OrderSearchCriteria createForMember(String memberId, int pageSize) {
        return OrderSearchCriteria.ofMember(memberId, null, null, null, null, pageSize);
    }

    /**
     * 셀러 ID로 기본 Criteria 생성
     *
     * <p>간단한 셀러별 주문 조회 시 사용합니다.
     *
     * @param sellerId 셀러 ID
     * @param pageSize 페이지 크기
     * @return OrderSearchCriteria (기본 정렬, 필터 없음)
     */
    public OrderSearchCriteria createForSeller(Long sellerId, int pageSize) {
        return OrderSearchCriteria.ofAdmin(
                sellerId, null, null, null, null, null, null, null, pageSize);
    }
}
