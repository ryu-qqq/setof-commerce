package com.ryuqq.setof.storage.legacy.composite.content.adapter;

import com.ryuqq.setof.application.contentpage.port.out.ComponentAutoProductQueryPort;
import com.ryuqq.setof.domain.contentpage.vo.AutoProductCriteria;
import com.ryuqq.setof.domain.contentpage.vo.ProductThumbnailSnapshot;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebComponentProductQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.mapper.LegacyWebContentMapper;
import com.ryuqq.setof.storage.legacy.composite.content.repository.LegacyWebContentCompositeQueryDslRepository;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * LegacyWebAutoProductQueryAdapter - AUTO 상품 조회 Adapter.
 *
 * <p>category/brand 기반으로 product_group 테이블에서 동적 상품을 조회한다.
 *
 * <p>활성화 조건: persistence.legacy.content.enabled=true
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(name = "persistence.legacy.content.enabled", havingValue = "true")
public class LegacyWebAutoProductQueryAdapter implements ComponentAutoProductQueryPort {

    private final LegacyWebContentCompositeQueryDslRepository repository;
    private final LegacyWebContentMapper mapper;

    public LegacyWebAutoProductQueryAdapter(
            LegacyWebContentCompositeQueryDslRepository repository, LegacyWebContentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<ProductThumbnailSnapshot> fetchAutoProducts(AutoProductCriteria criteria) {
        List<LegacyWebComponentProductQueryDto> products =
                repository.fetchAutoProducts(
                        criteria.categoryIds(), criteria.brandIds(), criteria.limit());
        return products.stream().map(mapper::toProductThumbnailSnapshot).toList();
    }
}
