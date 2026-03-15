package com.ryuqq.setof.application.discount;

import com.ryuqq.setof.application.discount.dto.command.CreateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.ModifyDiscountTargetsCommand;
import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountPolicyStatusCommand;
import java.time.Instant;
import java.util.List;

/**
 * Discount Application Command 테스트 Fixtures.
 *
 * <p>할인 정책 생성/수정/상태변경/타겟수정 Command 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class DiscountCommandFixtures {

    private static final Instant FIXED_START = Instant.parse("2025-01-01T00:00:00Z");
    private static final Instant FIXED_END = Instant.parse("2025-12-31T23:59:59Z");

    private DiscountCommandFixtures() {}

    // ===== CreateDiscountPolicyCommand =====

    public static CreateDiscountPolicyCommand createRateCommand() {
        return new CreateDiscountPolicyCommand(
                "테스트할인정책",
                "테스트용 할인 정책입니다",
                "RATE",
                10.0,
                null,
                50000,
                true,
                10000,
                "INSTANT",
                "ADMIN",
                null,
                "PLATFORM_INSTANT",
                50,
                FIXED_START,
                FIXED_END,
                1000000,
                List.of(targetItem("PRODUCT", 300L)));
    }

    public static CreateDiscountPolicyCommand createFixedAmountCommand() {
        return new CreateDiscountPolicyCommand(
                "정액할인정책",
                "정액 할인 정책입니다",
                "FIXED_AMOUNT",
                null,
                5000,
                null,
                false,
                null,
                "INSTANT",
                "SELLER",
                1L,
                "SELLER_INSTANT",
                50,
                FIXED_START,
                FIXED_END,
                1000000,
                List.of());
    }

    public static CreateDiscountPolicyCommand createCommandWithoutTargets() {
        return new CreateDiscountPolicyCommand(
                "타겟없는정책",
                null,
                "RATE",
                15.0,
                null,
                null,
                false,
                null,
                "COUPON",
                "ADMIN",
                null,
                "COUPON",
                30,
                FIXED_START,
                FIXED_END,
                500000,
                null);
    }

    public static CreateDiscountPolicyCommand createCommandWithMultipleTargets() {
        return new CreateDiscountPolicyCommand(
                "다중타겟정책",
                "다중 타겟 할인 정책입니다",
                "RATE",
                20.0,
                null,
                30000,
                true,
                20000,
                "INSTANT",
                "ADMIN",
                null,
                "PLATFORM_INSTANT",
                70,
                FIXED_START,
                FIXED_END,
                2000000,
                List.of(
                        targetItem("PRODUCT", 100L),
                        targetItem("PRODUCT", 200L),
                        targetItem("PRODUCT", 300L)));
    }

    public static CreateDiscountPolicyCommand.TargetItem targetItem(
            String targetType, long targetId) {
        return new CreateDiscountPolicyCommand.TargetItem(targetType, targetId);
    }

    // ===== UpdateDiscountPolicyCommand =====

    public static UpdateDiscountPolicyCommand updateCommand() {
        return updateCommand(1L);
    }

    public static UpdateDiscountPolicyCommand updateCommand(long policyId) {
        return new UpdateDiscountPolicyCommand(
                policyId,
                "수정된할인정책",
                "수정된 설명입니다",
                "RATE",
                15.0,
                null,
                30000,
                true,
                20000,
                70,
                FIXED_START,
                FIXED_END,
                2000000);
    }

    // ===== UpdateDiscountPolicyStatusCommand =====

    public static UpdateDiscountPolicyStatusCommand activateCommand() {
        return new UpdateDiscountPolicyStatusCommand(List.of(1L, 2L, 3L), true);
    }

    public static UpdateDiscountPolicyStatusCommand activateCommand(List<Long> policyIds) {
        return new UpdateDiscountPolicyStatusCommand(policyIds, true);
    }

    public static UpdateDiscountPolicyStatusCommand deactivateCommand() {
        return new UpdateDiscountPolicyStatusCommand(List.of(1L, 2L), false);
    }

    public static UpdateDiscountPolicyStatusCommand deactivateCommand(List<Long> policyIds) {
        return new UpdateDiscountPolicyStatusCommand(policyIds, false);
    }

    public static UpdateDiscountPolicyStatusCommand singleActivateCommand(long policyId) {
        return new UpdateDiscountPolicyStatusCommand(List.of(policyId), true);
    }

    // ===== ModifyDiscountTargetsCommand =====

    public static ModifyDiscountTargetsCommand modifyTargetsCommand() {
        return modifyTargetsCommand(1L);
    }

    public static ModifyDiscountTargetsCommand modifyTargetsCommand(long policyId) {
        return new ModifyDiscountTargetsCommand(policyId, "PRODUCT", List.of(100L, 200L, 300L));
    }

    public static ModifyDiscountTargetsCommand modifyTargetsCommandWithEmptyTargets(long policyId) {
        return new ModifyDiscountTargetsCommand(policyId, "PRODUCT", List.of());
    }
}
