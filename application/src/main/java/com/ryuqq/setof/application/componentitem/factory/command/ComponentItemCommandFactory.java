package com.ryuqq.setof.application.componentitem.factory.command;

import com.ryuqq.setof.application.componentitem.dto.command.CreateComponentItemCommand;
import com.ryuqq.setof.domain.cms.aggregate.component.ComponentItem;
import com.ryuqq.setof.domain.cms.vo.ComponentId;
import com.ryuqq.setof.domain.cms.vo.ComponentItemType;
import com.ryuqq.setof.domain.cms.vo.ContentTitle;
import com.ryuqq.setof.domain.cms.vo.DisplayOrder;
import com.ryuqq.setof.domain.cms.vo.ImageUrl;
import com.ryuqq.setof.domain.cms.vo.SortType;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * ComponentItemCommandFactory - Command → Domain 변환
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class ComponentItemCommandFactory {

    private final ClockHolder clockHolder;

    public ComponentItemCommandFactory(ClockHolder clockHolder) {
        this.clockHolder = clockHolder;
    }

    /**
     * CreateCommand → Domain 변환
     *
     * @param command 생성 명령
     * @return ComponentItem Domain
     */
    public ComponentItem toDomain(CreateComponentItemCommand command) {
        return ComponentItem.forNew(
                ComponentId.of(command.componentId()),
                ComponentItemType.from(command.itemType()),
                command.referenceId(),
                command.title() != null ? ContentTitle.of(command.title()) : null,
                command.imageUrl() != null ? ImageUrl.of(command.imageUrl()) : null,
                command.linkUrl() != null ? ImageUrl.of(command.linkUrl()) : null,
                command.displayOrder() != null
                        ? DisplayOrder.of(command.displayOrder())
                        : DisplayOrder.DEFAULT,
                parseSortType(command.sortType()),
                clockHolder.getClock());
    }

    /**
     * CreateCommand 목록 → Domain 목록 변환
     *
     * @param commands 생성 명령 목록
     * @return ComponentItem Domain 목록
     */
    public List<ComponentItem> toDomainList(List<CreateComponentItemCommand> commands) {
        return commands.stream().map(this::toDomain).toList();
    }

    /** SortType 문자열 파싱 (nullable) */
    private SortType parseSortType(String sortType) {
        if (sortType == null || sortType.isBlank()) {
            return null;
        }
        try {
            return SortType.valueOf(sortType);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
