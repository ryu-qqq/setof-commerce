package com.ryuqq.setof.domain.faq.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import com.ryuqq.setof.domain.faq.vo.FaqCategoryStatus;
import java.util.Map;

/**
 * FAQ 카테고리 상태 전이 예외
 *
 * <p>FAQ 카테고리 상태를 변경할 수 없을 때 발생합니다. HTTP 응답: 400 BAD REQUEST
 */
public class InvalidFaqCategoryStatusException extends DomainException {

    private InvalidFaqCategoryStatusException(
            FaqErrorCode errorCode, String message, Map<String, Object> args) {
        super(errorCode, message, args);
    }

    /**
     * 활성화 불가 예외 생성
     *
     * @param currentStatus 현재 상태
     * @return InvalidFaqCategoryStatusException
     */
    public static InvalidFaqCategoryStatusException cannotActivate(
            FaqCategoryStatus currentStatus) {
        return new InvalidFaqCategoryStatusException(
                FaqErrorCode.INVALID_FAQ_CATEGORY,
                String.format("현재 상태(%s)에서는 활성화할 수 없습니다", currentStatus.getDisplayName()),
                Map.of("currentStatus", currentStatus.name()));
    }

    /**
     * 비활성화 불가 예외 생성
     *
     * @param currentStatus 현재 상태
     * @return InvalidFaqCategoryStatusException
     */
    public static InvalidFaqCategoryStatusException cannotDeactivate(
            FaqCategoryStatus currentStatus) {
        return new InvalidFaqCategoryStatusException(
                FaqErrorCode.INVALID_FAQ_CATEGORY,
                String.format("현재 상태(%s)에서는 비활성화할 수 없습니다", currentStatus.getDisplayName()),
                Map.of("currentStatus", currentStatus.name()));
    }

    /**
     * 이미 활성 예외 생성
     *
     * @return InvalidFaqCategoryStatusException
     */
    public static InvalidFaqCategoryStatusException alreadyActive() {
        return new InvalidFaqCategoryStatusException(
                FaqErrorCode.INVALID_FAQ_CATEGORY, "이미 활성화된 FAQ 카테고리입니다", Map.of());
    }

    /**
     * 이미 비활성 예외 생성
     *
     * @return InvalidFaqCategoryStatusException
     */
    public static InvalidFaqCategoryStatusException alreadyInactive() {
        return new InvalidFaqCategoryStatusException(
                FaqErrorCode.INVALID_FAQ_CATEGORY, "이미 비활성화된 FAQ 카테고리입니다", Map.of());
    }

    /**
     * 이미 삭제됨 예외 생성
     *
     * @return InvalidFaqCategoryStatusException
     */
    public static InvalidFaqCategoryStatusException alreadyDeleted() {
        return new InvalidFaqCategoryStatusException(
                FaqErrorCode.INVALID_FAQ_CATEGORY, "이미 삭제된 FAQ 카테고리입니다", Map.of());
    }
}
