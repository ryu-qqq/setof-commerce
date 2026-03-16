package com.ryuqq.setof.adapter.in.rest.admin.v1.content.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.ComponentDetailsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.CreateContentV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SubComponentV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.SubComponentV1ApiRequest.*;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.UpdateContentDisplayYnV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.content.dto.request.ViewExtensionDetailsV1ApiRequest;
import com.ryuqq.setof.application.contentpage.dto.command.ChangeContentPageStatusCommand;
import com.ryuqq.setof.application.contentpage.dto.command.RegisterContentPageCommand;
import com.ryuqq.setof.application.contentpage.dto.command.RegisterDisplayComponentCommand;
import com.ryuqq.setof.application.contentpage.dto.command.UpdateContentPageCommand;
import com.ryuqq.setof.application.contentpage.dto.command.ViewExtensionCommand;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * ContentCommandV1ApiMapper - v1 콘텐츠 Command API 변환 매퍼.
 *
 * <p>레거시 v1 요청 DTO(displayYn Y/N, LocalDateTime, 다형성 SubComponent)를 Application Command(boolean,
 * Instant, specData JSON)로 변환합니다.
 *
 * <p>변환 규칙:
 *
 * <ul>
 *   <li>displayYn "Y" → active true, "N" → active false
 *   <li>LocalDateTime (KST 기준) → Instant
 *   <li>contentId null/0 → 등록, 양수 → 수정
 *   <li>ComponentDetailsV1ApiRequest → listType, orderType, badgeType, filterEnabled
 *   <li>ViewExtensionDetailsV1ApiRequest → ViewExtensionCommand
 *   <li>타입별 세부 데이터 → specData (JSON 문자열)
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ContentCommandV1ApiMapper {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private final ObjectMapper objectMapper;

    public ContentCommandV1ApiMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * CreateContentV1ApiRequest → RegisterContentPageCommand 변환 (등록용).
     *
     * @param request v1 콘텐츠 등록 요청 DTO
     * @return Application 콘텐츠 페이지 등록 Command
     */
    public RegisterContentPageCommand toRegisterCommand(CreateContentV1ApiRequest request) {
        return new RegisterContentPageCommand(
                request.title(),
                request.memo(),
                request.imageUrl(),
                toInstant(request.displayPeriod().displayStartDate()),
                toInstant(request.displayPeriod().displayEndDate()),
                toActive(request.displayYn()),
                toComponentCommands(request.components()));
    }

    /**
     * CreateContentV1ApiRequest → UpdateContentPageCommand 변환 (수정용).
     *
     * @param request v1 콘텐츠 수정 요청 DTO (contentId 필수)
     * @return Application 콘텐츠 페이지 수정 Command
     */
    public UpdateContentPageCommand toUpdateCommand(CreateContentV1ApiRequest request) {
        return new UpdateContentPageCommand(
                request.contentId(),
                request.title(),
                request.memo(),
                request.imageUrl(),
                toInstant(request.displayPeriod().displayStartDate()),
                toInstant(request.displayPeriod().displayEndDate()),
                toActive(request.displayYn()),
                toComponentCommands(request.components()));
    }

    /**
     * contentId + UpdateContentDisplayYnV1ApiRequest → ChangeContentPageStatusCommand 변환.
     *
     * @param contentId PathVariable 콘텐츠 ID
     * @param request v1 노출 상태 변경 요청 DTO
     * @return Application 콘텐츠 페이지 노출 상태 변경 Command
     */
    public ChangeContentPageStatusCommand toChangeStatusCommand(
            long contentId, UpdateContentDisplayYnV1ApiRequest request) {
        return new ChangeContentPageStatusCommand(contentId, toActive(request.displayYn()));
    }

    private List<RegisterDisplayComponentCommand> toComponentCommands(
            List<SubComponentV1ApiRequest> components) {
        if (components == null || components.isEmpty()) {
            return List.of();
        }
        return components.stream().map(this::toComponentCommand).toList();
    }

    private RegisterDisplayComponentCommand toComponentCommand(SubComponentV1ApiRequest component) {
        ComponentDetailsV1ApiRequest details = component.componentDetails();
        ViewExtensionDetailsV1ApiRequest viewExt = component.viewExtensionDetails();

        return new RegisterDisplayComponentCommand(
                component.componentId(),
                component.displayOrder(),
                component.componentType(),
                component.componentName(),
                toInstant(component.displayPeriod().displayStartDate()),
                toInstant(component.displayPeriod().displayEndDate()),
                toActive(component.displayYn()),
                details != null ? details.listType() : null,
                details != null ? details.orderType() : null,
                details != null ? details.badgeType() : null,
                details != null && "Y".equalsIgnoreCase(details.filterYn()),
                component.exposedProducts(),
                toViewExtensionCommand(viewExt),
                toSpecData(component));
    }

    private ViewExtensionCommand toViewExtensionCommand(ViewExtensionDetailsV1ApiRequest viewExt) {
        if (viewExt == null) {
            return null;
        }
        return new ViewExtensionCommand(
                viewExt.viewExtensionType(),
                viewExt.linkUrl(),
                viewExt.buttonName(),
                viewExt.productCountPerClick(),
                viewExt.maxClickCount(),
                viewExt.afterMaxActionType(),
                viewExt.afterMaxActionLinkUrl());
    }

    private String toSpecData(SubComponentV1ApiRequest component) {
        Map<String, Object> spec = new LinkedHashMap<>();

        switch (component) {
            case BrandComponentV1ApiRequest brand -> {
                spec.put("brandComponentId", brand.brandComponentId());
                spec.put("categoryId", brand.categoryId());
                spec.put("brandList", brand.brandList());
            }
            case CategoryComponentV1ApiRequest category -> {
                spec.put("categoryComponentId", category.categoryComponentId());
                spec.put("categoryId", category.categoryId());
            }
            case ProductComponentV1ApiRequest product ->
                    spec.put("productComponentId", product.productComponentId());
            case TabComponentV1ApiRequest tab -> {
                spec.put("tabComponentId", tab.tabComponentId());
                spec.put("tabDetails", tab.tabDetails());
            }
            case ImageComponentV1ApiRequest image -> {
                spec.put("imageComponentId", image.imageComponentId());
                spec.put("imageType", image.imageType());
                spec.put("imageComponentLinks", image.imageComponentLinks());
            }
            case TextComponentV1ApiRequest text -> {
                spec.put("textComponentId", text.textComponentId());
                spec.put("content", text.content());
            }
            case TitleComponentV1ApiRequest title -> {
                spec.put("titleComponentId", title.titleComponentId());
                spec.put("title1", title.title1());
                spec.put("title2", title.title2());
                spec.put("subTitle1", title.subTitle1());
                spec.put("subTitle2", title.subTitle2());
            }
            case BlankComponentV1ApiRequest blank -> {
                spec.put("blankComponentId", blank.blankComponentId());
                spec.put("height", blank.height());
                spec.put("lineYn", blank.lineYn());
            }
        }

        if (spec.isEmpty()) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(spec);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("컴포넌트 specData 직렬화 실패", e);
        }
    }

    private boolean toActive(String displayYn) {
        return "Y".equalsIgnoreCase(displayYn);
    }

    private Instant toInstant(LocalDateTime ldt) {
        if (ldt == null) {
            return null;
        }
        return ldt.atZone(KST).toInstant();
    }
}
