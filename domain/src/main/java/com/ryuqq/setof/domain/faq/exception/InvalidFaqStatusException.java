package com.ryuqq.setof.domain.faq.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import com.ryuqq.setof.domain.faq.vo.FaqStatus;
import java.util.Map;

/**
 * FAQ 상태 전이 예외
 *
 * <p>FAQ 상태를 변경할 수 없을 때 발생합니다. HTTP 응답: 400 BAD REQUEST
 */
public class InvalidFaqStatusException extends DomainException {

    private InvalidFaqStatusException(
            FaqErrorCode errorCode, String message, Map<String, Object> args) {
        super(errorCode, message, args);
    }

    /**
     * 게시 불가 예외 생성
     *
     * @param currentStatus 현재 상태
     * @return InvalidFaqStatusException
     */
    public static InvalidFaqStatusException cannotPublish(FaqStatus currentStatus) {
        return new InvalidFaqStatusException(
                FaqErrorCode.FAQ_CANNOT_PUBLISH,
                String.format("현재 상태(%s)에서는 게시할 수 없습니다", currentStatus.getDisplayName()),
                Map.of("currentStatus", currentStatus.name()));
    }

    /**
     * 숨김 불가 예외 생성
     *
     * @param currentStatus 현재 상태
     * @return InvalidFaqStatusException
     */
    public static InvalidFaqStatusException cannotHide(FaqStatus currentStatus) {
        return new InvalidFaqStatusException(
                FaqErrorCode.FAQ_CANNOT_HIDE,
                String.format("현재 상태(%s)에서는 숨김 처리할 수 없습니다", currentStatus.getDisplayName()),
                Map.of("currentStatus", currentStatus.name()));
    }

    /**
     * 이미 게시됨 예외 생성
     *
     * @return InvalidFaqStatusException
     */
    public static InvalidFaqStatusException alreadyPublished() {
        return new InvalidFaqStatusException(
                FaqErrorCode.FAQ_ALREADY_PUBLISHED, "이미 게시된 FAQ입니다", Map.of());
    }

    /**
     * 이미 숨김 예외 생성
     *
     * @return InvalidFaqStatusException
     */
    public static InvalidFaqStatusException alreadyHidden() {
        return new InvalidFaqStatusException(
                FaqErrorCode.FAQ_ALREADY_HIDDEN, "이미 숨김 처리된 FAQ입니다", Map.of());
    }

    /**
     * 이미 삭제됨 예외 생성
     *
     * @return InvalidFaqStatusException
     */
    public static InvalidFaqStatusException alreadyDeleted() {
        return new InvalidFaqStatusException(
                FaqErrorCode.FAQ_ALREADY_DELETED, "이미 삭제된 FAQ입니다", Map.of());
    }
}
