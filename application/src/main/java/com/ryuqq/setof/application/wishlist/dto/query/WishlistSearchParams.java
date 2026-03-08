package com.ryuqq.setof.application.wishlist.dto.query;

/**
 * 찜 목록 조회 파라미터 DTO.
 *
 * <p>커서 기반 페이징 파라미터를 포함합니다.
 *
 * @param userId 사용자 ID (레거시, SecurityContext에서 추출)
 * @param lastDomainId 커서 페이징용 마지막 찜 ID (null이면 첫 페이지)
 * @param size 페이지 크기 (기본: 20)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record WishlistSearchParams(Long userId, Long lastDomainId, Integer size) {

    private static final int DEFAULT_SIZE = 20;

    public WishlistSearchParams {
        if (size == null || size <= 0) {
            size = DEFAULT_SIZE;
        }
    }

    public static WishlistSearchParams of(Long userId, Long lastDomainId, Integer size) {
        return new WishlistSearchParams(userId, lastDomainId, size);
    }

    public int pageSize() {
        return size != null ? size : DEFAULT_SIZE;
    }
}
