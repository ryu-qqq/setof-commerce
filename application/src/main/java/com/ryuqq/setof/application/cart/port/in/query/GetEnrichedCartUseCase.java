package com.ryuqq.setof.application.cart.port.in.query;

import com.ryuqq.setof.application.cart.dto.response.EnrichedCartResponse;
import java.util.UUID;

/**
 * Enriched 장바구니 조회 UseCase
 *
 * <p>상품 상세 정보가 포함된 장바구니를 조회합니다. V1 Legacy API 호환성을 위해 생성되었습니다.
 *
 * <p>조회되는 상품 상세 정보:
 *
 * <ul>
 *   <li>상품그룹명
 *   <li>브랜드명
 *   <li>셀러명
 *   <li>옵션값
 *   <li>대표 이미지 URL
 *   <li>재고 수량
 *   <li>카테고리 정보
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
public interface GetEnrichedCartUseCase {

    /**
     * 상품 상세 정보가 포함된 장바구니 조회
     *
     * @param memberId 회원 ID
     * @return Enriched 장바구니 응답
     */
    EnrichedCartResponse getEnrichedCart(UUID memberId);
}
