package com.ryuqq.setof.application.seller.dto.command;

/**
 * Delete Seller Command
 *
 * <p>셀러 삭제(소프트 삭제) 요청 데이터를 담는 순수한 불변 객체
 *
 * @param sellerId 셀러 ID
 * @author development-team
 * @since 1.0.0
 */
public record DeleteSellerCommand(Long sellerId) {}
