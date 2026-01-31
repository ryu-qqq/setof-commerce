package com.ryuqq.setof.adapter.in.rest.admin.v2.commoncode.controller;

import static com.ryuqq.setof.adapter.in.rest.admin.v2.commoncode.CommonCodeAdminEndpoints.BASE;
import static com.ryuqq.setof.adapter.in.rest.admin.v2.commoncode.CommonCodeAdminEndpoints.CHANGE_ACTIVE_STATUS;
import static com.ryuqq.setof.adapter.in.rest.admin.v2.commoncode.CommonCodeAdminEndpoints.REGISTER;
import static com.ryuqq.setof.adapter.in.rest.admin.v2.commoncode.CommonCodeAdminEndpoints.UPDATE;

import com.ryuqq.setof.adapter.in.rest.admin.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncode.dto.command.ChangeActiveStatusApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncode.dto.command.RegisterCommonCodeApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncode.dto.command.UpdateCommonCodeApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.commoncode.mapper.CommonCodeCommandApiMapper;
import com.ryuqq.setof.application.commoncode.dto.command.ChangeCommonCodeStatusCommand;
import com.ryuqq.setof.application.commoncode.dto.command.RegisterCommonCodeCommand;
import com.ryuqq.setof.application.commoncode.dto.command.UpdateCommonCodeCommand;
import com.ryuqq.setof.application.commoncode.port.in.command.ChangeCommonCodeStatusUseCase;
import com.ryuqq.setof.application.commoncode.port.in.command.RegisterCommonCodeUseCase;
import com.ryuqq.setof.application.commoncode.port.in.command.UpdateCommonCodeUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * CommonCodeCommandController - 공통 코드 Command Controller.
 *
 * <p>API-CTL-001: Controller는 @RestController로 등록.
 *
 * <p>API-CTL-004: Command Controller는 등록/수정/삭제 엔드포인트만 처리.
 *
 * <p>API-CTL-003: UseCase만 의존 (Service 직접 의존 금지).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag(name = "공통 코드 관리", description = "공통 코드 등록/수정/상태변경 API")
@RestController
@RequestMapping(BASE)
public class CommonCodeCommandController {

    private final RegisterCommonCodeUseCase registerUseCase;
    private final UpdateCommonCodeUseCase updateUseCase;
    private final ChangeCommonCodeStatusUseCase changeStatusUseCase;
    private final CommonCodeCommandApiMapper commandMapper;

    public CommonCodeCommandController(
            RegisterCommonCodeUseCase registerUseCase,
            UpdateCommonCodeUseCase updateUseCase,
            ChangeCommonCodeStatusUseCase changeStatusUseCase,
            CommonCodeCommandApiMapper commandMapper) {
        this.registerUseCase = registerUseCase;
        this.updateUseCase = updateUseCase;
        this.changeStatusUseCase = changeStatusUseCase;
        this.commandMapper = commandMapper;
    }

    /**
     * 공통 코드 등록.
     *
     * @param request 등록 요청
     * @return 생성된 공통 코드 ID
     */
    @Operation(summary = "공통 코드 등록", description = "새로운 공통 코드를 등록합니다.")
    @PostMapping(REGISTER)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Long> register(@Valid @RequestBody RegisterCommonCodeApiRequest request) {

        RegisterCommonCodeCommand command = commandMapper.toCommand(request);
        Long createdId = registerUseCase.execute(command);

        return ApiResponse.of(createdId);
    }

    /**
     * 공통 코드 수정.
     *
     * @param id 공통 코드 ID
     * @param request 수정 요청
     * @return 빈 응답
     */
    @Operation(summary = "공통 코드 수정", description = "공통 코드의 표시명과 순서를 수정합니다.")
    @PutMapping(UPDATE)
    public ApiResponse<Void> update(
            @Parameter(description = "공통 코드 ID", required = true, example = "1") @PathVariable
                    Long id,
            @Valid @RequestBody UpdateCommonCodeApiRequest request) {

        UpdateCommonCodeCommand command = commandMapper.toCommand(id, request);
        updateUseCase.execute(command);

        return ApiResponse.of();
    }

    /**
     * 공통 코드 활성화 상태 변경.
     *
     * @param request 상태 변경 요청
     * @return 빈 응답
     */
    @Operation(summary = "공통 코드 활성화 상태 변경", description = "공통 코드의 활성화 상태를 일괄 변경합니다.")
    @PatchMapping(CHANGE_ACTIVE_STATUS)
    public ApiResponse<Void> changeActiveStatus(
            @Valid @RequestBody ChangeActiveStatusApiRequest request) {

        ChangeCommonCodeStatusCommand command = commandMapper.toCommand(request);
        changeStatusUseCase.execute(command);

        return ApiResponse.of();
    }
}
