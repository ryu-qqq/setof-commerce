package com.ryuqq.setof.adapter.out.persistence.board.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * BoardJpaEntity - Board JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 cms_boards 테이블과 매핑됩니다.
 *
 * <p>Lombok 금지 - Plain Java 사용
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "cms_boards")
public class BoardJpaEntity extends SoftDeletableEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** 게시판 타입 (NOTICE, EVENT, NEWS) */
    @Column(name = "board_type", nullable = false, length = 30)
    private String boardType;

    /** 제목 */
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    /** 본문 */
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    /** 요약 */
    @Column(name = "summary", length = 500)
    private String summary;

    /** 썸네일 URL */
    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    /** 상단 고정 여부 */
    @Column(name = "is_pinned", nullable = false)
    private boolean pinned;

    /** 상단 고정 순서 */
    @Column(name = "pin_order", nullable = false)
    private int pinOrder;

    /** 노출 시작일시 */
    @Column(name = "display_start_at")
    private Instant displayStartAt;

    /** 노출 종료일시 */
    @Column(name = "display_end_at")
    private Instant displayEndAt;

    /** 상태 (DRAFT, PUBLISHED, HIDDEN) */
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    /** 조회수 */
    @Column(name = "view_count", nullable = false)
    private long viewCount;

    /** 생성자 ID */
    @Column(name = "created_by")
    private Long createdBy;

    /** 수정자 ID */
    @Column(name = "updated_by")
    private Long updatedBy;

    /** JPA 기본 생성자 (protected) */
    protected BoardJpaEntity() {
        // JPA 기본 생성자
    }

    /** 전체 필드 생성자 (private) */
    private BoardJpaEntity(
            Long id,
            String boardType,
            String title,
            String content,
            String summary,
            String thumbnailUrl,
            boolean pinned,
            int pinOrder,
            Instant displayStartAt,
            Instant displayEndAt,
            String status,
            long viewCount,
            Long createdBy,
            Long updatedBy,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.boardType = boardType;
        this.title = title;
        this.content = content;
        this.summary = summary;
        this.thumbnailUrl = thumbnailUrl;
        this.pinned = pinned;
        this.pinOrder = pinOrder;
        this.displayStartAt = displayStartAt;
        this.displayEndAt = displayEndAt;
        this.status = status;
        this.viewCount = viewCount;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    /** of() 스태틱 팩토리 메서드 */
    public static BoardJpaEntity of(
            Long id,
            String boardType,
            String title,
            String content,
            String summary,
            String thumbnailUrl,
            boolean pinned,
            int pinOrder,
            Instant displayStartAt,
            Instant displayEndAt,
            String status,
            long viewCount,
            Long createdBy,
            Long updatedBy,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new BoardJpaEntity(
                id,
                boardType,
                title,
                content,
                summary,
                thumbnailUrl,
                pinned,
                pinOrder,
                displayStartAt,
                displayEndAt,
                status,
                viewCount,
                createdBy,
                updatedBy,
                createdAt,
                updatedAt,
                deletedAt);
    }

    // ===== Getters =====

    public Long getId() {
        return id;
    }

    public String getBoardType() {
        return boardType;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getSummary() {
        return summary;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public boolean isPinned() {
        return pinned;
    }

    public int getPinOrder() {
        return pinOrder;
    }

    public Instant getDisplayStartAt() {
        return displayStartAt;
    }

    public Instant getDisplayEndAt() {
        return displayEndAt;
    }

    public String getStatus() {
        return status;
    }

    public long getViewCount() {
        return viewCount;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }
}
