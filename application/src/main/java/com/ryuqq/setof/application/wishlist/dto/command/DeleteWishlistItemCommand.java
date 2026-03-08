package com.ryuqq.setof.application.wishlist.dto.command;

/**
 * 찜 항목 삭제 Command DTO.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public record DeleteWishlistItemCommand(Long userId, long productGroupId) {}
