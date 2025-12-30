package com.ryuqq.setof.domain.cms.vo;

/**
 * Content 상태 Enum
 *
 * <p>컨텐츠의 라이프사이클 상태를 나타냅니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public enum ContentStatus {
    /** 초안 - 아직 게시되지 않은 상태 */
    DRAFT,
    /** 활성 - 현재 노출 중인 상태 */
    ACTIVE,
    /** 비활성 - 노출 중지된 상태 */
    INACTIVE,
    /** 삭제됨 - 소프트 삭제된 상태 */
    DELETED;

    /**
     * 활성화 가능 여부 확인
     *
     * @return 활성화 가능하면 true
     */
    public boolean canActivate() {
        return this == DRAFT || this == INACTIVE;
    }

    /**
     * 비활성화 가능 여부 확인
     *
     * @return 비활성화 가능하면 true
     */
    public boolean canDeactivate() {
        return this == ACTIVE;
    }

    /**
     * 삭제 가능 여부 확인
     *
     * @return 삭제 가능하면 true
     */
    public boolean canDelete() {
        return this != DELETED;
    }
}
