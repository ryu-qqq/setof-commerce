package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.CreateDiscountFromExcelV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.CreateDiscountTargetV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.CreateDiscountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.DiscountDetailsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.UpdateDiscountStatusV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.UpdateDiscountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.response.DiscountPolicyV1ApiResponse;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.response.DiscountPolicyV1ApiResponse.DiscountDetailsResponse;
import com.ryuqq.setof.application.discount.dto.command.CreateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.CreateDiscountPolicyCommand.TargetItem;
import com.ryuqq.setof.application.discount.dto.command.ModifyDiscountTargetsCommand;
import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountPolicyStatusCommand;
import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyResult;
import com.ryuqq.setof.application.discount.dto.response.DiscountTargetResult;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * DiscountPolicyCommandApiMapper - 할인 정책 Command API 변환 매퍼.
 *
 * <p>v1 레거시 API Request/Response와 Application Command/Result 간 양방향 변환을 담당합니다.
 *
 * <p>레거시 필드 매핑 규칙:
 *
 * <ul>
 *   <li>DiscountType.RATE → discountMethod "RATE"
 *   <li>DiscountType.PRICE → discountMethod "FIXED_AMOUNT"
 *   <li>IssueType.PRODUCT → targetType "PRODUCT_GROUP"
 *   <li>IssueType.SELLER → targetType "SELLER"
 *   <li>IssueType.BRAND → targetType "BRAND"
 *   <li>discountRatio → discountRate (RATE 방식) / discountAmount (PRICE 방식, int 캐스팅)
 *   <li>maxDiscountPrice → maxDiscountAmount
 *   <li>Yn "Y"/"N" → boolean true/false
 *   <li>discountLimitYn → discountCapped
 *   <li>LocalDateTime → Instant (Asia/Seoul 기준)
 *   <li>publisherType ADMIN → stackingGroup "PLATFORM_INSTANT"
 *   <li>publisherType SELLER → stackingGroup "SELLER_INSTANT"
 *   <li>applicationType → 레거시에 없음, "IMMEDIATE" 고정
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class DiscountPolicyCommandApiMapper {

    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");

    /**
     * CreateDiscountV1ApiRequest → CreateDiscountPolicyCommand 변환.
     *
     * <p>레거시 DiscountType.RATE → discountMethod "RATE", PRICE → "FIXED_AMOUNT" 매핑. applicationType은
     * "IMMEDIATE" 고정. sellerId는 null 고정. totalBudget은 0 고정.
     *
     * @param request v1 할인 정책 생성 요청 DTO
     * @return Application Command DTO
     */
    public CreateDiscountPolicyCommand toCommand(CreateDiscountV1ApiRequest request) {
        DiscountDetailsV1ApiRequest details = request.discountDetails();
        return new CreateDiscountPolicyCommand(
                details.discountPolicyName(),
                details.memo(),
                toDiscountMethod(details.discountType()),
                details.isRateDiscount() ? details.discountRatio() : null,
                details.isPriceDiscount() ? (int) details.discountRatio() : null,
                (int) details.maxDiscountPrice(),
                details.hasDiscountLimit(),
                null,
                "INSTANT",
                details.publisherType(),
                null,
                toStackingGroup(details.publisherType()),
                details.priority(),
                toInstant(details.policyStartDate()),
                toInstant(details.policyEndDate()),
                0,
                List.of(new TargetItem(toTargetType(details.issueType()), 0L)));
    }

    /**
     * long + UpdateDiscountV1ApiRequest → UpdateDiscountPolicyCommand 변환.
     *
     * <p>API-DTO-004: Update Request에 ID 포함 금지 → PathVariable에서 전달.
     *
     * @param discountPolicyId 수정할 할인 정책 ID (PathVariable)
     * @param request v1 할인 정책 수정 요청 DTO
     * @return Application Command DTO
     */
    public UpdateDiscountPolicyCommand toCommand(
            long discountPolicyId, UpdateDiscountV1ApiRequest request) {
        DiscountDetailsV1ApiRequest details = request.discountDetails();
        return new UpdateDiscountPolicyCommand(
                discountPolicyId,
                details.discountPolicyName(),
                details.memo(),
                toDiscountMethod(details.discountType()),
                details.isRateDiscount() ? details.discountRatio() : null,
                details.isPriceDiscount() ? (int) details.discountRatio() : null,
                (int) details.maxDiscountPrice(),
                details.hasDiscountLimit(),
                null,
                details.priority(),
                toInstant(details.policyStartDate()),
                toInstant(details.policyEndDate()),
                0);
    }

    /**
     * UpdateDiscountStatusV1ApiRequest → UpdateDiscountPolicyStatusCommand 변환.
     *
     * <p>Yn "Y"/"N" → boolean true/false 변환.
     *
     * @param request v1 할인 정책 상태 변경 요청 DTO
     * @return Application Command DTO
     */
    public UpdateDiscountPolicyStatusCommand toCommand(UpdateDiscountStatusV1ApiRequest request) {
        return new UpdateDiscountPolicyStatusCommand(
                request.discountPolicyIds(), "Y".equalsIgnoreCase(request.activeYn()));
    }

    /**
     * long + CreateDiscountTargetV1ApiRequest → ModifyDiscountTargetsCommand 변환.
     *
     * <p>IssueType.PRODUCT → targetType "PRODUCT_GROUP", SELLER → "SELLER", BRAND → "BRAND" 매핑.
     *
     * @param discountPolicyId 대상을 수정할 할인 정책 ID (PathVariable)
     * @param request v1 할인 대상 생성 요청 DTO
     * @return Application Command DTO
     */
    public ModifyDiscountTargetsCommand toCommand(
            long discountPolicyId, CreateDiscountTargetV1ApiRequest request) {
        return new ModifyDiscountTargetsCommand(
                discountPolicyId, toTargetType(request.issueType()), request.targetIds());
    }

    /**
     * List&lt;CreateDiscountFromExcelV1ApiRequest&gt; → List&lt;CreateDiscountPolicyCommand&gt; 변환.
     *
     * <p>엑셀 업로드 일괄 생성 요청을 Command 목록으로 변환합니다.
     *
     * @param requests v1 엑셀 기반 할인 정책 생성 요청 목록
     * @return Application Command DTO 목록
     */
    public List<CreateDiscountPolicyCommand> toCommands(
            List<CreateDiscountFromExcelV1ApiRequest> requests) {
        return requests.stream().map(this::toCommandFromExcel).toList();
    }

    /**
     * DiscountPolicyResult → DiscountPolicyV1ApiResponse 역방향 변환.
     *
     * <p>새 시스템 Result를 레거시 v1 응답 형식으로 변환합니다.
     *
     * <ul>
     *   <li>discountMethod "RATE" → discountType "RATE", "FIXED_AMOUNT" → discountType "PRICE"
     *   <li>discountRate → discountRatio (RATE 방식) / discountAmount → discountRatio (PRICE 방식)
     *   <li>maxDiscountAmount → maxDiscountPrice
     *   <li>discountCapped → discountLimitYn "Y"/"N"
     *   <li>active → activeYn "Y"/"N"
     *   <li>Instant → LocalDateTime (Asia/Seoul)
     *   <li>shareYn, shareRatio → 레거시 응답 고정값 "N", 0.0
     *   <li>insertOperator, updateOperator → "system" 고정
     *   <li>issueType → targets 첫 번째 항목의 targetType 역매핑
     * </ul>
     *
     * @param result Application 결과 DTO
     * @return v1 할인 정책 응답 DTO
     */
    public DiscountPolicyV1ApiResponse toResponse(DiscountPolicyResult result) {
        String discountType = toDiscountType(result.discountMethod());
        double discountRatio = resolveDiscountRatio(result);
        String issueType = resolveIssueType(result);

        DiscountDetailsResponse details =
                DiscountDetailsResponse.of(
                        result.name(),
                        discountType,
                        result.publisherType(),
                        issueType,
                        result.discountCapped() ? "Y" : "N",
                        result.maxDiscountAmount() != null ? result.maxDiscountAmount() : 0L,
                        "N",
                        0.0,
                        discountRatio,
                        toLocalDateTime(result.startAt()),
                        toLocalDateTime(result.endAt()),
                        result.description(),
                        result.priority(),
                        result.active() ? "Y" : "N");

        return DiscountPolicyV1ApiResponse.of(
                result.id(),
                details,
                toLocalDateTime(result.createdAt()),
                toLocalDateTime(result.updatedAt()),
                "system",
                "system");
    }

    // ---------------------------------------------------------------------------
    // private 변환 헬퍼
    // ---------------------------------------------------------------------------

    /**
     * CreateDiscountFromExcelV1ApiRequest → CreateDiscountPolicyCommand 단건 변환.
     *
     * @param request 엑셀 기반 단건 할인 정책 생성 요청 DTO
     * @return Application Command DTO
     */
    private CreateDiscountPolicyCommand toCommandFromExcel(
            CreateDiscountFromExcelV1ApiRequest request) {
        DiscountDetailsV1ApiRequest details = request.discountDetails();
        return new CreateDiscountPolicyCommand(
                details.discountPolicyName(),
                details.memo(),
                toDiscountMethod(details.discountType()),
                details.isRateDiscount() ? details.discountRatio() : null,
                details.isPriceDiscount() ? (int) details.discountRatio() : null,
                (int) details.maxDiscountPrice(),
                details.hasDiscountLimit(),
                null,
                "INSTANT",
                details.publisherType(),
                null,
                toStackingGroup(details.publisherType()),
                details.priority(),
                toInstant(details.policyStartDate()),
                toInstant(details.policyEndDate()),
                0,
                List.of(new TargetItem(toTargetType(details.issueType()), 0L)));
    }

    /**
     * 레거시 discountType → 새 시스템 discountMethod 변환.
     *
     * @param legacyDiscountType 레거시 할인 유형 ("RATE" / "PRICE")
     * @return 새 시스템 할인 방식 ("RATE" / "FIXED_AMOUNT")
     */
    private String toDiscountMethod(String legacyDiscountType) {
        if ("PRICE".equalsIgnoreCase(legacyDiscountType)) {
            return "FIXED_AMOUNT";
        }
        return "RATE";
    }

    /**
     * 새 시스템 discountMethod → 레거시 discountType 역변환.
     *
     * @param discountMethod 새 시스템 할인 방식 ("RATE" / "FIXED_AMOUNT")
     * @return 레거시 할인 유형 ("RATE" / "PRICE")
     */
    private String toDiscountType(String discountMethod) {
        if ("FIXED_AMOUNT".equalsIgnoreCase(discountMethod)) {
            return "PRICE";
        }
        return "RATE";
    }

    /**
     * 레거시 issueType → 새 시스템 targetType 변환.
     *
     * <p>DiscountTargetType enum: PRODUCT, CATEGORY, BRAND, SELLER. 레거시 IssueType.PRODUCT →
     * DiscountTargetType.PRODUCT (동일).
     *
     * @param legacyIssueType 레거시 적용 대상 유형 ("PRODUCT" / "SELLER" / "BRAND")
     * @return 새 시스템 대상 유형 ("PRODUCT" / "SELLER" / "BRAND")
     */
    private String toTargetType(String legacyIssueType) {
        return legacyIssueType;
    }

    /**
     * 새 시스템 targetType → 레거시 issueType 역변환.
     *
     * <p>DiscountTargetType enum: PRODUCT, CATEGORY, BRAND, SELLER. DiscountTargetType.PRODUCT →
     * 레거시 IssueType.PRODUCT (동일).
     *
     * @param newTargetType 새 시스템 대상 유형 ("PRODUCT" / "SELLER" / "BRAND")
     * @return 레거시 적용 대상 유형 ("PRODUCT" / "SELLER" / "BRAND")
     */
    private String toIssueType(String newTargetType) {
        return newTargetType;
    }

    /**
     * 레거시 publisherType → 새 시스템 stackingGroup 변환.
     *
     * @param publisherType 발행자 유형 ("ADMIN" / "SELLER")
     * @return 스태킹 그룹 ("PLATFORM_INSTANT" / "SELLER_INSTANT")
     */
    private String toStackingGroup(String publisherType) {
        if ("SELLER".equalsIgnoreCase(publisherType)) {
            return "SELLER_INSTANT";
        }
        return "PLATFORM_INSTANT";
    }

    /**
     * LocalDateTime → Instant 변환 (Asia/Seoul 기준).
     *
     * @param localDateTime 변환할 LocalDateTime (nullable)
     * @return 변환된 Instant, null이면 null 반환
     */
    private Instant toInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.atZone(SEOUL).toInstant();
    }

    /**
     * Instant → LocalDateTime 변환 (Asia/Seoul 기준).
     *
     * @param instant 변환할 Instant (nullable)
     * @return 변환된 LocalDateTime, null이면 null 반환
     */
    private LocalDateTime toLocalDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return LocalDateTime.ofInstant(instant, SEOUL);
    }

    /**
     * 결과 DTO에서 레거시 discountRatio 값 결정.
     *
     * <p>RATE 방식이면 discountRate 반환, FIXED_AMOUNT 방식이면 discountAmount를 double로 변환하여 반환.
     *
     * @param result Application 결과 DTO
     * @return 레거시 discountRatio에 해당하는 값
     */
    private double resolveDiscountRatio(DiscountPolicyResult result) {
        if ("FIXED_AMOUNT".equalsIgnoreCase(result.discountMethod())) {
            return result.discountAmount() != null ? result.discountAmount().doubleValue() : 0.0;
        }
        return result.discountRate() != null ? result.discountRate() : 0.0;
    }

    /**
     * 결과 DTO의 targets에서 레거시 issueType 결정.
     *
     * <p>targets 목록의 첫 번째 항목의 targetType을 역매핑합니다. targets가 비어있으면 null 반환.
     *
     * @param result Application 결과 DTO
     * @return 레거시 issueType 값 또는 null
     */
    private String resolveIssueType(DiscountPolicyResult result) {
        if (result.targets() == null || result.targets().isEmpty()) {
            return null;
        }
        DiscountTargetResult firstTarget = result.targets().get(0);
        return toIssueType(firstTarget.targetType());
    }
}
