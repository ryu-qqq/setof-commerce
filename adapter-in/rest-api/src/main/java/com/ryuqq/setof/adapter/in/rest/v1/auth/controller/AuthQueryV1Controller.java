package com.ryuqq.setof.adapter.in.rest.v1.auth.controller;

import com.ryuqq.setof.adapter.in.rest.common.auth.AuthenticatedUserId;
import com.ryuqq.setof.adapter.in.rest.v1.auth.AuthV1Endpoints;
import com.ryuqq.setof.adapter.in.rest.v1.auth.dto.response.IsExistUserV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.auth.dto.response.MyPageV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.auth.dto.response.UserV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.auth.mapper.AuthV1ApiMapper;
import com.ryuqq.setof.adapter.in.rest.v1.common.dto.V1ApiResponse;
import com.ryuqq.setof.application.member.dto.query.IsExistUserResult;
import com.ryuqq.setof.application.member.dto.query.MyPageResult;
import com.ryuqq.setof.application.member.dto.query.UserResult;
import com.ryuqq.setof.application.member.port.in.GetMyPageUseCase;
import com.ryuqq.setof.application.member.port.in.GetUserUseCase;
import com.ryuqq.setof.application.member.port.in.IsExistUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthQueryV1Controller - 인증 조회 V1 Public API.
 *
 * <p>API-CTR-001: @RestController 필수.
 *
 * <p>API-CTR-003: UseCase(Port-In) 인터페이스 의존.
 *
 * <p>API-CTR-004: ResponseEntity + V1ApiResponse 래핑.
 *
 * <p>API-CTR-005: Controller @Transactional 금지.
 *
 * <p>API-CTR-007: Controller 비즈니스 로직 금지.
 *
 * @author ryu-qqq
 * @since 1.2.0
 */
@Tag(name = "인증 조회 V1", description = "사용자 조회/존재여부 V1 Public API")
@RestController
public class AuthQueryV1Controller {

    private final GetUserUseCase getUserUseCase;
    private final IsExistUserUseCase isExistUserUseCase;
    private final GetMyPageUseCase getMyPageUseCase;
    private final AuthV1ApiMapper mapper;

    public AuthQueryV1Controller(
            GetUserUseCase getUserUseCase,
            IsExistUserUseCase isExistUserUseCase,
            GetMyPageUseCase getMyPageUseCase,
            AuthV1ApiMapper mapper) {
        this.getUserUseCase = getUserUseCase;
        this.isExistUserUseCase = isExistUserUseCase;
        this.getMyPageUseCase = getMyPageUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "현재 사용자 조회", description = "인증된 사용자의 프로필을 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @GetMapping(AuthV1Endpoints.USER)
    public ResponseEntity<V1ApiResponse<UserV1ApiResponse>> fetchUser(
            @AuthenticatedUserId Long userId) {
        UserResult result = getUserUseCase.execute(userId);
        return ResponseEntity.ok(V1ApiResponse.success(mapper.toUserResponse(result)));
    }

    @Operation(summary = "사용자 존재 여부", description = "전화번호로 사용자 존재 여부를 확인합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "유효성 검증 실패")
    })
    @GetMapping(AuthV1Endpoints.EXISTS)
    public ResponseEntity<V1ApiResponse<IsExistUserV1ApiResponse>> isExistUser(
            @Parameter(description = "전화번호", required = true, example = "01012345678")
                    @RequestParam
                    @Pattern(regexp = "010[0-9]{8}", message = "유효하지 않은 전화번호 형식입니다.")
                    String phoneNumber) {
        IsExistUserResult result = isExistUserUseCase.execute(phoneNumber);
        return ResponseEntity.ok(V1ApiResponse.success(mapper.toIsExistUserResponse(result)));
    }

    @Operation(summary = "마이페이지 조회", description = "회원 프로필 및 주문 상태별 건수를 조회합니다.")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "조회 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "인증 필요")
    })
    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @GetMapping(AuthV1Endpoints.MY_PAGE)
    public ResponseEntity<V1ApiResponse<MyPageV1ApiResponse>> getMyPage(
            @AuthenticatedUserId Long userId) {
        MyPageResult result = getMyPageUseCase.execute(userId);
        return ResponseEntity.ok(V1ApiResponse.success(mapper.toMyPageResponse(result)));
    }
}
