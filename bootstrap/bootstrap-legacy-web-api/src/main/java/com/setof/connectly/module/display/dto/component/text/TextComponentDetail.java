package com.setof.connectly.module.display.dto.component.text;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.setof.connectly.module.display.dto.component.AbstractCreateComponent;
import com.setof.connectly.module.display.entity.embedded.ViewExtensionDetails;
import com.setof.connectly.module.display.enums.component.ComponentType;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonTypeName("textComponent")
public class TextComponentDetail extends AbstractCreateComponent {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long textComponentId;

    private String content;

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
    public ViewExtensionDetails getViewExtensionDetails() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TextComponentDetail)) return false;
        if (!super.equals(o)) return false; // 상위 클래스의 equals 호출

        TextComponentDetail that = (TextComponentDetail) o;

        if (!Objects.equals(content, that.content)) return false;
        if (!Objects.equals(getComponentDetails(), that.getComponentDetails())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), content, getComponentDetails());
    }

    @Override
    public boolean isProductRelatedComponent() {
        return false;
    }
}
