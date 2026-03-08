package com.ryuqq.setof.storage.legacy.composite.web.qna.dto;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * LegacyWebMyQnaQueryDto - 레거시 내 Q&A 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (@QueryProjection 금지).
 *
 * <p>PRODUCT/ORDER 유형 모두 이 DTO를 사용하며, qnaType에 따라 targetDto 해석이 달라집니다.
 *
 * <p>fetchMyQnas (커서 기반 슬라이스) 패턴에 사용됩니다.
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
 * @param productGroupId 상품그룹 ID (PRODUCT 전용)
 * @param productGroupName 상품그룹명 (PRODUCT 전용)
 * @param imageUrl 대표 이미지 URL (PRODUCT 전용)
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명
 * @param insertDate 등록일
 * @param updateDate 수정일
 * @param answers 답변 목록 (GroupBy.set으로 집계)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebMyQnaQueryDto(
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
        Long productGroupId,
        String productGroupName,
        String imageUrl,
        Long brandId,
        String brandName,
        LocalDateTime insertDate,
        LocalDateTime updateDate,
        Set<LegacyWebQnaAnswerQueryDto> answers) {

    public boolean isSecret() {
        return "Y".equals(privateYn);
    }

    public boolean isProductQna() {
        return "PRODUCT".equals(qnaType);
    }
}
