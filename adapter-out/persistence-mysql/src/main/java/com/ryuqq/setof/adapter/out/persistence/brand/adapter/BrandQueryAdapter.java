package com.ryuqq.setof.adapter.out.persistence.brand.adapter;

import com.ryuqq.setof.adapter.out.persistence.brand.mapper.BrandJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.brand.repository.BrandQueryDslRepository;
import com.ryuqq.setof.application.brand.port.out.query.BrandQueryPort;
import com.ryuqq.setof.domain.brand.aggregate.Brand;
import com.ryuqq.setof.domain.brand.query.criteria.BrandSearchCriteria;
import com.ryuqq.setof.domain.brand.vo.BrandCode;
import com.ryuqq.setof.domain.brand.vo.BrandId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * BrandQueryAdapter - Brand Query Adapter
 *
 * <p>CQRS의 Query(읽기) 담당으로, Brand 조회 요청을 QueryDslRepository에 위임하고 Mapper를 통해 Domain으로 변환하여 반환합니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>ID로 단건 조회 (findById)
 *   <li>브랜드 코드로 조회 (findByCode)
 *   <li>검색 조건으로 목록 조회 (findByCondition)
 *   <li>활성 브랜드 목록 조회 (findAllActive)
 *   <li>총 개수 조회 (countByCondition)
 *   <li>존재 여부 확인 (existsById, existsActiveById)
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>비즈니스 로직 금지
 *   <li>저장/수정/삭제 금지 (CommandAdapter로 분리)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class BrandQueryAdapter implements BrandQueryPort {

    private final BrandQueryDslRepository queryDslRepository;
    private final BrandJpaEntityMapper brandJpaEntityMapper;

    public BrandQueryAdapter(
            BrandQueryDslRepository queryDslRepository, BrandJpaEntityMapper brandJpaEntityMapper) {
        this.queryDslRepository = queryDslRepository;
        this.brandJpaEntityMapper = brandJpaEntityMapper;
    }

    /**
     * ID로 Brand 단건 조회
     *
     * @param id Brand ID (Value Object)
     * @return Brand Domain (Optional)
     */
    @Override
    public Optional<Brand> findById(BrandId id) {
        return queryDslRepository.findById(id.value()).map(brandJpaEntityMapper::toDomain);
    }

    /**
     * 브랜드 코드로 Brand 조회
     *
     * @param code 브랜드 코드 (Value Object)
     * @return Brand Domain (Optional)
     */
    @Override
    public Optional<Brand> findByCode(BrandCode code) {
        return queryDslRepository.findByCode(code.value()).map(brandJpaEntityMapper::toDomain);
    }

    /**
     * 검색 조건으로 Brand 목록 조회
     *
     * @param criteria 검색 조건 (Domain Criteria)
     * @return Brand 목록
     */
    @Override
    public List<Brand> findByCriteria(BrandSearchCriteria criteria) {
        return queryDslRepository.findByCondition(criteria).stream()
                .map(brandJpaEntityMapper::toDomain)
                .toList();
    }

    /**
     * 활성화된 모든 Brand 목록 조회
     *
     * @return Brand 목록 (nameKo 순)
     */
    @Override
    public List<Brand> findAllActive() {
        return queryDslRepository.findAllActive().stream()
                .map(brandJpaEntityMapper::toDomain)
                .toList();
    }

    /**
     * 검색 조건으로 총 개수 조회
     *
     * @param criteria 검색 조건 (Domain Criteria)
     * @return 총 개수
     */
    @Override
    public long countByCriteria(BrandSearchCriteria criteria) {
        return queryDslRepository.countByCondition(criteria);
    }

    /**
     * ID로 Brand 존재 여부 확인
     *
     * @param id Brand ID (Value Object)
     * @return 존재 여부
     */
    @Override
    public boolean existsById(BrandId id) {
        return queryDslRepository.existsById(id.value());
    }

    /**
     * 활성 Brand 존재 여부 확인
     *
     * @param brandId Brand ID (Long)
     * @return 활성 존재 여부
     */
    @Override
    public boolean existsActiveById(Long brandId) {
        return queryDslRepository.existsActiveById(brandId);
    }
}
