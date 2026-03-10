package com.ryuqq.setof.application.payment.dto.response;

import java.util.List;

/**
 * PaymentSliceResult - 결제 목록 슬라이스 Application Result DTO.
 *
 * <p>레거시 CustomSlice 호환 형태로 페이징 메타데이터를 포함합니다.
 *
 * <p>APP-DTO-001: Application Result는 Record로 정의.
 *
 * @param content 결제 개요 목록
 * @param last 마지막 페이지 여부
 * @param first 첫 페이지 여부
 * @param number 페이지 번호 (커서 기반이므로 항상 0)
 * @param size 요청한 페이지 크기
 * @param numberOfElements 현재 페이지 요소 수
 * @param empty 비어있는지 여부
 * @param lastDomainId 마지막 도메인 ID (커서)
 * @param cursorValue 커서 값 (문자열)
 * @param totalElements 전체 요소 수
 * @author ryu-qqq
 * @since 1.1.0
 */
public record PaymentSliceResult(
        List<PaymentOverviewResult> content,
        boolean last,
        boolean first,
        int number,
        int size,
        int numberOfElements,
        boolean empty,
        Long lastDomainId,
        String cursorValue,
        Long totalElements) {}
