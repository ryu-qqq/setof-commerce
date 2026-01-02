package com.ryuqq.setof.application.component.dto.command;

import com.ryuqq.setof.domain.cms.vo.ComponentId;

/**
 * Component 삭제 Command
 *
 * @param componentId 컴포넌트 ID
 * @author development-team
 * @since 1.0.0
 */
public record DeleteComponentCommand(ComponentId componentId) {}
