package com.ryuqq.setof.adapter.out.persistence.composite.content.adapter;

import com.ryuqq.setof.adapter.out.persistence.composite.content.dto.FixedProductThumbnailDto;
import com.ryuqq.setof.adapter.out.persistence.composite.content.mapper.ContentCompositeMapper;
import com.ryuqq.setof.adapter.out.persistence.composite.content.repository.ContentCompositeQueryDslRepository;
import com.ryuqq.setof.application.contentpage.port.out.ComponentFixedProductQueryPort;
import com.ryuqq.setof.domain.contentpage.vo.ProductThumbnailSnapshot;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * ComponentFixedProductQueryAdapter - FIXED 상품 조회 신규 Adapter.
 *
 * <p>setof DB에서 크로스 도메인 JOIN으로 고정 배치 상품을 조회한다.
 *
 * <p>활성화 조건: persistence.legacy.content.enabled=false
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(name = "persistence.legacy.content.enabled", havingValue = "false")
public class ComponentFixedProductQueryAdapter implements ComponentFixedProductQueryPort {

    private final ContentCompositeQueryDslRepository compositeRepository;
    private final ContentCompositeMapper mapper;

    public ComponentFixedProductQueryAdapter(
            ContentCompositeQueryDslRepository compositeRepository, ContentCompositeMapper mapper) {
        this.compositeRepository = compositeRepository;
        this.mapper = mapper;
    }

    @Override
    public Map<Long, List<ProductThumbnailSnapshot>> fetchFixedProducts(List<Long> componentIds) {
        if (componentIds == null || componentIds.isEmpty()) {
            return Map.of();
        }

        List<FixedProductThumbnailDto> dtos =
                compositeRepository.fetchFixedProductThumbnails(componentIds);

        return dtos.stream()
                .filter(dto -> dto.tabId() == null || dto.tabId() == 0L)
                .collect(
                        Collectors.groupingBy(
                                FixedProductThumbnailDto::componentId,
                                Collectors.mapping(mapper::toSnapshot, Collectors.toList())));
    }

    @Override
    public Map<Long, List<ProductThumbnailSnapshot>> fetchFixedProductsByTab(
            List<Long> componentIds) {
        if (componentIds == null || componentIds.isEmpty()) {
            return Map.of();
        }

        List<FixedProductThumbnailDto> dtos =
                compositeRepository.fetchFixedProductThumbnails(componentIds);

        return dtos.stream()
                .filter(dto -> dto.tabId() != null && dto.tabId() != 0L)
                .collect(
                        Collectors.groupingBy(
                                FixedProductThumbnailDto::tabId,
                                Collectors.mapping(mapper::toSnapshot, Collectors.toList())));
    }
}
