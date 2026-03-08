package com.ryuqq.setof.application.wishlist.dto.command;

/**
 * 찜 항목 추가 Command DTO.
 *
 * <p>레거시 CreateUserFavorite 기반 변환.
 *
 * @param userId 회원 ID (SecurityContext에서 추출)
 * @param productGroupId 찜할 상품 그룹 ID
 * @author ryu-qqq
 * @since 1.1.0
 */
public record AddWishlistItemCommand(Long userId, long productGroupId) {}
