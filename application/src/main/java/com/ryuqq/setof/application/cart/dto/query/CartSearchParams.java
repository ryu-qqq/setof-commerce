package com.ryuqq.setof.application.cart.dto.query;

/**
 * 장바구니 목록 조회 파라미터 DTO.
 *
 * <p>커서 기반 페이징 파라미터를 포함합니다.
 *
 * @param memberId 회원 ID (UUIDv7, 새 DB 호환)
 * @param userId 사용자 ID (레거시, SecurityContext에서 추출)
 * @param lastCartId 커서 페이징용 마지막 장바구니 ID (null이면 첫 페이지)
 * @param size 페이지 크기 (기본: 20)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record CartSearchParams(String memberId, Long userId, Long lastCartId, Integer size) {

    private static final int DEFAULT_SIZE = 20;

    public CartSearchParams {
        if (size == null || size <= 0) {
            size = DEFAULT_SIZE;
        }
    }

    public static CartSearchParams of(String memberId, Long userId, Long lastCartId, Integer size) {
        return new CartSearchParams(memberId, userId, lastCartId, size);
    }

    public static CartSearchParams forCount(String memberId, Long userId) {
        return new CartSearchParams(memberId, userId, null, null);
    }

    public int pageSize() {
        return size != null ? size : DEFAULT_SIZE;
    }
}
