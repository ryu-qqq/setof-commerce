package com.ryuqq.setof.storage.legacy.composite.qna.dto;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * LegacyWebMyOrderQnaQueryDto - 레거시 내 주문 Q&A 조회 Projection DTO.
 *
 * <p>Projections.constructor()로 매핑 (@QueryProjection 금지).
 *
 * <p>ORDER 타입 내 Q&A 조회 시 주문 정보(결제, 금액, 수량) + 상품 정보가 포함됩니다.
 *
 * <p>옵션 정보는 별도 쿼리로 조회 후 Mapper에서 병합합니다.
 *
 * @param qnaId Q&A ID
 * @param title Q&A 제목
 * @param content Q&A 내용
 * @param privateYn 비밀글 여부 (Y/N)
 * @param qnaStatus Q&A 상태
 * @param qnaType Q&A 유형
 * @param qnaDetailType Q&A 상세 유형
 * @param userType 사용자 유형
 * @param userId 작성자 사용자 ID
 * @param userName 작성자 이름
 * @param productGroupId 상품그룹 ID
 * @param productGroupName 상품그룹명
 * @param imageUrl 대표 이미지 URL
 * @param brandId 브랜드 ID
 * @param brandName 브랜드명
 * @param orderId 주문 ID
 * @param paymentId 결제 ID
 * @param orderAmount 주문 금액
 * @param quantity 주문 수량
 * @param insertDate 등록일
 * @param updateDate 수정일
 * @param answers 답변 목록 (GroupBy.set으로 집계)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record LegacyWebMyOrderQnaQueryDto(
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
        Long orderId,
        Long paymentId,
        Long orderAmount,
        Integer quantity,
        LocalDateTime insertDate,
        LocalDateTime updateDate,
        Set<LegacyWebQnaAnswerQueryDto> answers) {}
