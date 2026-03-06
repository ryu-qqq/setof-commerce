package com.ryuqq.setof.domain.contentpage.vo;

/**
 * ImageSlide - 이미지 슬라이드 VO.
 *
 * @param imageUrl 이미지 URL
 * @param linkUrl 클릭 시 이동 URL
 * @param displayOrder 노출 순서
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ImageSlide(String imageUrl, String linkUrl, int displayOrder) {}
