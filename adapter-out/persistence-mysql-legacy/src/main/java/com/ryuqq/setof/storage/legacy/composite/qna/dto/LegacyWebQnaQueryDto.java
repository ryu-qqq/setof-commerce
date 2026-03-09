package com.ryuqq.setof.storage.legacy.composite.qna.dto;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * LegacyWebQnaQueryDto - 레거시 상품 Q&A 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (@QueryProjection 금지).
 *
 * <p>GroupBy.groupBy(qna.id).list()로 Q&A 본문 + 답변 Set을 함께 집계합니다.
 *
 * <p>fetchProductQnas (2-step: 페이지 ID 조회 후 상세 조회) 패턴에 사용됩니다.
 *
 * @param qnaId Q&A ID
 * @param title Q&A 제목
 * @param content Q&A 내용
 * @param privateYn 비밀글 여부 (Y/N)
 * @param qnaStatus Q&A 상태
 * @param qnaType Q&A 유형 (PRODUCT / ORDER)
 * @param qnaDetailType Q&A 상세 유형
 * @param userType 사용자 유형
 * @param userId 작성자 사용자 ID
 * @param userName 작성자 이름
 * @param insertDate 등록일
 * @param updateDate 수정일
 * @param answers 답변 목록 (GroupBy.set으로 집계)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebQnaQueryDto(
        long qnaId,
        String title,
        String content,
        String privateYn,
        String qnaStatus,
        String qnaType,
        String qnaDetailType,
        String userType,
        long userId,
        String userName,
        LocalDateTime insertDate,
        LocalDateTime updateDate,
        Set<LegacyWebQnaAnswerQueryDto> answers) {

    public boolean isSecret() {
        return "Y".equals(privateYn);
    }
}
