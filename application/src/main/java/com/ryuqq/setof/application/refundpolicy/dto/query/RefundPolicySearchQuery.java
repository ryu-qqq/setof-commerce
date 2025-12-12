package com.ryuqq.setof.application.refundpolicy.dto.query;

/**
 * 환불 정책 검색 Query DTO
 *
 * @param sellerId 셀러 ID
 * @param includeDeleted 삭제된 정책 포함 여부
 * @author development-team
 * @since 1.0.0
 */
public record RefundPolicySearchQuery(Long sellerId, boolean includeDeleted) {}
