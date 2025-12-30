package com.connectly.partnerAdmin.module.display.dto.component.image;

import com.connectly.partnerAdmin.module.display.dto.component.AbstractCreateComponent;
import com.connectly.partnerAdmin.module.display.dto.component.SortItem;
import com.connectly.partnerAdmin.module.display.entity.embedded.ComponentDetails;
import com.connectly.partnerAdmin.module.display.enums.ComponentType;
import com.connectly.partnerAdmin.module.display.enums.ImageType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
@JsonTypeName("imageComponentDetail")
public class ImageComponentDetail extends AbstractCreateComponent {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long imageComponentId;
    private ImageType imageType;
    private List<ImageComponentLink> imageComponentLinks;
    private ComponentDetails componentDetails;

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
    public List<SortItem> getSortItems() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageComponentDetail that = (ImageComponentDetail) o;

        if (!Objects.equals(imageType, that.imageType)) return false;
        if (!Objects.equals(componentDetails, that.componentDetails)) return false;
        return Objects.equals(imageComponentLinks, that.imageComponentLinks);

    }

    @Override
    public int hashCode() {
        return Objects.hash(imageType, componentDetails, imageComponentLinks);
    }


    public void setImageComponentId(){
        imageComponentLinks.forEach(imageComponentLink ->
                imageComponentLink.setImageComponentId(imageComponentId)
        );
    }


}
