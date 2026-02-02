package com.ryuqq.setof.adapter.out.persistence.brand.adapter;

import com.ryuqq.setof.adapter.out.persistence.brand.entity.BrandJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.brand.mapper.BrandJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.brand.repository.BrandQueryDslRepository;
import com.ryuqq.setof.application.brand.port.out.query.BrandQueryPort;
import com.ryuqq.setof.domain.brand.aggregate.Brand;
import com.ryuqq.setof.domain.brand.id.BrandId;
import com.ryuqq.setof.domain.brand.query.BrandSearchCriteria;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * BrandQueryAdapter - 브랜드 Query 어댑터.
 *
 * <p>BrandQueryPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 *
 * <p>PER-ADP-005: Entity -> Domain 변환 (Mapper 사용).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Component
public class BrandQueryAdapter implements BrandQueryPort {

    private final BrandQueryDslRepository queryDslRepository;
    private final BrandJpaEntityMapper mapper;

    /**
     * 생성자 주입.
     *
     * <p>PER-ADP-006: Mapper + QueryDslRepository 의존.
     *
     * @param queryDslRepository QueryDSL 레포지토리
     * @param mapper Entity-Domain 매퍼
     */
    public BrandQueryAdapter(
            BrandQueryDslRepository queryDslRepository, BrandJpaEntityMapper mapper) {
        this.queryDslRepository = queryDslRepository;
        this.mapper = mapper;
    }

    /**
     * ID로 브랜드 조회.
     *
     * @param id 브랜드 ID
     * @return 브랜드 Optional
     */
    @Override
    public Optional<Brand> findById(BrandId id) {
        return queryDslRepository.findById(id.value()).map(mapper::toDomain);
    }

    /**
     * ID 목록으로 브랜드 목록 조회.
     *
     * @param ids 브랜드 ID 목록
     * @return 브랜드 목록
     */
    @Override
    public List<Brand> findByIds(List<BrandId> ids) {
        List<Long> idValues = ids.stream().map(BrandId::value).toList();
        List<BrandJpaEntity> entities = queryDslRepository.findByIds(idValues);
        return entities.stream().map(mapper::toDomain).toList();
    }

    /**
     * ID 존재 여부 확인.
     *
     * @param id 브랜드 ID
     * @return 존재 여부
     */
    @Override
    public boolean existsById(BrandId id) {
        return queryDslRepository.existsById(id.value());
    }

    /**
     * 검색 조건으로 브랜드 목록 조회.
     *
     * @param criteria 검색 조건
     * @return 브랜드 목록
     */
    @Override
    public List<Brand> findByCriteria(BrandSearchCriteria criteria) {
        List<BrandJpaEntity> entities = queryDslRepository.findByCriteria(criteria);
        return entities.stream().map(mapper::toDomain).toList();
    }

    /**
     * 검색 조건으로 브랜드 개수 조회.
     *
     * @param criteria 검색 조건
     * @return 브랜드 개수
     */
    @Override
    public long countByCriteria(BrandSearchCriteria criteria) {
        return queryDslRepository.countByCriteria(criteria);
    }

    /**
     * 노출 중인 브랜드 전체 조회.
     *
     * @return 노출 중인 브랜드 목록
     */
    @Override
    public List<Brand> findAllDisplayed() {
        List<BrandJpaEntity> entities = queryDslRepository.findAllDisplayed();
        return entities.stream().map(mapper::toDomain).toList();
    }
}
