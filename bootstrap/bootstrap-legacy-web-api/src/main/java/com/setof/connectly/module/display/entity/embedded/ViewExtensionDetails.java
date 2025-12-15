package com.setof.connectly.module.display.entity.embedded;

import com.setof.connectly.module.display.dto.viewExtension.ViewExtensionDetailDto;
import com.setof.connectly.module.display.enums.viewDetails.ViewExtensionType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public ViewExtensionDetails(
            ViewExtensionType viewExtensionType,
            String linkUrl,
            String buttonName,
            int productCountPerClick,
            int maxClickCount,
            ViewExtensionType afterMaxActionType,
            String afterMaxActionLinkUrl) {
        this.viewExtensionType = viewExtensionType;
        this.linkUrl = linkUrl;
        this.buttonName = buttonName;
        this.productCountPerClick = productCountPerClick;
        this.maxClickCount = maxClickCount;
        this.afterMaxActionType = afterMaxActionType;
        this.afterMaxActionLinkUrl = afterMaxActionLinkUrl;
    }

    public ViewExtensionDetailDto toDto(long componentId) {
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
