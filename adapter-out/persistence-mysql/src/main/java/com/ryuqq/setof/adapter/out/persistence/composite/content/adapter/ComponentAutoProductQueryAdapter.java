package com.ryuqq.setof.adapter.out.persistence.composite.content.adapter;

import com.ryuqq.setof.adapter.out.persistence.composite.content.mapper.ContentCompositeMapper;
import com.ryuqq.setof.adapter.out.persistence.composite.content.repository.ContentCompositeQueryDslRepository;
import com.ryuqq.setof.application.contentpage.port.out.ComponentAutoProductQueryPort;
import com.ryuqq.setof.domain.contentpage.vo.AutoProductCriteria;
import com.ryuqq.setof.domain.contentpage.vo.ProductThumbnailSnapshot;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * ComponentAutoProductQueryAdapter - AUTO 상품 조회 신규 Adapter.
 *
 * <p>setof DB에서 크로스 도메인 JOIN으로 카테고리/브랜드 기반 동적 상품을 조회한다.
 *
 * <p>활성화 조건: persistence.legacy.content.enabled=false
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(name = "persistence.legacy.content.enabled", havingValue = "false")
public class ComponentAutoProductQueryAdapter implements ComponentAutoProductQueryPort {

    private final ContentCompositeQueryDslRepository compositeRepository;
    private final ContentCompositeMapper mapper;

    public ComponentAutoProductQueryAdapter(
            ContentCompositeQueryDslRepository compositeRepository, ContentCompositeMapper mapper) {
        this.compositeRepository = compositeRepository;
        this.mapper = mapper;
    }

    @Override
    public List<ProductThumbnailSnapshot> fetchAutoProducts(AutoProductCriteria criteria) {
        return compositeRepository
                .fetchAutoProductThumbnails(
                        criteria.categoryIds(), criteria.brandIds(), criteria.limit())
                .stream()
                .map(mapper::toSnapshot)
                .toList();
    }
}
