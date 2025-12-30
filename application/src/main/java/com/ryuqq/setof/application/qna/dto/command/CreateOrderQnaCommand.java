package com.ryuqq.setof.application.qna.dto.command;

import java.util.List;

/**
 * Create Order QnA Command
 *
 * <p>주문 문의 생성 요청 데이터를 담는 순수한 불변 객체
 *
 * @param orderId 주문 ID
 * @param writerId 작성자 ID (UUID 문자열)
 * @param writerType 작성자 유형 (MEMBER, GUEST)
 * @param writerName 작성자 이름
 * @param title 문의 제목
 * @param content 문의 내용
 * @param detailType 문의 세부 유형
 * @param isSecret 비밀글 여부
 * @param imageUrls 이미지 URL 목록 (최대 3개)
 * @author development-team
 * @since 1.0.0
 */
public record CreateOrderQnaCommand(
        Long orderId,
        String writerId,
        String writerType,
        String writerName,
        String title,
        String content,
        String detailType,
        boolean isSecret,
        List<String> imageUrls) {}
