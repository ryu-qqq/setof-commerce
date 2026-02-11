package com.ryuqq.setof.adapter.in.rest.v1.board;

/**
 * BoardV1Endpoints - 공지사항 V1 Public API 엔드포인트 상수.
 *
 * <p>API-END-001: Endpoints final class + private 생성자.
 *
 * <p>API-END-002: static final 상수.
 *
 * <p>레거시 NewsController 경로 호환: /api/v1/board
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class BoardV1Endpoints {

    private BoardV1Endpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** V1 기본 경로 */
    public static final String BASE_V1 = "/api/v1";

    /** 공지사항 목록 조회 경로 (GET /api/v1/board) */
    public static final String BOARDS = BASE_V1 + "/board";
}
