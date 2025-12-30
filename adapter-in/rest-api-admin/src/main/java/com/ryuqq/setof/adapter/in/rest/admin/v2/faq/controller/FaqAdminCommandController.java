package com.ryuqq.setof.adapter.in.rest.admin.v2.faq.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.faq.dto.command.ChangeFaqCategoryV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.faq.dto.command.CreateFaqV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.faq.dto.command.UpdateFaqV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.faq.mapper.FaqAdminV2ApiMapper;
import com.ryuqq.setof.application.faq.dto.command.ChangeFaqCategoryCommand;
import com.ryuqq.setof.application.faq.dto.command.CreateFaqCommand;
import com.ryuqq.setof.application.faq.dto.command.DeleteFaqCommand;
import com.ryuqq.setof.application.faq.dto.command.HideFaqCommand;
import com.ryuqq.setof.application.faq.dto.command.PublishFaqCommand;
import com.ryuqq.setof.application.faq.dto.command.SetTopFaqCommand;
import com.ryuqq.setof.application.faq.dto.command.UnsetTopFaqCommand;
import com.ryuqq.setof.application.faq.dto.command.UpdateFaqCommand;
import com.ryuqq.setof.application.faq.port.in.command.ChangeFaqCategoryUseCase;
import com.ryuqq.setof.application.faq.port.in.command.CreateFaqUseCase;
import com.ryuqq.setof.application.faq.port.in.command.DeleteFaqUseCase;
import com.ryuqq.setof.application.faq.port.in.command.HideFaqUseCase;
import com.ryuqq.setof.application.faq.port.in.command.PublishFaqUseCase;
import com.ryuqq.setof.application.faq.port.in.command.SetTopFaqUseCase;
import com.ryuqq.setof.application.faq.port.in.command.UnsetTopFaqUseCase;
import com.ryuqq.setof.application.faq.port.in.command.UpdateFaqUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * FAQ Admin Command Controller
 *
 * <p>CQRS Command 담당: 생성, 수정, 삭제, 상태 변경 엔드포인트
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "FAQ Admin", description = "FAQ 관리 API")
@RestController
@RequestMapping(ApiV2Paths.Faqs.BASE)
@PreAuthorize("hasRole('ADMIN')")
public class FaqAdminCommandController {

    private final CreateFaqUseCase createFaqUseCase;
    private final UpdateFaqUseCase updateFaqUseCase;
    private final DeleteFaqUseCase deleteFaqUseCase;
    private final PublishFaqUseCase publishFaqUseCase;
    private final HideFaqUseCase hideFaqUseCase;
    private final SetTopFaqUseCase setTopFaqUseCase;
    private final UnsetTopFaqUseCase unsetTopFaqUseCase;
    private final ChangeFaqCategoryUseCase changeFaqCategoryUseCase;
    private final FaqAdminV2ApiMapper mapper;

    public FaqAdminCommandController(
            CreateFaqUseCase createFaqUseCase,
            UpdateFaqUseCase updateFaqUseCase,
            DeleteFaqUseCase deleteFaqUseCase,
            PublishFaqUseCase publishFaqUseCase,
            HideFaqUseCase hideFaqUseCase,
            SetTopFaqUseCase setTopFaqUseCase,
            UnsetTopFaqUseCase unsetTopFaqUseCase,
            ChangeFaqCategoryUseCase changeFaqCategoryUseCase,
            FaqAdminV2ApiMapper mapper) {
        this.createFaqUseCase = createFaqUseCase;
        this.updateFaqUseCase = updateFaqUseCase;
        this.deleteFaqUseCase = deleteFaqUseCase;
        this.publishFaqUseCase = publishFaqUseCase;
        this.hideFaqUseCase = hideFaqUseCase;
        this.setTopFaqUseCase = setTopFaqUseCase;
        this.unsetTopFaqUseCase = unsetTopFaqUseCase;
        this.changeFaqCategoryUseCase = changeFaqCategoryUseCase;
        this.mapper = mapper;
    }

