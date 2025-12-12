package com.ryuqq.setof.adapter.out.persistence.seller.adapter;

import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerCsInfoJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.seller.mapper.SellerJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.seller.repository.SellerQueryDslRepository;
import com.ryuqq.setof.application.seller.port.out.query.SellerQueryPort;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.vo.SellerId;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * SellerQueryAdapter - Seller Query Adapter
 *
 * <p>CQRS의 Query(읽기) 담당으로, Seller 조회 요청을 QueryDslRepository에 위임하고 Mapper를 통해 Domain으로 변환하여 반환합니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>ID로 단건 조회 (findById)
 *   <li>검색 조건으로 목록 조회 (findByConditions)
 *   <li>총 개수 조회 (countByConditions)
 *   <li>존재 여부 확인 (existsById, existsActiveById)
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>비즈니스 로직 금지
 *   <li>저장/수정/삭제 금지 (PersistenceAdapter로 분리)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class SellerQueryAdapter implements SellerQueryPort {

    private final SellerQueryDslRepository queryDslRepository;
    private final SellerJpaEntityMapper sellerJpaEntityMapper;

    public SellerQueryAdapter(
            SellerQueryDslRepository queryDslRepository,
            SellerJpaEntityMapper sellerJpaEntityMapper) {
        this.queryDslRepository = queryDslRepository;
        this.sellerJpaEntityMapper = sellerJpaEntityMapper;
    }

    /**
     * ID로 Seller 단건 조회
     *
     * @param id Seller ID (Value Object)
     * @return Seller Domain (Optional)
     */
    @Override
    public Optional<Seller> findById(SellerId id) {
        Optional<SellerJpaEntity> sellerEntity = queryDslRepository.findById(id.value());

        if (sellerEntity.isEmpty()) {
            return Optional.empty();
        }

        SellerCsInfoJpaEntity csInfoEntity =
                queryDslRepository.findCsInfoBySellerId(id.value()).orElse(null);

        return Optional.of(sellerJpaEntityMapper.toDomain(sellerEntity.get(), csInfoEntity));
    }

    /**
     * 셀러명으로 검색
     *
     * @param sellerName 셀러명 (LIKE 검색)
     * @param approvalStatus 승인 상태 (nullable)
     * @param offset 오프셋
     * @param limit 개수 제한
     * @return Seller 목록
     */
    @Override
    public List<Seller> findByConditions(
            String sellerName, String approvalStatus, int offset, int limit) {
        List<SellerJpaEntity> sellerEntities =
                queryDslRepository.findByCondition(
                        sellerName, approvalStatus, false, offset, limit);

        return sellerEntities.stream()
                .map(
                        sellerEntity -> {
                            SellerCsInfoJpaEntity csInfoEntity =
                                    queryDslRepository
                                            .findCsInfoBySellerId(sellerEntity.getId())
                                            .orElse(null);
                            return sellerJpaEntityMapper.toDomain(sellerEntity, csInfoEntity);
                        })
                .toList();
    }

    /**
     * 검색 조건에 맞는 셀러 총 개수 조회
     *
     * @param sellerName 셀러명 (LIKE 검색)
     * @param approvalStatus 승인 상태 (nullable)
     * @return 셀러 총 개수
     */
    @Override
    public long countByConditions(String sellerName, String approvalStatus) {
        return queryDslRepository.countByCondition(sellerName, approvalStatus, false);
    }

    /**
     * ID로 Seller 존재 여부 확인
     *
     * @param id Seller ID (Value Object)
     * @return 존재 여부
     */
    @Override
    public boolean existsById(SellerId id) {
        return queryDslRepository.existsById(id.value());
    }

    /**
     * 활성 셀러 존재 여부 확인
     *
     * @param sellerId Seller ID (Long)
     * @return 존재 여부
     */
    @Override
    public boolean existsActiveById(Long sellerId) {
        return queryDslRepository.existsById(sellerId);
    }
}
