package com.ryuqq.setof.application.brand.port.out.query;

import com.ryuqq.setof.domain.brand.aggregate.Brand;
import com.ryuqq.setof.domain.brand.query.criteria.BrandSearchCriteria;
import com.ryuqq.setof.domain.brand.vo.BrandCode;
import com.ryuqq.setof.domain.brand.vo.BrandId;
import java.util.List;
import java.util.Optional;

/**
 * Brand Query Port (Query)
 *
 * <p>Brand Aggregate를 조회하는 읽기 전용 Port
 *
 * @author development-team
 * @since 1.0.0
 */
public interface BrandQueryPort {

    /**
     * ID로 Brand 단건 조회
     *
     * @param id Brand ID (Value Object)
     * @return Brand Domain (Optional)
     */
    Optional<Brand> findById(BrandId id);

    /**
     * 브랜드 코드로 Brand 단건 조회
     *
     * @param brandCode 브랜드 코드 (Value Object)
     * @return Brand Domain (Optional)
     */
    Optional<Brand> findByCode(BrandCode brandCode);

    /**
     * 활성화된 모든 브랜드 목록 조회
     *
     * @return 활성 Brand 목록
     */
    List<Brand> findAllActive();

    /**
     * 검색 조건으로 브랜드 목록 조회
     *
     * @param criteria 검색 조건 (Domain Criteria)
     * @return Brand 목록
     */
    List<Brand> findByCriteria(BrandSearchCriteria criteria);

    /**
     * 검색 조건에 맞는 브랜드 총 개수 조회
     *
     * @param criteria 검색 조건 (Domain Criteria)
     * @return 브랜드 총 개수
     */
    long countByCriteria(BrandSearchCriteria criteria);

    /**
     * Brand ID 존재 여부 확인
     *
     * @param id Brand ID
     * @return 존재 여부
     */
    boolean existsById(BrandId id);

    /**
     * Brand ID로 활성 브랜드 존재 여부 확인
     *
     * @param brandId Brand ID (Long)
     * @return 존재 여부
     */
    boolean existsActiveById(Long brandId);
}
