package com.ryuqq.setof.application.component.service.query;

import com.ryuqq.setof.application.component.assembler.ComponentAssembler;
import com.ryuqq.setof.application.component.dto.response.ComponentResponse;
import com.ryuqq.setof.application.component.manager.query.ComponentReadManager;
import com.ryuqq.setof.application.component.port.in.query.GetComponentUseCase;
import com.ryuqq.setof.application.component.port.in.query.GetComponentsByContentUseCase;
import com.ryuqq.setof.domain.cms.aggregate.component.Component;
import com.ryuqq.setof.domain.cms.vo.ComponentId;
import com.ryuqq.setof.domain.cms.vo.ContentId;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Component 조회 Service
 *
 * <p>컴포넌트 단건/목록 조회를 담당합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class ComponentQueryService implements GetComponentUseCase, GetComponentsByContentUseCase {

    private final ComponentReadManager componentReadManager;
    private final ComponentAssembler componentAssembler;

    public ComponentQueryService(
            ComponentReadManager componentReadManager, ComponentAssembler componentAssembler) {
        this.componentReadManager = componentReadManager;
        this.componentAssembler = componentAssembler;
    }

    @Override
    public ComponentResponse execute(ComponentId componentId) {
        Component component = componentReadManager.findById(componentId);
        return componentAssembler.toResponse(component);
    }

    @Override
    public List<ComponentResponse> getByContentId(ContentId contentId) {
        List<Component> components = componentReadManager.findByContentId(contentId);
        return componentAssembler.toResponses(components);
    }
}
