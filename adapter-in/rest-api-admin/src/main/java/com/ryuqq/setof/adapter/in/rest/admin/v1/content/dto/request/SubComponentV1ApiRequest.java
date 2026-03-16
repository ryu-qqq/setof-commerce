package com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * SubComponentV1ApiRequest - 콘텐츠 하위 컴포넌트 v1 요청 DTO (polymorphic).
 *
 * <p>레거시 SubComponent 인터페이스와 동일한 JSON 구조를 유지합니다. "type" 속성으로 컴포넌트 타입을 구분합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(
            value = SubComponentV1ApiRequest.BrandComponentV1ApiRequest.class,
            name = "brandComponentDetail"),
    @JsonSubTypes.Type(
            value = SubComponentV1ApiRequest.CategoryComponentV1ApiRequest.class,
            name = "categoryComponentDetail"),
    @JsonSubTypes.Type(
            value = SubComponentV1ApiRequest.ProductComponentV1ApiRequest.class,
            name = "productComponentDetail"),
    @JsonSubTypes.Type(
            value = SubComponentV1ApiRequest.TabComponentV1ApiRequest.class,
            name = "tabComponentDetail"),
    @JsonSubTypes.Type(
            value = SubComponentV1ApiRequest.ImageComponentV1ApiRequest.class,
            name = "imageComponentDetail"),
    @JsonSubTypes.Type(
            value = SubComponentV1ApiRequest.TextComponentV1ApiRequest.class,
            name = "textComponentDetail"),
    @JsonSubTypes.Type(
            value = SubComponentV1ApiRequest.TitleComponentV1ApiRequest.class,
            name = "titleComponentDetail"),
    @JsonSubTypes.Type(
            value = SubComponentV1ApiRequest.BlankComponentV1ApiRequest.class,
            name = "blankComponentDetail"),
})
@Schema(description = "콘텐츠 하위 컴포넌트 (polymorphic)")
public sealed interface SubComponentV1ApiRequest
        permits SubComponentV1ApiRequest.BrandComponentV1ApiRequest,
                SubComponentV1ApiRequest.CategoryComponentV1ApiRequest,
                SubComponentV1ApiRequest.ProductComponentV1ApiRequest,
                SubComponentV1ApiRequest.TabComponentV1ApiRequest,
                SubComponentV1ApiRequest.ImageComponentV1ApiRequest,
                SubComponentV1ApiRequest.TextComponentV1ApiRequest,
                SubComponentV1ApiRequest.TitleComponentV1ApiRequest,
                SubComponentV1ApiRequest.BlankComponentV1ApiRequest {

    /** 공통 필드 접근자. */
    Long componentId();

    DisplayPeriodV1ApiRequest displayPeriod();

    String componentName();

    int displayOrder();

    String displayYn();

    /** 컴포넌트 타입 문자열. */
    String componentType();

    /** exposedProducts (기본 0). */
    default int exposedProducts() {
        return 0;
    }

    /** viewExtensionId (기본 null). */
    default Long viewExtensionId() {
        return null;
    }

    /** componentDetails (공통). */
    default ComponentDetailsV1ApiRequest componentDetails() {
        return null;
    }

    /** viewExtensionDetails (공통). */
    default ViewExtensionDetailsV1ApiRequest viewExtensionDetails() {
        return null;
    }

    // ── Brand ──

    record BrandComponentV1ApiRequest(
            Long brandComponentId,
            Long componentId,
            DisplayPeriodV1ApiRequest displayPeriod,
            String componentName,
            int displayOrder,
            String displayYn,
            List<BrandItemV1ApiRequest> brandList,
            Long categoryId,
            ComponentDetailsV1ApiRequest componentDetails,
            int exposedProducts,
            Long viewExtensionId,
            ViewExtensionDetailsV1ApiRequest viewExtensionDetails)
            implements SubComponentV1ApiRequest {

        @Override
        public String componentType() {
            return "BRAND";
        }
    }

    record BrandItemV1ApiRequest(long brandId, String brandName) {}

    // ── Category ──

    record CategoryComponentV1ApiRequest(
            Long categoryComponentId,
            Long componentId,
            DisplayPeriodV1ApiRequest displayPeriod,
            String componentName,
            int displayOrder,
            String displayYn,
            long categoryId,
            ComponentDetailsV1ApiRequest componentDetails,
            int exposedProducts,
            Long viewExtensionId,
            ViewExtensionDetailsV1ApiRequest viewExtensionDetails)
            implements SubComponentV1ApiRequest {

        @Override
        public String componentType() {
            return "CATEGORY";
        }
    }

    // ── Product ──

    record ProductComponentV1ApiRequest(
            Long productComponentId,
            Long componentId,
            DisplayPeriodV1ApiRequest displayPeriod,
            String componentName,
            int displayOrder,
            String displayYn,
            int exposedProducts,
            ComponentDetailsV1ApiRequest componentDetails,
            Long viewExtensionId,
            ViewExtensionDetailsV1ApiRequest viewExtensionDetails)
            implements SubComponentV1ApiRequest {

        @Override
        public String componentType() {
            return "PRODUCT";
        }
    }

    // ── Tab ──

    record TabComponentV1ApiRequest(
            Long tabComponentId,
            Long componentId,
            DisplayPeriodV1ApiRequest displayPeriod,
            String componentName,
            int displayOrder,
            String displayYn,
            List<TabDetailV1ApiRequest> tabDetails,
            int exposedProducts,
            ComponentDetailsV1ApiRequest componentDetails,
            Long viewExtensionId,
            ViewExtensionDetailsV1ApiRequest viewExtensionDetails)
            implements SubComponentV1ApiRequest {

        @Override
        public String componentType() {
            return "TAB";
        }
    }

    record TabDetailV1ApiRequest(
            Long tabId, String tabName, String stickyYn, String tabMovingType, int displayOrder) {}

    // ── Image ──

    record ImageComponentV1ApiRequest(
            Long imageComponentId,
            Long componentId,
            DisplayPeriodV1ApiRequest displayPeriod,
            String componentName,
            int displayOrder,
            String displayYn,
            String imageType,
            List<ImageComponentLinkV1ApiRequest> imageComponentLinks,
            ComponentDetailsV1ApiRequest componentDetails)
            implements SubComponentV1ApiRequest {

        @Override
        public String componentType() {
            return "IMAGE";
        }
    }

    record ImageComponentLinkV1ApiRequest(
            Long imageComponentItemId, int displayOrder, String imageUrl, String linkUrl) {}

    // ── Text ──

    record TextComponentV1ApiRequest(
            Long textComponentId,
            Long componentId,
            DisplayPeriodV1ApiRequest displayPeriod,
            String componentName,
            int displayOrder,
            String displayYn,
            String content,
            ComponentDetailsV1ApiRequest componentDetails)
            implements SubComponentV1ApiRequest {

        @Override
        public String componentType() {
            return "TEXT";
        }
    }

    // ── Title ──

    record TitleComponentV1ApiRequest(
            Long titleComponentId,
            Long componentId,
            DisplayPeriodV1ApiRequest displayPeriod,
            String componentName,
            int displayOrder,
            String displayYn,
            String title1,
            String title2,
            String subTitle1,
            String subTitle2,
            ComponentDetailsV1ApiRequest componentDetails)
            implements SubComponentV1ApiRequest {

        @Override
        public String componentType() {
            return "TITLE";
        }
    }

    // ── Blank ──

    record BlankComponentV1ApiRequest(
            Long blankComponentId,
            Long componentId,
            DisplayPeriodV1ApiRequest displayPeriod,
            String componentName,
            int displayOrder,
            String displayYn,
            double height,
            String lineYn,
            ComponentDetailsV1ApiRequest componentDetails)
            implements SubComponentV1ApiRequest {

        @Override
        public String componentType() {
            return "BLANK";
        }
    }
}
