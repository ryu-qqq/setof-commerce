package com.connectly.partnerAdmin.module.display.dto.component.text;


import com.connectly.partnerAdmin.module.display.dto.component.AbstractCreateComponent;
import com.connectly.partnerAdmin.module.display.dto.component.SortItem;
import com.connectly.partnerAdmin.module.display.dto.component.category.CategoryComponentDetail;
import com.connectly.partnerAdmin.module.display.entity.embedded.ComponentDetails;
import com.connectly.partnerAdmin.module.display.entity.embedded.DisplayPeriod;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Objects;


@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonTypeName("textComponentDetail")
public class TextComponentDetail extends AbstractCreateComponent {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long textComponentId;
    private String content;
    private ComponentDetails componentDetails;

    @Override
    public Long getSubComponentId() {
        return textComponentId;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Override
    public Long getViewExtensionId() {
        return null;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.TEXT;
    }

    @Override
    public List<SortItem> getSortItems() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TextComponentDetail)) return false;
        if (!super.equals(o)) return false; // 상위 클래스의 equals 호출

        TextComponentDetail that = (TextComponentDetail) o;

        if (!Objects.equals(content, that.content)) return false;
        if (!Objects.equals(componentDetails, that.componentDetails)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), content, componentDetails);
    }


}
