package com.ryuqq.setof.domain.contentpage.id;

/**
 * ContentPageId - 콘텐츠 페이지 식별자 VO.
 *
 * <p>DOM-ID-001: record 기반, of() 팩터리.
 *
 * <p>DOM-ID-002: forNew() (새 생성), isNew() (영속 전 여부).
 *
 * @param value 콘텐츠 페이지 ID 값 (null = 미영속)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record ContentPageId(Long value) {

    public static ContentPageId of(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("ContentPageId must be positive");
        }
        return new ContentPageId(value);
    }

    public static ContentPageId forNew() {
        return new ContentPageId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
