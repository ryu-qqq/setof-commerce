package com.ryuqq.setof.application.product.factory.query;

import com.ryuqq.setof.domain.product.query.criteria.ProductGroupSearchCriteria;
import com.ryuqq.setof.domain.product.vo.ProductSearchPeriod;
import com.ryuqq.setof.domain.product.vo.ProductSortType;
import org.springframework.stereotype.Component;

/**
 * ProductGroupQueryFactory - 검색 조건 Criteria 생성 팩토리
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
public class ProductGroupQueryFactory {

    /**
     * 개별 파라미터로부터 Criteria 생성 (하위 호환용)
     *
     * @param sellerId 셀러 ID
     * @param categoryId 카테고리 ID
     * @param brandId 브랜드 ID
     * @param name 상품그룹명
     * @param status 상태
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return ProductGroupSearchCriteria
     */
    public ProductGroupSearchCriteria create(
            Long sellerId,
            Long categoryId,
            Long brandId,
            String name,
            String status,
            int page,
            int size) {
        return ProductGroupSearchCriteria.of(
                sellerId, categoryId, brandId, name, status, page, size);
    }

    /**
     * 개별 파라미터로부터 Criteria 생성 (전체 조건)
     *
     * @param sellerId 셀러 ID
     * @param categoryId 카테고리 ID
     * @param brandId 브랜드 ID
     * @param name 상품그룹명
     * @param status 상태
     * @param searchPeriod 검색 기간
     * @param sortType 정렬 기준
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return ProductGroupSearchCriteria
     */
    public ProductGroupSearchCriteria create(
            Long sellerId,
            Long categoryId,
            Long brandId,
            String name,
            String status,
            ProductSearchPeriod searchPeriod,
            ProductSortType sortType,
            int page,
            int size) {
        return ProductGroupSearchCriteria.of(
                sellerId, categoryId, brandId, name, status, searchPeriod, sortType, page, size);
    }

    /**
     * 셀러별 조회용 Criteria 생성
     *
     * @param sellerId 셀러 ID
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return ProductGroupSearchCriteria
     */
    public ProductGroupSearchCriteria createForSeller(Long sellerId, int page, int size) {
        return ProductGroupSearchCriteria.ofSeller(sellerId, page, size);
    }

    /**
     * 카테고리별 조회용 Criteria 생성
     *
     * @param categoryId 카테고리 ID
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return ProductGroupSearchCriteria
     */
    public ProductGroupSearchCriteria createForCategory(Long categoryId, int page, int size) {
        return ProductGroupSearchCriteria.ofCategory(categoryId, page, size);
    }

    /**
     * 브랜드별 조회용 Criteria 생성
     *
     * @param brandId 브랜드 ID
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return ProductGroupSearchCriteria
     */
    public ProductGroupSearchCriteria createForBrand(Long brandId, int page, int size) {
        return ProductGroupSearchCriteria.ofBrand(brandId, page, size);
    }

    /**
     * 이름 검색용 Criteria 생성
     *
     * @param name 상품그룹명
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return ProductGroupSearchCriteria
     */
    public ProductGroupSearchCriteria createForName(String name, int page, int size) {
        return ProductGroupSearchCriteria.ofName(name, page, size);
    }

    /**
     * 기간 검색용 Criteria 생성
     *
     * @param searchPeriod 검색 기간
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return ProductGroupSearchCriteria
     */
    public ProductGroupSearchCriteria createForPeriod(
            ProductSearchPeriod searchPeriod, int page, int size) {
        return ProductGroupSearchCriteria.ofPeriod(searchPeriod, page, size);
    }
}
