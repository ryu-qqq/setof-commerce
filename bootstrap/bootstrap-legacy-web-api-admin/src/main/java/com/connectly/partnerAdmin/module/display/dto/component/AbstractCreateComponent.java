package com.connectly.partnerAdmin.module.display.dto.component;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.entity.embedded.ComponentDetails;
import com.connectly.partnerAdmin.module.display.entity.embedded.DisplayPeriod;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Objects;

@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractCreateComponent implements SubComponent {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long componentId;
    private DisplayPeriod displayPeriod;
    private String componentName;
    private int displayOrder;
    private Yn displayYn;


    @Override
    public ComponentDetails getComponentDetails() {
        return null;
    }

    @Override
    public int getExposedProducts() {
        return 0;
    }

    @Override
    public List<DisplayProductGroupThumbnail> getProductGroupThumbnails() {
        return List.of();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractCreateComponent that)) return false;
        if (!super.equals(o)) return false;

        if (!Objects.equals(displayYn, that.displayYn)) return false;
        if (!Objects.equals(componentName, that.componentName)) return false;
        if (!Objects.equals(displayPeriod.getDisplayEndDate(), that.displayPeriod.getDisplayEndDate())) return false;
        if (!Objects.equals(displayPeriod.getDisplayStartDate(), that.displayPeriod.getDisplayStartDate())) return false;
        return displayOrder == that.displayOrder;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), displayOrder, displayPeriod.getDisplayStartDate(), displayPeriod.getDisplayEndDate(), componentName, displayYn);
    }
}
