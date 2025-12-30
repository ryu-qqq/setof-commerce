package com.ryuqq.setof.application.qna.dto.command;

/**
 * Create Product QnA Command
 *
 * <p>상품 문의 생성 요청 데이터를 담는 순수한 불변 객체
 *
 * @param productGroupId 상품 그룹 ID
 * @param writerId 작성자 ID (UUID 문자열)
 * @param writerType 작성자 유형 (MEMBER, GUEST)
 * @param writerName 작성자 이름
 * @param title 문의 제목
 * @param content 문의 내용
 * @param detailType 문의 세부 유형
 * @param isSecret 비밀글 여부
 * @author development-team
 * @since 1.0.0
 */
public record CreateProductQnaCommand(
        Long productGroupId,
        String writerId,
        String writerType,
        String writerName,
        String title,
        String content,
        String detailType,
        boolean isSecret) {}