    /**
     * FAQ 생성
     *
     * @param request 생성 요청
     * @return 생성된 FAQ ID
     */
    @Operation(summary = "FAQ 생성")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "생성 성공")
            })
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> create(
            @Valid @RequestBody CreateFaqV2ApiRequest request) {
        // TODO: 인증 정보에서 작성자 ID 추출
        Long createdBy = 1L;
        CreateFaqCommand command = mapper.toCreateCommand(request, createdBy);
        Long faqId = createFaqUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ofSuccess(faqId));
    }

    /**
     * FAQ 수정
     *
     * @param faqId FAQ ID
     * @param request 수정 요청
     * @return 성공 응답
     */
    @Operation(summary = "FAQ 수정")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "수정 성공")
            })
    @PatchMapping(ApiV2Paths.Faqs.ID_PATH)
    public ResponseEntity<ApiResponse<Void>> update(
            @Parameter(description = "FAQ ID", required = true) @PathVariable Long faqId,
            @Valid @RequestBody UpdateFaqV2ApiRequest request) {
        // TODO: 인증 정보에서 수정자 ID 추출
        Long updatedBy = 1L;
        UpdateFaqCommand command = mapper.toUpdateCommand(faqId, request, updatedBy);
        updateFaqUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    /**
     * FAQ 발행
     *
     * @param faqId FAQ ID
     * @return 성공 응답
     */
    @Operation(summary = "FAQ 발행")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "발행 성공")
            })
    @PostMapping(ApiV2Paths.Faqs.PUBLISH_PATH)
    public ResponseEntity<ApiResponse<Void>> publish(
            @Parameter(description = "FAQ ID", required = true) @PathVariable Long faqId) {
        // TODO: 인증 정보에서 수정자 ID 추출
        Long updatedBy = 1L;
        publishFaqUseCase.execute(new PublishFaqCommand(faqId, updatedBy));
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    /**
     * FAQ 숨김
     *
     * @param faqId FAQ ID
     * @return 성공 응답
     */
    @Operation(summary = "FAQ 숨김")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "숨김 성공")
            })
    @PostMapping(ApiV2Paths.Faqs.HIDE_PATH)
    public ResponseEntity<ApiResponse<Void>> hide(
            @Parameter(description = "FAQ ID", required = true) @PathVariable Long faqId) {
        // TODO: 인증 정보에서 수정자 ID 추출
        Long updatedBy = 1L;
        hideFaqUseCase.execute(new HideFaqCommand(faqId, updatedBy));
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    /**
     * FAQ 상단 노출 설정
     *
     * @param faqId FAQ ID
     * @param topOrder 상단 순서 (기본값: 1)
     * @return 성공 응답
     */
    @Operation(summary = "FAQ 상단 노출 설정")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "상단 노출 설정 성공")
            })
    @PostMapping(ApiV2Paths.Faqs.TOP_PATH)
    public ResponseEntity<ApiResponse<Void>> setTop(
            @Parameter(description = "FAQ ID", required = true) @PathVariable Long faqId,
            @Parameter(description = "상단 순서", example = "1") @RequestParam(defaultValue = "1")
                    int topOrder) {
        // TODO: 인증 정보에서 수정자 ID 추출
        Long updatedBy = 1L;
        setTopFaqUseCase.execute(new SetTopFaqCommand(faqId, topOrder, updatedBy));
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    /**
     * FAQ 상단 노출 해제
     *
     * @param faqId FAQ ID
     * @return 성공 응답
     */
    @Operation(summary = "FAQ 상단 노출 해제")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "상단 노출 해제 성공")
            })
    @PostMapping(ApiV2Paths.Faqs.UNTOP_PATH)
    public ResponseEntity<ApiResponse<Void>> unsetTop(
            @Parameter(description = "FAQ ID", required = true) @PathVariable Long faqId) {
        // TODO: 인증 정보에서 수정자 ID 추출
        Long updatedBy = 1L;
        unsetTopFaqUseCase.execute(new UnsetTopFaqCommand(faqId, updatedBy));
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    /**
     * FAQ 카테고리 변경
     *
     * @param faqId FAQ ID
     * @param request 카테고리 변경 요청
     * @return 성공 응답
     */
    @Operation(summary = "FAQ 카테고리 변경")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "카테고리 변경 성공")
            })
    @PostMapping(ApiV2Paths.Faqs.CATEGORY_PATH)
    public ResponseEntity<ApiResponse<Void>> changeCategory(
            @Parameter(description = "FAQ ID", required = true) @PathVariable Long faqId,
            @Valid @RequestBody ChangeFaqCategoryV2ApiRequest request) {
        // TODO: 인증 정보에서 수정자 ID 추출
        Long updatedBy = 1L;
        ChangeFaqCategoryCommand command =
                mapper.toChangeCategoryCommand(faqId, request, updatedBy);
        changeFaqCategoryUseCase.execute(command);
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }

    /**
     * FAQ 삭제
     *
     * @param faqId FAQ ID
     * @return 성공 응답
     */
    @Operation(summary = "FAQ 삭제")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "삭제 성공")
            })
    @PostMapping(ApiV2Paths.Faqs.DELETE_PATH)
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "FAQ ID", required = true) @PathVariable Long faqId) {
        // TODO: 인증 정보에서 삭제자 ID 추출
        Long deletedBy = 1L;
        deleteFaqUseCase.execute(new DeleteFaqCommand(faqId, deletedBy));
        return ResponseEntity.ok(ApiResponse.ofSuccess());
    }
}
