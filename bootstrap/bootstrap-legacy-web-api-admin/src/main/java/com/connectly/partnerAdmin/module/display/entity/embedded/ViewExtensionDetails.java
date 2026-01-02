package com.connectly.partnerAdmin.module.display.entity.embedded;

import com.connectly.partnerAdmin.module.display.dto.viewExtension.ViewExtensionDetailDto;
import com.connectly.partnerAdmin.module.display.enums.ViewExtensionType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.Objects;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Embeddable
public class ViewExtensionDetails {

    @Enumerated(EnumType.STRING)
    private ViewExtensionType viewExtensionType;
    private String linkUrl;
    private String buttonName;
    private int productCountPerClick;
    private int maxClickCount;

    @Enumerated(EnumType.STRING)
    private ViewExtensionType afterMaxActionType;
    private String afterMaxActionLinkUrl;


    public ViewExtensionDetails(ViewExtensionDetails other) {
        this.viewExtensionType = other.viewExtensionType;
        this.linkUrl = other.linkUrl;
        this.buttonName = other.buttonName;
        this.productCountPerClick = other.productCountPerClick;
        this.maxClickCount = other.maxClickCount;
        this.afterMaxActionType = other.afterMaxActionType;
        this.afterMaxActionLinkUrl = other.afterMaxActionLinkUrl;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewExtensionDetails that = (ViewExtensionDetails) o;
        return viewExtensionType == that.viewExtensionType &&
                Objects.equals(linkUrl, that.linkUrl) &&
                Objects.equals(buttonName, that.buttonName) &&
                productCountPerClick == that.productCountPerClick &&
                maxClickCount == that.maxClickCount &&
                afterMaxActionType == that.afterMaxActionType &&
                Objects.equals(afterMaxActionLinkUrl, that.afterMaxActionLinkUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(viewExtensionType, linkUrl, buttonName, productCountPerClick, maxClickCount, afterMaxActionType, afterMaxActionLinkUrl);
    }

    public ViewExtensionDetailDto toDto(long componentId){
        return ViewExtensionDetailDto.builder()
                .componentId(componentId)
                .viewExtensionType(viewExtensionType)
                .linkUrl(linkUrl)
                .buttonName(buttonName)
                .productCountPerClick(productCountPerClick)
                .maxClickCount(maxClickCount)
                .afterMaxActionType(afterMaxActionType)
                .afterMaxActionLinkUrl(afterMaxActionLinkUrl)
                .build();
    }
}
