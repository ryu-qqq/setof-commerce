package com.ryuqq.setof.domain.board.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * Board 에러 코드 Enum
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>ErrorCode 인터페이스 구현 필수
 *   <li>Spring HttpStatus 사용 금지 (int 타입 사용)
 *   <li>Lombok 금지
 * </ul>
 */
public enum BoardErrorCode implements ErrorCode {
    BOARD_NOT_FOUND("BOARD-001", 404, "게시물을 찾을 수 없습니다"),
    INVALID_BOARD_ID("BOARD-002", 400, "유효하지 않은 게시물 ID입니다"),
    INVALID_BOARD_STATUS("BOARD-003", 400, "유효하지 않은 게시물 상태입니다"),
    INVALID_BOARD_TYPE("BOARD-004", 400, "유효하지 않은 게시판 타입입니다"),
    INVALID_BOARD_CONTENT("BOARD-005", 400, "유효하지 않은 게시물 내용입니다"),
    INVALID_DISPLAY_PERIOD("BOARD-006", 400, "유효하지 않은 노출 기간입니다"),
    BOARD_ALREADY_PUBLISHED("BOARD-007", 409, "이미 게시된 게시물입니다"),
    BOARD_ALREADY_HIDDEN("BOARD-008", 409, "이미 숨김 처리된 게시물입니다"),
    BOARD_CANNOT_PUBLISH("BOARD-009", 400, "게시할 수 없는 상태입니다"),
    BOARD_CANNOT_HIDE("BOARD-010", 400, "숨김 처리할 수 없는 상태입니다"),
    BOARD_ALREADY_DELETED("BOARD-011", 410, "이미 삭제된 게시물입니다");

    private final String code;
    private final int httpStatus;
    private final String message;

    BoardErrorCode(String code, int httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
