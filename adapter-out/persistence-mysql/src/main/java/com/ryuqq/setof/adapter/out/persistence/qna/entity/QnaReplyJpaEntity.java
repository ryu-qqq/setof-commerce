package com.ryuqq.setof.adapter.out.persistence.qna.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * QnaReplyJpaEntity - QnA 답변 JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 qna_replies 테이블과 매핑됩니다.
 *
 * <p><strong>Materialized Path 패턴:</strong>
 *
 * <ul>
 *   <li>path 컬럼으로 계층 구조 표현 (예: "001.002.001")
 *   <li>무제한 답변 depth 지원
 *   <li>path로 정렬하면 자연스러운 트리 순서
 * </ul>
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>qnaId: QnA 외래키 (Long)
 *   <li>parentReplyId: 부모 답변 외래키 (Long, nullable)
 *   <li>JPA 관계 어노테이션 금지
 * </ul>
 *
 * <p><strong>Lombok 금지:</strong>
 *
 * <ul>
 *   <li>Plain Java getter 사용
 *   <li>Setter 제공 금지
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "qna_replies")
public class QnaReplyJpaEntity extends SoftDeletableEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** QnA ID (Long FK) */
    @Column(name = "qna_id", nullable = false)
    private Long qnaId;

    /** 부모 답변 ID (Long FK, nullable - 루트 답변은 null) */
    @Column(name = "parent_reply_id")
    private Long parentReplyId;

    /** 작성자 ID (UUIDv7 문자열) */
    @Column(name = "writer_id", nullable = false, length = 36)
    private String writerId;

    /** 작성자 유형 (SELLER, CUSTOMER, ADMIN) */
    @Column(name = "writer_type", nullable = false, length = 20)
    private String writerType;

    /** 작성자 이름 */
    @Column(name = "writer_name", nullable = false, length = 50)
    private String writerName;

    /** 답변 내용 */
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    /** Materialized Path (예: "001.002.001") */
    @Column(name = "path", nullable = false, length = 500)
    private String path;

    /**
     * JPA 기본 생성자 (protected)
     *
     * <p>JPA 스펙 요구사항으로 반드시 필요합니다.
     */
    protected QnaReplyJpaEntity() {
        // JPA 기본 생성자
    }

    /**
     * 전체 필드 생성자 (private)
     *
     * <p>직접 호출 금지, of() 스태틱 메서드로만 생성하세요.
     */
    private QnaReplyJpaEntity(
            Long id,
            Long qnaId,
            Long parentReplyId,
            String writerId,
            String writerType,
            String writerName,
            String content,
            String path,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.qnaId = qnaId;
        this.parentReplyId = parentReplyId;
        this.writerId = writerId;
        this.writerType = writerType;
        this.writerName = writerName;
        this.content = content;
        this.path = path;
    }

    /**
     * of() 스태틱 팩토리 메서드 (Mapper 전용)
     *
     * <p>Entity 생성은 반드시 이 메서드를 통해서만 가능합니다.
     *
     * @param id 답변 ID (null이면 신규 생성)
     * @param qnaId QnA ID
     * @param parentReplyId 부모 답변 ID (nullable)
     * @param writerId 작성자 ID (UUID 문자열)
     * @param writerType 작성자 유형
     * @param writerName 작성자 이름
     * @param content 답변 내용
     * @param path Materialized Path
     * @param createdAt 생성 일시
     * @param updatedAt 수정 일시
     * @param deletedAt 삭제 일시
     * @return QnaReplyJpaEntity 인스턴스
     */
    public static QnaReplyJpaEntity of(
            Long id,
            Long qnaId,
            Long parentReplyId,
            String writerId,
            String writerType,
            String writerName,
            String content,
            String path,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new QnaReplyJpaEntity(
                id,
                qnaId,
                parentReplyId,
                writerId,
                writerType,
                writerName,
                content,
                path,
                createdAt,
                updatedAt,
                deletedAt);
    }

    // ===== Getters (Setter 제공 금지) =====

    public Long getId() {
        return id;
    }

    public Long getQnaId() {
        return qnaId;
    }

    public Long getParentReplyId() {
        return parentReplyId;
    }

    public String getWriterId() {
        return writerId;
    }

    public String getWriterType() {
        return writerType;
    }

    public String getWriterName() {
        return writerName;
    }

    public String getContent() {
        return content;
    }

    public String getPath() {
        return path;
    }

    // ===== Update Methods =====

    /**
     * 답변 내용 수정
     *
     * @param newContent 새 답변 내용
     */
    public void updateContent(String newContent) {
        this.content = newContent;
    }
}
