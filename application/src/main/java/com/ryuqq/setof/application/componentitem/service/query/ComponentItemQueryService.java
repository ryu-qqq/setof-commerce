package com.ryuqq.setof.application.componentitem.service.query;

import com.ryuqq.setof.application.componentitem.assembler.ComponentItemAssembler;
import com.ryuqq.setof.application.componentitem.dto.response.ComponentItemResponse;
import com.ryuqq.setof.application.componentitem.manager.query.ComponentItemReadManager;
import com.ryuqq.setof.application.componentitem.port.in.query.GetComponentItemsUseCase;
import com.ryuqq.setof.domain.cms.aggregate.component.ComponentItem;
import com.ryuqq.setof.domain.cms.vo.ComponentId;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ComponentItemQueryService - ComponentItem 조회 서비스
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class ComponentItemQueryService implements GetComponentItemsUseCase {

    private final ComponentItemReadManager readManager;
    private final ComponentItemAssembler assembler;

    public ComponentItemQueryService(
            ComponentItemReadManager readManager, ComponentItemAssembler assembler) {
        this.readManager = readManager;
        this.assembler = assembler;
    }

    @Override
    public List<ComponentItemResponse> getActiveByComponentId(ComponentId componentId) {
        List<ComponentItem> items = readManager.findActiveByComponentId(componentId);
        return assembler.toResponseList(items);
    }

    @Override
    public List<ComponentItemResponse> getActiveByComponentIds(List<ComponentId> componentIds) {
        List<ComponentItem> items = readManager.findActiveByComponentIds(componentIds);
        return assembler.toResponseList(items);
    }
}
