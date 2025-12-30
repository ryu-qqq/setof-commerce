package com.ryuqq.setof.domain.cms.vo;

/**
 * Banner 상태 Enum
 *
 * @author development-team
 * @since 1.0.0
 */
public enum BannerStatus {
    /** 활성 */
    ACTIVE,
    /** 비활성 */
    INACTIVE,
    /** 삭제됨 */
    DELETED;

    /**
     * 활성 상태인지 확인
     *
     * @return 활성이면 true
     */
    public boolean isActive() {
        return this == ACTIVE;
    }
}
