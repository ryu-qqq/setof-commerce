package com.ryuqq.setof.adapter.in.rest.v2.bank.controller;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiV2Paths;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v2.bank.dto.response.BankV2ApiResponse;
import com.ryuqq.setof.application.bank.dto.response.BankResponse;
import com.ryuqq.setof.application.bank.port.in.query.GetBanksUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Bank V2 Controller
 *
 * <p>은행 정보 조회 API 엔드포인트
 *
 * <p>은행 정보는 조회만 가능하며, 관리자가 직접 DB에서 관리합니다.
 *
 * @author development-team
 * @since 2.0.0
 */
@Tag(name = "Bank", description = "은행 정보 조회 API")
@RestController
@RequestMapping(ApiV2Paths.Banks.BASE)
public class BankV2Controller {

    private final GetBanksUseCase getBanksUseCase;

    public BankV2Controller(GetBanksUseCase getBanksUseCase) {
        this.getBanksUseCase = getBanksUseCase;
    }

    /**
     * 활성 은행 목록 조회
     *
     * <p>환불계좌 등록 시 은행 선택 드롭다운에 표시할 은행 목록을 조회합니다.
     *
     * @return 활성 은행 목록 (displayOrder 정렬)
     */
    @Operation(
            summary = "은행 목록 조회",
            description = "활성 상태인 은행 목록을 조회합니다. 환불계좌 등록 시 은행 선택에 사용됩니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "조회 성공"),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "500",
                        description = "서버 오류",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    @GetMapping
    public ResponseEntity<ApiResponse<List<BankV2ApiResponse>>> getActiveBanks() {
        List<BankResponse> banks = getBanksUseCase.execute();

        List<BankV2ApiResponse> response =
                banks.stream().map(BankV2ApiResponse::from).toList();

        return ResponseEntity.ok(ApiResponse.ofSuccess(response));
    }
}
