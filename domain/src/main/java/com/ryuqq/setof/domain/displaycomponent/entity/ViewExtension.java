package com.ryuqq.setof.domain.displaycomponent.entity;

import com.ryuqq.setof.domain.displaycomponent.id.ViewExtensionId;
import com.ryuqq.setof.domain.displaycomponent.vo.AfterMaxAction;
import com.ryuqq.setof.domain.displaycomponent.vo.ViewExtensionType;

/**
 * ViewExtension - 뷰 확장 Entity (DisplayComponent 소속).
 *
 * <p>컴포넌트의 "더보기" 버튼, 페이징, 스크롤 등 확장 동작을 정의한다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class ViewExtension {

    private final ViewExtensionId id;
    private ViewExtensionType extensionType;
    private String linkUrl;
    private String buttonName;
    private int productCountPerClick;
    private int maxClickCount;
    private AfterMaxAction afterMaxAction;

    private ViewExtension(
            ViewExtensionId id,
            ViewExtensionType extensionType,
            String linkUrl,
            String buttonName,
            int productCountPerClick,
            int maxClickCount,
            AfterMaxAction afterMaxAction) {
        this.id = id;
        this.extensionType = extensionType;
        this.linkUrl = linkUrl;
        this.buttonName = buttonName;
        this.productCountPerClick = productCountPerClick;
        this.maxClickCount = maxClickCount;
        this.afterMaxAction = afterMaxAction;
    }

    public static ViewExtension forNew(
            ViewExtensionType extensionType,
            String linkUrl,
            String buttonName,
            int productCountPerClick,
            int maxClickCount,
            AfterMaxAction afterMaxAction) {
        return new ViewExtension(
                ViewExtensionId.forNew(),
                extensionType,
                linkUrl,
                buttonName,
                productCountPerClick,
                maxClickCount,
                afterMaxAction);
    }

    public static ViewExtension reconstitute(
            ViewExtensionId id,
            ViewExtensionType extensionType,
            String linkUrl,
            String buttonName,
            int productCountPerClick,
            int maxClickCount,
            AfterMaxAction afterMaxAction) {
        return new ViewExtension(
                id,
                extensionType,
                linkUrl,
                buttonName,
                productCountPerClick,
                maxClickCount,
                afterMaxAction);
    }

    public void update(
            ViewExtensionType extensionType,
            String linkUrl,
            String buttonName,
            int productCountPerClick,
            int maxClickCount,
            AfterMaxAction afterMaxAction) {
        this.extensionType = extensionType;
        this.linkUrl = linkUrl;
        this.buttonName = buttonName;
        this.productCountPerClick = productCountPerClick;
        this.maxClickCount = maxClickCount;
        this.afterMaxAction = afterMaxAction;
    }

    public ViewExtensionId id() {
        return id;
    }

    public Long idValue() {
        return id.value();
    }

    public ViewExtensionType extensionType() {
        return extensionType;
    }

    public String linkUrl() {
        return linkUrl;
    }

    public String buttonName() {
        return buttonName;
    }

    public int productCountPerClick() {
        return productCountPerClick;
    }

    public int maxClickCount() {
        return maxClickCount;
    }

    public AfterMaxAction afterMaxAction() {
        return afterMaxAction;
    }
}
