package com.setof.connectly.module.display.dto.component.title;

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
@JsonTypeName("titleComponent")
public class TitleComponentDetail extends AbstractCreateComponent {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long titleComponentId;

    private String title1;
    private String title2;
    private String subTitle1;
    private String subTitle2;

    @Override
    public Long getSubComponentId() {
        return titleComponentId;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Override
    public Long getViewExtensionId() {
        return null;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.TITLE;
    }

    @Override
    public ViewExtensionDetails getViewExtensionDetails() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TitleComponentDetail)) return false;
        if (!super.equals(o)) return false; // 상위 클래스의 equals 호출

        TitleComponentDetail that = (TitleComponentDetail) o;

        if (!Objects.equals(title1, that.title1)) return false;
        if (!Objects.equals(title2, that.title2)) return false;
        if (!Objects.equals(subTitle1, that.subTitle1)) return false;
        if (!Objects.equals(subTitle2, that.subTitle2)) return false;
        if (!Objects.equals(getComponentDetails(), that.getComponentDetails())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(), title1, title2, subTitle1, subTitle2, getComponentDetails());
    }

    @Override
    public boolean isProductRelatedComponent() {
        return false;
    }
}
