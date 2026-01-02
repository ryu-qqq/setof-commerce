package com.ryuqq.setof.adapter.in.rest.admin.v2.banneritem.controller;

import com.ryuqq.setof.adapter.in.rest.admin.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banneritem.dto.command.CreateBannerItemV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.banneritem.mapper.BannerItemAdminV2ApiMapper;
import com.ryuqq.setof.application.banneritem.dto.command.CreateBannerItemCommand;
import com.ryuqq.setof.application.banneritem.port.in.command.CreateBannerItemUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * BannerItem Admin Command Controller
 *
 * <p>CQRS Command 담당: 생성, 수정, 삭제 엔드포인트
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "BannerItem Admin", description = "배너 아이템 관리 API")
@RestController
@RequestMapping(ApiV2Paths.BannerItems.BASE)
@PreAuthorize("hasRole('ADMIN')")
public class BannerItemAdminCommandController {

    private final CreateBannerItemUseCase createBannerItemUseCase;
    private final BannerItemAdminV2ApiMapper mapper;

    public BannerItemAdminCommandController(
            CreateBannerItemUseCase createBannerItemUseCase, BannerItemAdminV2ApiMapper mapper) {
        this.createBannerItemUseCase = createBannerItemUseCase;
        this.mapper = mapper;
    }

    /**
     * 배너 아이템 단건 생성
     *
     * @param bannerId 배너 ID
     * @param request 생성 요청
     * @return 생성된 배너 아이템 ID
     */
    @Operation(summary = "배너 아이템 생성")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "생성 성공")
            })
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> create(
            @Parameter(description = "배너 ID", required = true) @PathVariable Long bannerId,
            @Valid @RequestBody CreateBannerItemV2ApiRequest request) {
        CreateBannerItemCommand command = mapper.toCreateCommand(bannerId, request);
        Long bannerItemId = createBannerItemUseCase.create(command);
        return ResponseEntity.ok(ApiResponse.ofSuccess(bannerItemId));
    }

    /**
     * 배너 아이템 일괄 생성
     *
     * @param bannerId 배너 ID
     * @param requests 생성 요청 목록
     * @return 생성된 배너 아이템 ID 목록
     */
    @Operation(summary = "배너 아이템 일괄 생성")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "생성 성공")
            })
    @PostMapping("/batch")
    public ResponseEntity<ApiResponse<List<Long>>> createBatch(
            @Parameter(description = "배너 ID", required = true) @PathVariable Long bannerId,
            @Valid @RequestBody List<CreateBannerItemV2ApiRequest> requests) {
        List<CreateBannerItemCommand> commands = mapper.toCreateCommands(bannerId, requests);
        List<Long> bannerItemIds = createBannerItemUseCase.createAll(commands);
        return ResponseEntity.ok(ApiResponse.ofSuccess(bannerItemIds));
    }
}
