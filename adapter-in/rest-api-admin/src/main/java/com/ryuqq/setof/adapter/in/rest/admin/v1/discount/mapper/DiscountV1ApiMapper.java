package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.CreateDiscountTargetV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.CreateDiscountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.CreateDiscountV1ApiRequest.CreateDiscountDetailsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.UpdateDiscountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.query.DiscountFilterV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.response.DiscountPolicyV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.response.DiscountPolicyV1ApiResponse.DiscountDetailsV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.response.DiscountTargetV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.response.ProductDiscountTargetV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.response.SellerDiscountTargetV1ApiResponse;
import com.ryuqq.setof.application.discount.dto.command.ActivateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.DeactivateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.RegisterDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountTargetsCommand;
import com.ryuqq.setof.application.discount.dto.query.DiscountPolicySearchQuery;
import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyResponse;
import com.ryuqq.setof.domain.discount.vo.DiscountGroup;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import com.ryuqq.setof.domain.discount.vo.DiscountType;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Discount V1 API Mapper
 *
 * <p>V1 Request/Response와 V2 Command/Query/Response 간의 변환을 담당합니다.
 *
 * <p>매핑 규칙:
 *
 * <ul>
 *   <li>V1 issueType → V2 discountTargetType
 *   <li>V1 discountType RATIO → V2 RATE
 *   <li>V1 LocalDateTime → V2 Instant (Asia/Seoul 기준)
 *   <li>V1 activeYn (Y/N) ↔ V2 isActive (boolean)
 *   <li>V1 shareRatio → V2 sellerCostShareRatio (platform = 100 - seller)
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class DiscountV1ApiMapper {

    private static final ZoneId KOREA_ZONE = ZoneId.of("Asia/Seoul");
    private static final String ACTIVE_YES = "Y";

    /**
     * V1 생성 요청을 V2 등록 Command로 변환
     *
     * @param request V1 생성 요청
     * @param sellerId 셀러 ID (V1은 권한에서 추출, V2는 경로에서)
     * @return V2 등록 Command
     */
    public RegisterDiscountPolicyCommand toRegisterCommand(
            CreateDiscountV1ApiRequest request, Long sellerId) {

        CreateDiscountDetailsV1ApiRequest details = request.discountDetails();

        return new RegisterDiscountPolicyCommand(
                sellerId,
                details.discountPolicyName(),
                mapDiscountGroup(details.issueType()),
                mapDiscountType(details.discountType()),
                mapDiscountTargetType(details.issueType()),
                null,
                mapDiscountRate(details.discountRatio()),
                null,
                details.maxDiscountPrice(),
                null,
                toInstant(details.policyStartDate()),
                toInstant(details.policyEndDate()),
                null,
                null,
                calculatePlatformShareRatio(details.shareRatio()),
                calculateSellerShareRatio(details.shareRatio()),
                details.priority() != null ? details.priority() : 100,
                isActive(details.activeYn()));
    }

    /**
     * V1 수정 요청을 V2 수정 Command로 변환
     *
     * @param discountPolicyId 할인 정책 ID
     * @param request V1 수정 요청
     * @param sellerId 셀러 ID
     * @return V2 수정 Command
     */
    public UpdateDiscountPolicyCommand toUpdateCommand(
            Long discountPolicyId, UpdateDiscountV1ApiRequest request, Long sellerId) {

        CreateDiscountDetailsV1ApiRequest details = request.discountDetails();

        if (details == null) {
            return new UpdateDiscountPolicyCommand(
                    discountPolicyId,
                    sellerId,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
        }

        return new UpdateDiscountPolicyCommand(
                discountPolicyId,
                sellerId,
                details.discountPolicyName(),
                details.maxDiscountPrice(),
                null,
                toInstant(details.policyEndDate()),
                null,
                null,
                calculatePlatformShareRatio(details.shareRatio()),
                calculateSellerShareRatio(details.shareRatio()),
                details.priority());
    }

    /**
     * V1 필터를 V2 검색 Query로 변환
     *
     * @param filter V1 필터
     * @param sellerId 셀러 ID
     * @return V2 검색 Query
     */
    public DiscountPolicySearchQuery toSearchQuery(
            DiscountFilterV1ApiRequest filter, Long sellerId) {
        boolean activeOnly = filter.activeYn() != null && ACTIVE_YES.equals(filter.activeYn());

        DiscountTargetType targetType =
                filter.issueType() != null ? mapDiscountTargetType(filter.issueType()) : null;

        return new DiscountPolicySearchQuery(
                sellerId, null, null, targetType, null, activeOnly, false, false);
    }

    /**
     * 활성화 Command 생성
     *
     * @param discountPolicyId 할인 정책 ID
     * @param sellerId 셀러 ID
     * @return 활성화 Command
     */
    public ActivateDiscountPolicyCommand toActivateCommand(Long discountPolicyId, Long sellerId) {
        return new ActivateDiscountPolicyCommand(discountPolicyId, sellerId);
    }

    /**
     * 비활성화 Command 생성
     *
     * @param discountPolicyId 할인 정책 ID
     * @param sellerId 셀러 ID
     * @return 비활성화 Command
     */
    public DeactivateDiscountPolicyCommand toDeactivateCommand(
            Long discountPolicyId, Long sellerId) {
        return new DeactivateDiscountPolicyCommand(discountPolicyId, sellerId);
    }

    /**
     * V2 Response를 V1 Response로 변환
     *
     * @param response V2 Application Response
     * @return V1 API Response
     */
    public DiscountPolicyV1ApiResponse toV1Response(DiscountPolicyResponse response) {
        DiscountDetailsV1ApiResponse details =
                new DiscountDetailsV1ApiResponse(
                        response.policyName(),
                        mapToV1DiscountType(response.discountType()),
                        determinePublisherType(response.sellerId()),
                        mapToV1IssueType(response.discountTargetType()),
                        response.maximumDiscountAmount() != null ? ACTIVE_YES : "N",
                        response.maximumDiscountAmount(),
                        response.sellerCostShareRatio() != null
                                        && response.sellerCostShareRatio()
                                                        .compareTo(BigDecimal.ZERO)
                                                > 0
                                ? ACTIVE_YES
                                : "N",
                        response.sellerCostShareRatio() != null
                                ? response.sellerCostShareRatio().doubleValue()
                                : 0.0,
                        response.discountRate() != null
                                ? response.discountRate().doubleValue()
                                : 0.0,
                        toLocalDateTime(response.validStartAt()),
                        toLocalDateTime(response.validEndAt()),
                        null,
                        response.priority(),
                        response.isActive() ? ACTIVE_YES : "N");

        return new DiscountPolicyV1ApiResponse(
                response.discountPolicyId(),
                details,
                toLocalDateTime(response.createdAt()),
                toLocalDateTime(response.updatedAt()),
                "system",
                "system");
    }

    private DiscountGroup mapDiscountGroup(String issueType) {
        return DiscountGroup.PRODUCT;
    }

    private DiscountType mapDiscountType(String v1Type) {
        if (v1Type == null) {
            return DiscountType.RATE;
        }
        return switch (v1Type.toUpperCase()) {
            case "RATIO", "RATE" -> DiscountType.RATE;
            case "PRICE", "FIXED_PRICE" -> DiscountType.FIXED_PRICE;
            default -> DiscountType.RATE;
        };
    }

    private DiscountTargetType mapDiscountTargetType(String v1IssueType) {
        if (v1IssueType == null) {
            return DiscountTargetType.ALL;
        }
        return switch (v1IssueType.toUpperCase()) {
            case "PRODUCT" -> DiscountTargetType.PRODUCT;
            case "SELLER" -> DiscountTargetType.SELLER;
            case "BRAND" -> DiscountTargetType.BRAND;
            case "CATEGORY" -> DiscountTargetType.CATEGORY;
            default -> DiscountTargetType.ALL;
        };
    }

    private String mapToV1DiscountType(DiscountType discountType) {
        if (discountType == null) {
            return "RATIO";
        }
        return switch (discountType) {
            case RATE, TIERED_RATE -> "RATIO";
            case FIXED_PRICE, TIERED_PRICE -> "PRICE";
        };
    }

    private String mapToV1IssueType(DiscountTargetType targetType) {
        if (targetType == null) {
            return "PRODUCT";
        }
        return switch (targetType) {
            case PRODUCT -> "PRODUCT";
            case SELLER -> "SELLER";
            case BRAND -> "BRAND";
            case CATEGORY -> "CATEGORY";
            case ALL -> "PRODUCT";
        };
    }

    private String determinePublisherType(Long sellerId) {
        return sellerId != null && sellerId > 0 ? "SELLER" : "ADMIN";
    }

    private Integer mapDiscountRate(Double discountRatio) {
        if (discountRatio == null) {
            return null;
        }
        return discountRatio.intValue();
    }

    private BigDecimal calculatePlatformShareRatio(Double shareRatio) {
        if (shareRatio == null) {
            return BigDecimal.valueOf(100);
        }
        return BigDecimal.valueOf(100 - shareRatio);
    }

    private BigDecimal calculateSellerShareRatio(Double shareRatio) {
        if (shareRatio == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(shareRatio);
    }

    private Instant toInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.atZone(KOREA_ZONE).toInstant();
    }

    private LocalDateTime toLocalDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return LocalDateTime.ofInstant(instant, KOREA_ZONE);
    }

    private boolean isActive(String activeYn) {
        return ACTIVE_YES.equals(activeYn);
    }

    /**
     * V1 Target 생성 요청을 V2 UpdateDiscountTargetsCommand로 변환
     *
     * @param discountPolicyId 할인 정책 ID
     * @param sellerId 셀러 ID
     * @param request V1 Target 생성 요청
     * @return V2 UpdateDiscountTargetsCommand
     */
    public UpdateDiscountTargetsCommand toUpdateTargetsCommand(
            Long discountPolicyId, Long sellerId, CreateDiscountTargetV1ApiRequest request) {
        return new UpdateDiscountTargetsCommand(discountPolicyId, sellerId, request.targetIds());
    }

    /**
     * targetIds를 V1 DiscountTarget 응답 목록으로 변환
     *
     * @param discountPolicyId 할인 정책 ID
     * @param targetIds 적용 대상 ID 목록
     * @param issueType 발행 타입 (PRODUCT, SELLER 등)
     * @return V1 DiscountTarget 응답 목록
     */
    public List<DiscountTargetV1ApiResponse> toV1TargetResponses(
            Long discountPolicyId, List<Long> targetIds, String issueType) {
        return targetIds.stream()
                .map(
                        targetId ->
                                createTargetResponse(
                                        discountPolicyId,
                                        targetId,
                                        issueType,
                                        targetIds.indexOf(targetId)))
                .toList();
    }

    private DiscountTargetV1ApiResponse createTargetResponse(
            Long discountPolicyId, Long targetId, String issueType, int index) {
        LocalDateTime now = LocalDateTime.now(KOREA_ZONE);

        if ("SELLER".equalsIgnoreCase(issueType)) {
            return new SellerDiscountTargetV1ApiResponse(
                    discountPolicyId, (long) (index + 1), "셀러-" + targetId, "system", now);
        }

        return new ProductDiscountTargetV1ApiResponse(
                discountPolicyId, (long) (index + 1), targetId, "system", now);
    }
}
