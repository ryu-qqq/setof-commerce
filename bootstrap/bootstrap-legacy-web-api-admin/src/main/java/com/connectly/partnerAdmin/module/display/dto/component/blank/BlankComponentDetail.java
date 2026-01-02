package com.connectly.partnerAdmin.module.display.dto.component.blank;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.dto.component.AbstractCreateComponent;
import com.connectly.partnerAdmin.module.display.dto.component.SortItem;
import com.connectly.partnerAdmin.module.display.entity.embedded.ComponentDetails;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonTypeName("blankComponentDetail")
public class BlankComponentDetail extends AbstractCreateComponent {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long blankComponentId;
    private double height;
    private Yn lineYn;
    private ComponentDetails componentDetails;

    @Override
    public Long getSubComponentId() {
        return blankComponentId;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Override
    public Long getViewExtensionId() {
        return null;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.BLANK;
    }

    @Override
    public List<SortItem> getSortItems() {
        return null;
    }

}
