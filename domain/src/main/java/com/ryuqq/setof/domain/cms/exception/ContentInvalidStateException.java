package com.ryuqq.setof.domain.cms.exception;

import com.ryuqq.setof.domain.common.exception.DomainException;
import java.util.Map;

/**
 * ContentInvalidStateException - Content 상태 전환 불가 시 발생
 *
 * <p>HTTP 응답: 400 BAD REQUEST
 *
 * @author development-team
 * @since 1.0.0
 */
public class ContentInvalidStateException extends DomainException {

    private ContentInvalidStateException(String message, Map<String, Object> args) {
        super(CmsErrorCode.INVALID_CONTENT_STATE, message, args);
    }

    /**
     * 활성화 불가 예외 생성
     *
     * @param contentId Content ID
     * @param currentStatus 현재 상태
     * @return ContentInvalidStateException 인스턴스
     */
    public static ContentInvalidStateException cannotActivate(
            Long contentId, String currentStatus) {
        return new ContentInvalidStateException(
                String.format(
                        "Cannot activate content %d. Current status: %s", contentId, currentStatus),
                Map.of(
                        "contentId",
                        contentId,
                        "currentStatus",
                        currentStatus,
                        "action",
                        "activate"));
    }

    /**
     * 비활성화 불가 예외 생성
     *
     * @param contentId Content ID
     * @param currentStatus 현재 상태
     * @return ContentInvalidStateException 인스턴스
     */
    public static ContentInvalidStateException cannotDeactivate(
            Long contentId, String currentStatus) {
        return new ContentInvalidStateException(
                String.format(
                        "Cannot deactivate content %d. Current status: %s",
                        contentId, currentStatus),
                Map.of(
                        "contentId",
                        contentId,
                        "currentStatus",
                        currentStatus,
                        "action",
                        "deactivate"));
    }

    /**
     * 삭제 불가 예외 생성
     *
     * @param contentId Content ID
     * @param currentStatus 현재 상태
     * @return ContentInvalidStateException 인스턴스
     */
    public static ContentInvalidStateException cannotDelete(Long contentId, String currentStatus) {
        return new ContentInvalidStateException(
                String.format(
                        "Cannot delete content %d. Current status: %s", contentId, currentStatus),
                Map.of("contentId", contentId, "currentStatus", currentStatus, "action", "delete"));
    }
}
