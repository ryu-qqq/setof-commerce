package com.ryuqq.setof.adapter.in.rest.admin.v2.discount.mapper;

import com.ryuqq.setof.adapter.in.rest.admin.v2.discount.dto.command.RegisterDiscountPolicyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.discount.dto.command.UpdateDiscountPolicyV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.discount.dto.command.UpdateDiscountTargetsV2ApiRequest;
import com.ryuqq.setof.application.discount.dto.command.ActivateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.DeactivateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.DeleteDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.RegisterDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountTargetsCommand;
import com.ryuqq.setof.application.discount.dto.query.DiscountPolicySearchQuery;
import com.ryuqq.setof.domain.discount.vo.DiscountGroup;
import com.ryuqq.setof.domain.discount.vo.DiscountTargetType;
import com.ryuqq.setof.domain.discount.vo.DiscountType;
import org.springframework.stereotype.Component;

/**
 * DiscountPolicy Admin V2 API Mapper
 *
 * <p>할인 정책 관리 API DTO ↔ Application Command 변환
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class DiscountPolicyAdminV2ApiMapper {

    /** 할인 정책 등록 요청 → 등록 커맨드 변환 (PathVariable sellerId 사용) */
    public RegisterDiscountPolicyCommand toRegisterCommand(
            Long sellerId, RegisterDiscountPolicyV2ApiRequest request) {
        return new RegisterDiscountPolicyCommand(
                sellerId,
                request.policyName(),
                DiscountGroup.valueOf(request.discountGroup()),
                DiscountType.valueOf(request.discountType()),
                DiscountTargetType.valueOf(request.discountTargetType()),
                request.targetId(),
                request.discountRate(),
                request.fixedDiscountAmount(),
                request.maximumDiscountAmount(),
                request.minimumOrderAmount(),
                request.validStartAt(),
                request.validEndAt(),
                request.maxUsagePerCustomer(),
                request.maxTotalUsage(),
                request.platformCostShareRatio(),
                request.sellerCostShareRatio(),
                request.priority(),
                request.isActive());
    }

    /** 할인 정책 수정 요청 → 수정 커맨드 변환 (PathVariable sellerId, discountPolicyId 사용) */
    public UpdateDiscountPolicyCommand toUpdateCommand(
            Long discountPolicyId, Long sellerId, UpdateDiscountPolicyV2ApiRequest request) {
        return new UpdateDiscountPolicyCommand(
                discountPolicyId,
                sellerId,
                request.policyName(),
                request.maximumDiscountAmount(),
                request.minimumOrderAmount(),
                request.validEndAt(),
                request.maxUsagePerCustomer(),
                request.maxTotalUsage(),
                request.platformCostShareRatio(),
                request.sellerCostShareRatio(),
                request.priority());
    }

    /** 할인 정책 활성화 커맨드 생성 */
    public ActivateDiscountPolicyCommand toActivateCommand(Long discountPolicyId, Long sellerId) {
        return new ActivateDiscountPolicyCommand(discountPolicyId, sellerId);
    }

    /** 할인 정책 비활성화 커맨드 생성 */
    public DeactivateDiscountPolicyCommand toDeactivateCommand(
            Long discountPolicyId, Long sellerId) {
        return new DeactivateDiscountPolicyCommand(discountPolicyId, sellerId);
    }

    /** 할인 정책 삭제 커맨드 생성 */
    public DeleteDiscountPolicyCommand toDeleteCommand(Long discountPolicyId, Long sellerId) {
        return new DeleteDiscountPolicyCommand(discountPolicyId, sellerId);
    }

    /** 셀러의 전체 정책 조회 쿼리 생성 */
    public DiscountPolicySearchQuery toSearchQuery(
            Long sellerId, boolean activeOnly, boolean includeDeleted, boolean validOnly) {
        return new DiscountPolicySearchQuery(
                sellerId, null, null, null, null, activeOnly, includeDeleted, validOnly);
    }

    /** 셀러의 활성 정책만 조회 쿼리 생성 */
    public DiscountPolicySearchQuery toActiveSearchQuery(Long sellerId) {
        return DiscountPolicySearchQuery.forActivePolicies(sellerId);
    }

    /** 셀러의 모든 정책 조회 쿼리 생성 */
    public DiscountPolicySearchQuery toAllSearchQuery(Long sellerId) {
        return DiscountPolicySearchQuery.forAllPolicies(sellerId);
    }

    /** 특정 할인 그룹의 정책 조회 쿼리 생성 */
    public DiscountPolicySearchQuery toGroupSearchQuery(Long sellerId, String discountGroup) {
        return DiscountPolicySearchQuery.forGroup(sellerId, DiscountGroup.valueOf(discountGroup));
    }

    /** 할인 정책 적용 대상 수정 커맨드 생성 */
    public UpdateDiscountTargetsCommand toUpdateTargetsCommand(
            Long discountPolicyId, Long sellerId, UpdateDiscountTargetsV2ApiRequest request) {
        return new UpdateDiscountTargetsCommand(discountPolicyId, sellerId, request.targetIds());
    }
}
