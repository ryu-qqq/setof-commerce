package com.ryuqq.setof.domain.board.exception;

import com.ryuqq.setof.domain.board.vo.BoardStatus;
import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * 게시물 상태 전이 예외
 *
 * <p>게시물 상태를 변경할 수 없을 때 발생합니다. HTTP 응답: 400 BAD REQUEST
 */
public class InvalidBoardStatusException extends DomainException {

    private InvalidBoardStatusException(
            BoardErrorCode errorCode, String message, Map<String, Object> args) {
        super(errorCode, message, args);
    }

    /**
     * 게시 불가 예외 생성
     *
     * @param currentStatus 현재 상태
     * @return InvalidBoardStatusException
     */
    public static InvalidBoardStatusException cannotPublish(BoardStatus currentStatus) {
        return new InvalidBoardStatusException(
                BoardErrorCode.BOARD_CANNOT_PUBLISH,
                String.format("현재 상태(%s)에서는 게시할 수 없습니다", currentStatus.getDisplayName()),
                Map.of("currentStatus", currentStatus.name()));
    }

    /**
     * 숨김 불가 예외 생성
     *
     * @param currentStatus 현재 상태
     * @return InvalidBoardStatusException
     */
    public static InvalidBoardStatusException cannotHide(BoardStatus currentStatus) {
        return new InvalidBoardStatusException(
                BoardErrorCode.BOARD_CANNOT_HIDE,
                String.format("현재 상태(%s)에서는 숨김 처리할 수 없습니다", currentStatus.getDisplayName()),
                Map.of("currentStatus", currentStatus.name()));
    }

    /**
     * 이미 게시됨 예외 생성
     *
     * @return InvalidBoardStatusException
     */
    public static InvalidBoardStatusException alreadyPublished() {
        return new InvalidBoardStatusException(
                BoardErrorCode.BOARD_ALREADY_PUBLISHED, "이미 게시된 게시물입니다", Map.of());
    }

    /**
     * 이미 숨김 예외 생성
     *
     * @return InvalidBoardStatusException
     */
    public static InvalidBoardStatusException alreadyHidden() {
        return new InvalidBoardStatusException(
                BoardErrorCode.BOARD_ALREADY_HIDDEN, "이미 숨김 처리된 게시물입니다", Map.of());
    }

    /**
     * 이미 삭제됨 예외 생성
     *
     * @return InvalidBoardStatusException
     */
    public static InvalidBoardStatusException alreadyDeleted() {
        return new InvalidBoardStatusException(
                BoardErrorCode.BOARD_ALREADY_DELETED, "이미 삭제된 게시물입니다", Map.of());
    }
}
