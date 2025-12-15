package com.setof.connectly.module.display.dto.viewExtension;

import com.setof.connectly.module.display.enums.viewDetails.ViewExtensionType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ViewExtensionDetailDto {

    private long componentId;
    private ViewExtensionType viewExtensionType;
    private String linkUrl;
    private String buttonName;
    private int productCountPerClick;
    private int maxClickCount;
    private ViewExtensionType afterMaxActionType;
    private String afterMaxActionLinkUrl;

    public ViewExtensionDetailDto(
            long componentId,
            ViewExtensionType viewExtensionType,
            String linkUrl,
            String buttonName,
            int productCountPerClick,
            int maxClickCount,
            ViewExtensionType afterMaxActionType,
            String afterMaxActionLinkUrl) {
        this.componentId = componentId;
        this.viewExtensionType = viewExtensionType;
        this.linkUrl = linkUrl;
        this.buttonName = buttonName;
        this.productCountPerClick = productCountPerClick;
        this.maxClickCount = maxClickCount;
        this.afterMaxActionType = afterMaxActionType;
        this.afterMaxActionLinkUrl = afterMaxActionLinkUrl;
    }
}
