package com.ryuqq.setof.adapter.out.persistence.productgroupprice.adapter;

import com.ryuqq.setof.adapter.out.persistence.productgroupprice.entity.ProductGroupPriceJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productgroupprice.repository.ProductGroupPriceJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.productgroupprice.repository.ProductGroupPriceQueryDslRepository;
import com.ryuqq.setof.application.discount.port.out.command.ProductGroupPriceCommandPort;
import com.ryuqq.setof.domain.discount.dto.ProductGroupPriceUpdateData;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductGroupPriceCommandAdapter - 상품 그룹 가격 Command 어댑터.
 *
 * <p>ProductGroupPriceCommandPort를 구현하여 영속성 계층과 연결합니다.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ProductGroupPriceCommandAdapter implements ProductGroupPriceCommandPort {

    private final ProductGroupPriceJpaRepository jpaRepository;
    private final ProductGroupPriceQueryDslRepository queryDslRepository;

    public ProductGroupPriceCommandAdapter(
            ProductGroupPriceJpaRepository jpaRepository,
            ProductGroupPriceQueryDslRepository queryDslRepository) {
        this.jpaRepository = jpaRepository;
        this.queryDslRepository = queryDslRepository;
    }

    /**
     * 여러 상품그룹의 가격 정보를 일괄 갱신합니다.
     *
     * @param updates 갱신 대상 목록
     */
    @Override
    public void persistAll(List<ProductGroupPriceUpdateData> updates) {
        queryDslRepository.updatePrices(updates);
    }

    /**
     * 상품그룹 최초 가격 레코드를 기본값(0)으로 초기화합니다.
     *
     * @param productGroupId 상품그룹 ID
     */
    @Override
    public void initPrice(long productGroupId) {
        ProductGroupPriceJpaEntity entity =
                ProductGroupPriceJpaEntity.create(productGroupId, 0, 0, 0, 0);
        jpaRepository.save(entity);
    }
}
