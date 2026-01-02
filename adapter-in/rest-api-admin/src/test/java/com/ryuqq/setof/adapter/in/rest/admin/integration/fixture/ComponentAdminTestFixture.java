package com.ryuqq.setof.adapter.in.rest.admin.integration.fixture;

import com.ryuqq.setof.adapter.in.rest.admin.v2.component.dto.command.ComponentDetailV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.component.dto.command.CreateComponentV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.component.dto.command.UpdateComponentV2ApiRequest;
import com.ryuqq.setof.application.component.dto.response.ComponentDetailResponse;
import com.ryuqq.setof.application.component.dto.response.ComponentResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Component Admin 통합 테스트용 Fixture
 *
 * @author development-team
 * @since 2.0.0
 */
public final class ComponentAdminTestFixture {

    private ComponentAdminTestFixture() {}

    // ===== IDs =====
    public static final Long CONTENT_ID = 1L;
    public static final Long COMPONENT_ID_1 = 1L;
    public static final Long COMPONENT_ID_2 = 2L;
    public static final Long NON_EXISTENT_COMPONENT_ID = 99999L;

    // ===== Component 속성 =====
    public static final String COMPONENT_TYPE_PRODUCT = "PRODUCT";
    public static final String COMPONENT_TYPE_TEXT = "TEXT";
    public static final String COMPONENT_NAME = "인기 상품";
    public static final int DISPLAY_ORDER = 1;
    public static final int EXPOSED_PRODUCTS = 10;
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INACTIVE = "INACTIVE";

    // ===== Display Date =====
    public static final Instant DISPLAY_START_DATE = Instant.now().minus(1, ChronoUnit.DAYS);
    public static final Instant DISPLAY_END_DATE = Instant.now().plus(30, ChronoUnit.DAYS);

    // ===== Detail 속성 =====
    public static final String LIST_TYPE = "GRID";
    public static final String ORDER_TYPE = "LATEST";
    public static final String BADGE_TYPE = "NONE";
    public static final boolean SHOW_FILTER = false;

    /** 컴포넌트 상세 요청 (PRODUCT 타입) */
    public static ComponentDetailV2ApiRequest createProductDetailRequest() {
        return new ComponentDetailV2ApiRequest(
                null, // height
                null, // showLine
                null, // content
                null, // title1
                null, // title2
                null, // subTitle1
                null, // subTitle2
                null, // imageType
                LIST_TYPE, // listType
                ORDER_TYPE, // orderType
                BADGE_TYPE, // badgeType
                SHOW_FILTER, // showFilter
                null, // categoryId
                null, // stickyYn
                null // tabMovingType
                );
    }

    /** 컴포넌트 생성 요청 생성 */
    public static CreateComponentV2ApiRequest createComponentRequest() {
        return new CreateComponentV2ApiRequest(
                COMPONENT_TYPE_PRODUCT,
                COMPONENT_NAME,
                DISPLAY_ORDER,
                EXPOSED_PRODUCTS,
                DISPLAY_START_DATE,
                DISPLAY_END_DATE,
                createProductDetailRequest());
    }

    /** 컴포넌트 생성 요청 생성 (커스텀) */
    public static CreateComponentV2ApiRequest createComponentRequest(String type, String name) {
        return new CreateComponentV2ApiRequest(
                type,
                name,
                DISPLAY_ORDER,
                EXPOSED_PRODUCTS,
                DISPLAY_START_DATE,
                DISPLAY_END_DATE,
                createProductDetailRequest());
    }

    /** 컴포넌트 수정 요청 생성 */
    public static UpdateComponentV2ApiRequest updateComponentRequest() {
        return new UpdateComponentV2ApiRequest(
                COMPONENT_NAME + " (수정)",
                DISPLAY_ORDER + 1,
                EXPOSED_PRODUCTS + 5,
                DISPLAY_START_DATE,
                DISPLAY_END_DATE.plus(30, ChronoUnit.DAYS),
                createProductDetailRequest());
    }

    /** 컴포넌트 응답 생성 (Mock용) */
    public static ComponentResponse createComponentResponse(Long componentId) {
        return ComponentResponse.of(
                componentId,
                CONTENT_ID,
                COMPONENT_TYPE_PRODUCT,
                COMPONENT_NAME,
                DISPLAY_ORDER,
                STATUS_ACTIVE,
                EXPOSED_PRODUCTS,
                DISPLAY_START_DATE,
                DISPLAY_END_DATE,
                ComponentDetailResponse.forProduct(LIST_TYPE, ORDER_TYPE, BADGE_TYPE, SHOW_FILTER),
                Instant.now().minus(7, ChronoUnit.DAYS),
                Instant.now());
    }

    /** 컴포넌트 응답 생성 (커스텀) */
    public static ComponentResponse createComponentResponse(
            Long componentId, String name, String type, String status) {
        return ComponentResponse.of(
                componentId,
                CONTENT_ID,
                type,
                name,
                DISPLAY_ORDER,
                status,
                EXPOSED_PRODUCTS,
                DISPLAY_START_DATE,
                DISPLAY_END_DATE,
                ComponentDetailResponse.forProduct(LIST_TYPE, ORDER_TYPE, BADGE_TYPE, SHOW_FILTER),
                Instant.now().minus(7, ChronoUnit.DAYS),
                Instant.now());
    }

    /** 컴포넌트 응답 목록 생성 (Mock용) */
    public static List<ComponentResponse> createComponentResponses() {
        return List.of(
                createComponentResponse(
                        COMPONENT_ID_1, "컴포넌트 1", COMPONENT_TYPE_PRODUCT, STATUS_ACTIVE),
                createComponentResponse(
                        COMPONENT_ID_2, "컴포넌트 2", COMPONENT_TYPE_TEXT, STATUS_ACTIVE));
    }
}
