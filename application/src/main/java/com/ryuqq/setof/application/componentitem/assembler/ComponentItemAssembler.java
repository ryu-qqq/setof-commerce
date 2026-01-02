package com.ryuqq.setof.application.componentitem.assembler;

import com.ryuqq.setof.application.componentitem.dto.response.ComponentItemResponse;
import com.ryuqq.setof.domain.cms.aggregate.component.ComponentItem;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ComponentItemAssembler - Domain → Response DTO 변환
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ComponentItemAssembler {

    /**
     * Domain → Response DTO 변환
     *
     * @param domain ComponentItem Domain
     * @return ComponentItemResponse
     */
    public ComponentItemResponse toResponse(ComponentItem domain) {
        return new ComponentItemResponse(
                domain.id() != null ? domain.id().value() : null,
                domain.componentId().value(),
                domain.itemType().name(),
                domain.referenceId(),
                domain.title() != null ? domain.title().value() : null,
                domain.imageUrl() != null ? domain.imageUrl().value() : null,
                domain.linkUrl() != null ? domain.linkUrl().value() : null,
                domain.displayOrder() != null ? domain.displayOrder().value() : 0,
                domain.status().name(),
                domain.sortType() != null ? domain.sortType().name() : null,
                domain.createdAt());
    }

    /**
     * Domain 목록 → Response DTO 목록 변환
     *
     * @param domains ComponentItem Domain 목록
     * @return ComponentItemResponse 목록
     */
    public List<ComponentItemResponse> toResponseList(List<ComponentItem> domains) {
        return domains.stream().map(this::toResponse).toList();
    }
}
