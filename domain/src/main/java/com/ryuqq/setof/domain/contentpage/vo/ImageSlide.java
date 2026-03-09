package com.ryuqq.setof.domain.contentpage.vo;

/**
 * ImageSlide - 이미지 슬라이드 VO.
 *
 * @param imageComponentItemId 이미지 컴포넌트 아이템 ID
 * @param displayOrder 노출 순서
 * @param imageUrl 이미지 URL
 * @param linkUrl 클릭 시 이동 URL
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ImageSlide(
        long imageComponentItemId, int displayOrder, String imageUrl, String linkUrl) {}
