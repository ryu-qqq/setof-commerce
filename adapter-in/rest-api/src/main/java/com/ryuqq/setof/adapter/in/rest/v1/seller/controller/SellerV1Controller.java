package com.ryuqq.setof.adapter.in.rest.v1.seller.controller;

import com.ryuqq.setof.adapter.in.rest.auth.paths.ApiPaths;
import com.ryuqq.setof.adapter.in.rest.common.dto.ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.seller.dto.response.SellerV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.v1.seller.mapper.SellerV1ApiMapper;
import com.ryuqq.setof.application.seller.dto.response.SellerResponse;
import com.ryuqq.setof.application.seller.port.in.query.GetSellerUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * V1 Seller Controller (Legacy)
 *
 * <p>레거시 API 호환을 위한 V1 Seller 엔드포인트. 내부적으로 V2 UseCase를 사용합니다.
 *
 * @author development-team
 * @since 1.0.0
 * @deprecated V2 API로 마이그레이션 권장
 */
@Tag(name = "Seller (Legacy V1)", description = "레거시 Seller API - V2로 마이그레이션 권장")
@Deprecated
@RestController
@RequestMapping(ApiPaths.Seller.BASE)
public class SellerV1Controller {

    private final GetSellerUseCase getSellerUseCase;
    private final SellerV1ApiMapper sellerV1ApiMapper;

    public SellerV1Controller(
            GetSellerUseCase getSellerUseCase, SellerV1ApiMapper sellerV1ApiMapper) {
        this.getSellerUseCase = getSellerUseCase;
        this.sellerV1ApiMapper = sellerV1ApiMapper;
    }

    @Deprecated
    @Operation(summary = "[Legacy] 셀러 정보 조회", description = "셀러 정보를 조회합니다.")
    @GetMapping(ApiPaths.Seller.DETAIL)
    public ResponseEntity<ApiResponse<SellerV1ApiResponse>> fetchSeller(
            @PathVariable("sellerId") long sellerId) {

        SellerResponse response = getSellerUseCase.execute(sellerId);
        SellerV1ApiResponse v1Response = sellerV1ApiMapper.toV1Response(response);

        return ResponseEntity.ok(ApiResponse.ofSuccess(v1Response));
    }
}
