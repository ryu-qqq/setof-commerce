package com.ryuqq.setof.storage.legacy.qna.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * LegacyQnaContents - 레거시 Q&A 내용 Embedded 클래스.
 *
 * <p>레거시 DB의 qna 테이블의 title, content 컬럼 매핑.
 *
 * <p>PER-ENT-001: 엔티티는 JPA 표준 어노테이션만 사용.
 *
 * <p>PER-ENT-003: Lombok 사용 금지 (Zero-Tolerance).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Embeddable
public class LegacyQnaContents {

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    protected LegacyQnaContents() {}

    public LegacyQnaContents(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
