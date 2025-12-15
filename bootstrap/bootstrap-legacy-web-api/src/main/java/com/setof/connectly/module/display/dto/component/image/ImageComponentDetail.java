package com.setof.connectly.module.display.dto.component.image;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.setof.connectly.module.display.dto.component.AbstractCreateComponent;
import com.setof.connectly.module.display.entity.embedded.ViewExtensionDetails;
import com.setof.connectly.module.display.enums.ImageType;
import com.setof.connectly.module.display.enums.component.ComponentType;
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
@JsonTypeName("imageComponent")
public class ImageComponentDetail extends AbstractCreateComponent {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long imageComponentId;

    private ImageType imageType;
    private List<ImageComponentLink> imageComponentLinks;

    @Override
    public Long getSubComponentId() {
        return imageComponentId;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Override
    public Long getViewExtensionId() {
        return null;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.IMAGE;
    }

    @Override
    public ViewExtensionDetails getViewExtensionDetails() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageComponentDetail that = (ImageComponentDetail) o;

        if (!Objects.equals(imageType, that.imageType)) return false;
        if (!Objects.equals(getComponentDetails(), that.getComponentDetails())) return false;
        if (!Objects.equals(imageComponentLinks, that.imageComponentLinks)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageType, getComponentDetails(), imageComponentLinks);
    }

    @Override
    public boolean isProductRelatedComponent() {
        return false;
    }
}
