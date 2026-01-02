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
 * QnaJpaEntity - QnA JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 qnas 테이블과 매핑됩니다.
 *
 * <p><strong>QnA 유형:</strong>
 *
 * <ul>
 *   <li>PRODUCT: 상품 문의 (이미지 첨부 불가)
 *   <li>ORDER: 주문 문의 (이미지 최대 3장)
 * </ul>
 *
 * <p><strong>SoftDeletableEntity 상속:</strong>
 *
 * <ul>
 *   <li>공통 감사 필드 상속: createdAt, updatedAt, deletedAt
 *   <li>Soft Delete 지원
 * </ul>
 *
 * <p><strong>Lombok 금지:</strong>
 *
 * <ul>
 *   <li>Plain Java getter 사용
 *   <li>Setter 제공 금지
 *   <li>명시적 생성자 제공
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Entity
@Table(name = "qnas")
public class QnaJpaEntity extends SoftDeletableEntity {

    /** 기본 키 (Auto Increment) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /** QnA 유형 (PRODUCT, ORDER) */
    @Column(name = "qna_type", nullable = false, length = 20)
    private String qnaType;

    /** 상세 유형 (SIZE, DELIVERY, STOCK 등) */
    @Column(name = "detail_type", nullable = false, length = 30)
    private String detailType;

    /** 대상 ID (상품 그룹 ID 또는 주문 ID) */
    @Column(name = "target_id", nullable = false)
    private Long targetId;

    /** 작성자 ID (UUIDv7 문자열) */
    @Column(name = "writer_id", nullable = false, length = 36)
    private String writerId;

    /** 작성자 유형 (MEMBER, GUEST) */
    @Column(name = "writer_type", nullable = false, length = 20)
    private String writerType;

    /** 작성자 이름 */
    @Column(name = "writer_name", nullable = false, length = 50)
    private String writerName;

    /** 문의 제목 */
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    /** 문의 내용 */
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    /** 비밀글 여부 */
    @Column(name = "is_secret", nullable = false)
    private boolean isSecret;

    /** 상태 (OPEN, CLOSED) */
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    /** 답변 수 */
    @Column(name = "reply_count", nullable = false)
    private int replyCount;

    /**
     * JPA 기본 생성자 (protected)
     *
     * <p>JPA 스펙 요구사항으로 반드시 필요합니다.
     */
    protected QnaJpaEntity() {
        // JPA 기본 생성자
    }

    /**
     * 전체 필드 생성자 (private)
     *
     * <p>직접 호출 금지, of() 스태틱 메서드로만 생성하세요.
     */
    private QnaJpaEntity(
            Long id,
            String qnaType,
            String detailType,
            Long targetId,
            String writerId,
            String writerType,
            String writerName,
            String title,
            String content,
            boolean isSecret,
            String status,
            int replyCount,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.qnaType = qnaType;
        this.detailType = detailType;
        this.targetId = targetId;
        this.writerId = writerId;
        this.writerType = writerType;
        this.writerName = writerName;
        this.title = title;
        this.content = content;
        this.isSecret = isSecret;
        this.status = status;
        this.replyCount = replyCount;
    }

    /**
     * of() 스태틱 팩토리 메서드 (Mapper 전용)
     *
     * <p>Entity 생성은 반드시 이 메서드를 통해서만 가능합니다.
     *
     * @param id QnA ID (null이면 신규 생성)
     * @param qnaType QnA 유형
     * @param detailType 상세 유형
     * @param targetId 대상 ID
     * @param writerId 작성자 ID (UUID 문자열)
     * @param writerType 작성자 유형
     * @param writerName 작성자 이름
     * @param title 제목
     * @param content 내용
     * @param isSecret 비밀글 여부
     * @param status 상태
     * @param replyCount 답변 수
     * @param createdAt 생성 일시
     * @param updatedAt 수정 일시
     * @param deletedAt 삭제 일시
     * @return QnaJpaEntity 인스턴스
     */
    public static QnaJpaEntity of(
            Long id,
            String qnaType,
            String detailType,
            Long targetId,
            String writerId,
            String writerType,
            String writerName,
            String title,
            String content,
            boolean isSecret,
            String status,
            int replyCount,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new QnaJpaEntity(
                id,
                qnaType,
                detailType,
                targetId,
                writerId,
                writerType,
                writerName,
                title,
                content,
                isSecret,
                status,
                replyCount,
                createdAt,
                updatedAt,
                deletedAt);
    }

    // ===== Getters (Setter 제공 금지) =====

    public Long getId() {
        return id;
    }

    public String getQnaType() {
        return qnaType;
    }

    public String getDetailType() {
        return detailType;
    }

    public Long getTargetId() {
        return targetId;
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

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public boolean isSecret() {
        return isSecret;
    }

    public String getStatus() {
        return status;
    }

    public int getReplyCount() {
        return replyCount;
    }
}
