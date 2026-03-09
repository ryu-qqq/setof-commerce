package com.ryuqq.setof.storage.legacy.composite.content.adapter;

import com.ryuqq.setof.application.contentpage.port.out.ComponentFixedProductQueryPort;
import com.ryuqq.setof.domain.contentpage.vo.ProductThumbnailSnapshot;
import com.ryuqq.setof.storage.legacy.composite.content.dto.LegacyWebComponentProductQueryDto;
import com.ryuqq.setof.storage.legacy.composite.content.mapper.LegacyWebContentMapper;
import com.ryuqq.setof.storage.legacy.composite.content.repository.LegacyWebContentCompositeQueryDslRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * LegacyWebFixedProductQueryAdapter - FIXED 상품 조회 Adapter.
 *
 * <p>component_target → component_item 경로로 고정 배치된 상품을 조회한다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebFixedProductQueryAdapter implements ComponentFixedProductQueryPort {

    private final LegacyWebContentCompositeQueryDslRepository repository;
    private final LegacyWebContentMapper mapper;

    public LegacyWebFixedProductQueryAdapter(
            LegacyWebContentCompositeQueryDslRepository repository, LegacyWebContentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Map<Long, List<ProductThumbnailSnapshot>> fetchFixedProducts(List<Long> componentIds) {
        if (componentIds == null || componentIds.isEmpty()) {
            return Map.of();
        }
        List<LegacyWebComponentProductQueryDto> products =
                repository.fetchComponentProducts(componentIds);
        return products.stream()
                .filter(dto -> dto.tabId() == 0L)
                .collect(
                        Collectors.groupingBy(
                                LegacyWebComponentProductQueryDto::componentId,
                                Collectors.mapping(
                                        mapper::toProductThumbnailSnapshot, Collectors.toList())));
    }

    @Override
    public Map<Long, List<ProductThumbnailSnapshot>> fetchFixedProductsByTab(
            List<Long> componentIds) {
        if (componentIds == null || componentIds.isEmpty()) {
            return Map.of();
        }
        List<LegacyWebComponentProductQueryDto> products =
                repository.fetchComponentProducts(componentIds);
        return products.stream()
                .filter(dto -> dto.tabId() != 0L)
                .collect(
                        Collectors.groupingBy(
                                LegacyWebComponentProductQueryDto::tabId,
                                Collectors.mapping(
                                        mapper::toProductThumbnailSnapshot, Collectors.toList())));
    }
}
