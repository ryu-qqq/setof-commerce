package com.ryuqq.setof.adapter.out.persistence.productnotice.adapter;

import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.ProductNoticeItemJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productnotice.entity.ProductNoticeJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.productnotice.mapper.ProductNoticeJpaEntityMapper;
import com.ryuqq.setof.adapter.out.persistence.productnotice.repository.ProductNoticeItemJpaRepository;
import com.ryuqq.setof.adapter.out.persistence.productnotice.repository.ProductNoticeJpaRepository;
import com.ryuqq.setof.application.productnotice.port.out.command.ProductNoticePersistencePort;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ProductNoticePersistenceAdapter - ProductNotice Persistence Adapter
 *
 * <p>CQRS의 Command(쓰기) 담당으로, ProductNotice 저장 요청을 JpaRepository에 위임합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ProductNoticePersistenceAdapter implements ProductNoticePersistencePort {

    private final ProductNoticeJpaRepository jpaRepository;
    private final ProductNoticeItemJpaRepository itemJpaRepository;
    private final ProductNoticeJpaEntityMapper mapper;

    public ProductNoticePersistenceAdapter(
            ProductNoticeJpaRepository jpaRepository,
            ProductNoticeItemJpaRepository itemJpaRepository,
            ProductNoticeJpaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.itemJpaRepository = itemJpaRepository;
        this.mapper = mapper;
    }

    /**
     * ProductNotice 저장 (신규 생성)
     *
     * @param productNotice 저장할 상품고시
     * @return 저장된 상품고시 ID
     */
    @Override
    public Long persist(ProductNotice productNotice) {
        // 1. 메인 Entity 저장
        ProductNoticeJpaEntity entity = mapper.toEntity(productNotice);
        ProductNoticeJpaEntity savedEntity = jpaRepository.save(entity);

        // 2. 항목 Entity 저장
        saveItems(productNotice, savedEntity.getId());

        return savedEntity.getId();
    }

    /**
     * ProductNotice 수정
     *
     * @param productNotice 수정할 상품고시
     */
    @Override
    public void update(ProductNotice productNotice) {
        // 1. 메인 Entity 저장
        ProductNoticeJpaEntity entity = mapper.toEntity(productNotice);
        jpaRepository.save(entity);

        // 2. 기존 항목 삭제 후 새로 저장
        Long productNoticeId = productNotice.getIdValue();
        itemJpaRepository.deleteByProductNoticeId(productNoticeId);
        saveItems(productNotice, productNoticeId);
    }

    /**
     * 항목 저장
     *
     * @param productNotice 상품고시
     * @param productNoticeId 상품고시 ID
     */
    private void saveItems(ProductNotice productNotice, Long productNoticeId) {
        List<ProductNoticeItemJpaEntity> itemEntities =
                mapper.toItemEntities(productNotice.getItems(), productNoticeId);
        if (!itemEntities.isEmpty()) {
            itemJpaRepository.saveAll(itemEntities);
        }
    }
}
