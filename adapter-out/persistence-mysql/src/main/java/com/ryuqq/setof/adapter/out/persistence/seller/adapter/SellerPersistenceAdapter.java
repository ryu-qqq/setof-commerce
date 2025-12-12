package com.ryuqq.setof.adapter.out.persistence.seller.adapter;

import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerCsInfoJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.seller.entity.SellerJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.seller.mapper.SellerJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.seller.repository.SellerCsInfoJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.seller.repository.SellerJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.seller.repository.SellerQueryDslRepository;
import com.ryuqq.setof.application.seller.port.out.command.SellerPersistencePort;
import com.ryuqq.setof.domain.seller.aggregate.Seller;
import com.ryuqq.setof.domain.seller.vo.SellerId;
import org.springframework.stereotype.Component;

/**
 * SellerPersistenceAdapter - Seller Persistence Adapter
 *
 * <p>CQRS의 Command(쓰기) 담당으로, Seller 저장 요청을 JpaRepository에 위임합니다.
 *
 * <p><strong>책임:</strong>
 *
 * <ul>
 *   <li>Seller 저장 (persist)
 *   <li>SellerJpaEntity + SellerCsInfoJpaEntity 함께 저장
 * </ul>
 *
 * <p><strong>금지 사항:</strong>
 *
 * <ul>
 *   <li>비즈니스 로직 금지
 *   <li>조회 로직 금지 (QueryAdapter로 분리)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class SellerPersistenceAdapter implements SellerPersistencePort {

    private final SellerJpaRepository sellerJpaRepository;
    private final SellerCsInfoJpaRepository csInfoJpaRepository;
    private final SellerQueryDslRepository queryDslRepository;
    private final SellerJpaEntityMapper sellerJpaEntityMapper;

    public SellerPersistenceAdapter(
            SellerJpaRepository sellerJpaRepository,
            SellerCsInfoJpaRepository csInfoJpaRepository,
            SellerQueryDslRepository queryDslRepository,
            SellerJpaEntityMapper sellerJpaEntityMapper) {
        this.sellerJpaRepository = sellerJpaRepository;
        this.csInfoJpaRepository = csInfoJpaRepository;
        this.queryDslRepository = queryDslRepository;
        this.sellerJpaEntityMapper = sellerJpaEntityMapper;
    }

    /**
     * Seller 저장 (생성/수정)
     *
     * <p>Seller와 CS Info를 함께 저장합니다.
     *
     * @param seller Seller 도메인
     * @return 저장된 SellerId
     */
    @Override
    public SellerId persist(Seller seller) {
        // Seller Entity 저장
        SellerJpaEntity sellerEntity = sellerJpaEntityMapper.toSellerEntity(seller);
        SellerJpaEntity savedSellerEntity = sellerJpaRepository.save(sellerEntity);

        // CS Info Entity 저장 (기존 ID 조회)
        Long existingCsInfoId = findExistingCsInfoId(savedSellerEntity.getId());
        SellerCsInfoJpaEntity csInfoEntity =
                sellerJpaEntityMapper.toCsInfoEntity(seller, existingCsInfoId);

        // 신규 Seller인 경우 sellerId 설정
        if (existingCsInfoId == null) {
            csInfoEntity =
                    SellerCsInfoJpaEntity.of(
                            null,
                            savedSellerEntity.getId(),
                            seller.getCsEmail(),
                            seller.getCsMobilePhone(),
                            seller.getCsLandlinePhone(),
                            seller.getCreatedAt(),
                            seller.getUpdatedAt());
        }

        csInfoJpaRepository.save(csInfoEntity);

        return SellerId.of(savedSellerEntity.getId());
    }

    /**
     * 기존 CS Info ID 조회
     *
     * @param sellerId Seller ID
     * @return CS Info ID (없으면 null)
     */
    private Long findExistingCsInfoId(Long sellerId) {
        return queryDslRepository
                .findCsInfoBySellerId(sellerId)
                .map(SellerCsInfoJpaEntity::getId)
                .orElse(null);
    }
}
