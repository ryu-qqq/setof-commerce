package com.ryuqq.setof.application.discount.dto.command;

import java.util.List;

/**
 * 할인 정책 사용 상태 일괄 변경 Command DTO.
 *
 * <p>복수의 할인 정책을 일괄로 활성화/비활성화할 때 사용합니다.
 *
 * @param policyIds 상태를 변경할 할인 정책 ID 목록
 * @param active true면 활성화, false면 비활성화
 * @author ryu-qqq
 * @since 1.1.0
 */
public record UpdateDiscountPolicyStatusCommand(List<Long> policyIds, boolean active) {}
