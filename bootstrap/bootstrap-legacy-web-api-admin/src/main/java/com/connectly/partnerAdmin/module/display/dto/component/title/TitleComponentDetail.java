package com.connectly.partnerAdmin.module.display.dto.component.title;

import com.connectly.partnerAdmin.module.display.dto.component.AbstractCreateComponent;
import com.connectly.partnerAdmin.module.display.dto.component.SortItem;
import com.connectly.partnerAdmin.module.display.dto.component.text.TextComponentDetail;
import com.connectly.partnerAdmin.module.display.entity.embedded.ComponentDetails;
import com.connectly.partnerAdmin.module.display.entity.embedded.DisplayPeriod;
import com.connectly.partnerAdmin.module.display.entity.embedded.TitleDetails;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@JsonTypeName("titleComponentDetail")
public class TitleComponentDetail extends AbstractCreateComponent {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long titleComponentId;
    private String title1;
    private String title2;
    private String subTitle1;
    private String subTitle2;
    private ComponentDetails componentDetails;

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
    public List<SortItem> getSortItems() {
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
        if (!Objects.equals(componentDetails, that.componentDetails)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title1, title2, subTitle1, subTitle2, componentDetails);
    }


}
