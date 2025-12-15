package com.setof.connectly.module.display.dto.component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.setof.connectly.module.common.enums.Yn;
import com.setof.connectly.module.display.entity.embedded.ComponentDetails;
import com.setof.connectly.module.display.entity.embedded.DisplayPeriod;
import com.setof.connectly.module.product.dto.ProductGroupThumbnail;
import java.util.ArrayList;
import java.util.List;
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
public abstract class AbstractCreateComponent implements SubComponent {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long componentId;

    private String componentName;
    private DisplayPeriod displayPeriod;
    private int displayOrder;
    private Yn displayYn;

    @JsonIgnore private ComponentDetails componentDetails;

    @Override
    public int getExposedProducts() {
        return 0;
    }

    @Override
    public List<ProductGroupThumbnail> getProductGroupThumbnails() {
        return new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractCreateComponent)) return false;
        if (!super.equals(o)) return false; // 상위 클래스의 equals 호출

        AbstractCreateComponent that = (AbstractCreateComponent) o;

        if (!Objects.equals(displayYn, that.displayYn)) return false;
        if (!Objects.equals(componentName, that.componentName)) return false;
        if (!Objects.equals(
                displayPeriod.getDisplayEndDate(), that.displayPeriod.getDisplayEndDate()))
            return false;
        if (!Objects.equals(
                displayPeriod.getDisplayStartDate(), that.displayPeriod.getDisplayStartDate()))
            return false;
        if (displayOrder != that.displayOrder) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(),
                displayOrder,
                displayPeriod.getDisplayStartDate(),
                displayPeriod.getDisplayEndDate(),
                componentName,
                displayYn);
    }
}
