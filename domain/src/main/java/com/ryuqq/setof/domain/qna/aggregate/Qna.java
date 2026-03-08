package com.ryuqq.setof.domain.qna.aggregate;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.qna.exception.QnaAlreadyAnsweredException;
import com.ryuqq.setof.domain.qna.exception.QnaAlreadyClosedException;
import com.ryuqq.setof.domain.qna.id.LegacyQnaId;
import com.ryuqq.setof.domain.qna.id.QnaId;
import com.ryuqq.setof.domain.qna.vo.QnaContent;
import com.ryuqq.setof.domain.qna.vo.QnaDetailType;
import com.ryuqq.setof.domain.qna.vo.QnaStatus;
import com.ryuqq.setof.domain.qna.vo.QnaTitle;
import com.ryuqq.setof.domain.qna.vo.QnaType;
import com.ryuqq.setof.domain.qna.vo.QnaUpdateData;
import java.time.Instant;

/**
 * Qna - Q&A Aggregate Root.
 *
 * <p>회원이 상품 또는 주문에 대해 작성한 질문과 판매자의 답변을 관리합니다.
 *
 * <p>상품/주문 매핑 정보는 별도 Aggregate(QnaProduct, QnaOrder)로 분리되었습니다.
 *
 * <p>주요 도메인 규칙:
 *
 * <ul>
 *   <li>답변이 등록되면 상태가 ANSWERED로 전환
 *   <li>종료된 Q&A에는 답변 등록/수정 불가
 *   <li>비밀글일 경우 작성자/판매자 외에는 제목과 내용이 마스킹 처리
 *   <li>대댓글(후속 질문)은 parentId로 스레딩. parentId가 null이면 원본(root) 질문
 *   <li>리스트 조회에서는 root 질문만 노출, 상세 조회 시 children 함께 로딩
 * </ul>
 *
 * <p>DOM-AGG-001: static 팩토리 메서드 사용.
 *
 * <p>DOM-AGG-002: ID는 전용 ID VO 사용.
 *
 * <p>DOM-AGG-003: 시간 필드 Instant 타입.
 *
 * <p>DOM-AGG-004: Setter 금지.
 *
 * <p>DOM-AGG-008: 필수 도메인 값은 전용 VO 사용.
 *
 * <p>DOM-CMN-001: 순수 자바 객체 (POJO).
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class Qna {

    private final QnaId id;
    private final LegacyQnaId legacyId;
    private final LegacyQnaId parentId;
    private final LegacyMemberId legacyMemberId;
    private final MemberId memberId;
    private final Long sellerId;
    private final QnaType qnaType;
    private final QnaDetailType detailType;
    private QnaStatus status;
    private QnaTitle title;
    private QnaContent content;
    private boolean secret;
    private QnaAnswer answer;
    private DeletionStatus deletionStatus;
    private final Instant createdAt;
    private Instant updatedAt;

    private Qna(
            QnaId id,
            LegacyQnaId legacyId,
            LegacyQnaId parentId,
            LegacyMemberId legacyMemberId,
            MemberId memberId,
            Long sellerId,
            QnaType qnaType,
            QnaDetailType detailType,
            QnaStatus status,
            QnaTitle title,
            QnaContent content,
            boolean secret,
            QnaAnswer answer,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.legacyId = legacyId;
        this.parentId = parentId;
        this.legacyMemberId = legacyMemberId;
        this.memberId = memberId;
        this.sellerId = sellerId;
        this.qnaType = qnaType;
        this.detailType = detailType;
        this.status = status;
        this.title = title;
        this.content = content;
        this.secret = secret;
        this.answer = answer;
        this.deletionStatus = deletionStatus != null ? deletionStatus : DeletionStatus.active();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ========== Factory Methods ==========

    /**
     * 신규 원본 Q&A 생성.
     *
     * @param legacyMemberId 레거시 회원 ID
     * @param memberId 새 회원 ID
     * @param sellerId 판매자 ID
     * @param qnaType Q&A 유형
     * @param detailType Q&A 상세 유형
     * @param title 제목
     * @param content 내용
     * @param secret 비밀글 여부
     * @param occurredAt 발생 시각
     * @return 신규 Qna
     */
    public static Qna forNew(
            LegacyMemberId legacyMemberId,
            MemberId memberId,
            Long sellerId,
            QnaType qnaType,
            QnaDetailType detailType,
            QnaTitle title,
            QnaContent content,
            boolean secret,
            Instant occurredAt) {
        return new Qna(
                QnaId.forNew(),
                LegacyQnaId.forNew(),
                null,
                legacyMemberId,
                memberId,
                sellerId,
                qnaType,
                detailType,
                QnaStatus.PENDING,
                title,
                content,
                secret,
                null,
                DeletionStatus.active(),
                occurredAt,
                occurredAt);
    }

    /**
     * 후속 질문(대댓글) 생성.
     *
     * <p>원본 Q&A의 컨텍스트(판매자, 유형 등)를 그대로 이어받습니다.
     *
     * @param parentId 부모 Q&A ID
     * @param legacyMemberId 레거시 회원 ID
     * @param memberId 새 회원 ID
     * @param title 제목
     * @param content 내용
     * @param occurredAt 발생 시각
     * @return 후속 Qna
     */
    public Qna createFollowUp(
            LegacyQnaId parentId,
            LegacyMemberId legacyMemberId,
            MemberId memberId,
            QnaTitle title,
            QnaContent content,
            Instant occurredAt) {
        return new Qna(
                QnaId.forNew(),
                LegacyQnaId.forNew(),
                parentId,
                legacyMemberId,
                memberId,
                this.sellerId,
                this.qnaType,
                this.detailType,
                QnaStatus.PENDING,
                title,
                content,
                this.secret,
                null,
                DeletionStatus.active(),
                occurredAt,
                occurredAt);
    }

    /** 영속성 레이어에서 복원. */
    public static Qna reconstitute(
            QnaId id,
            LegacyQnaId legacyId,
            LegacyQnaId parentId,
            LegacyMemberId legacyMemberId,
            MemberId memberId,
            Long sellerId,
            QnaType qnaType,
            QnaDetailType detailType,
            QnaStatus status,
            QnaTitle title,
            QnaContent content,
            boolean secret,
            QnaAnswer answer,
            DeletionStatus deletionStatus,
            Instant createdAt,
            Instant updatedAt) {
        return new Qna(
                id,
                legacyId,
                parentId,
                legacyMemberId,
                memberId,
                sellerId,
                qnaType,
                detailType,
                status,
                title,
                content,
                secret,
                answer,
                deletionStatus,
                createdAt,
                updatedAt);
    }

    // ========== Command Methods ==========

    /**
     * Q&A 질문 수정.
     *
     * @param updateData 수정 데이터 (제목, 내용, 비밀글 여부)
     * @param occurredAt 수정 시각
     */
    public void update(QnaUpdateData updateData, Instant occurredAt) {
        this.title = updateData.title();
        this.content = updateData.content();
        this.secret = updateData.secret();
        this.updatedAt = occurredAt;
    }

    /**
     * 답변 등록.
     *
     * <p>이미 답변이 있으면 예외, 종료된 Q&A에도 예외.
     *
     * @param answerContent 답변 내용
     * @param occurredAt 발생 시각
     * @throws QnaAlreadyAnsweredException 이미 답변이 등록된 경우
     * @throws QnaAlreadyClosedException Q&A가 종료된 경우
     */
    public void registerAnswer(QnaContent answerContent, Instant occurredAt) {
        validateNotClosed();
        if (hasAnswer()) {
            throw new QnaAlreadyAnsweredException(legacyId);
        }
        this.answer = QnaAnswer.create(answerContent, occurredAt);
        this.status = QnaStatus.ANSWERED;
        this.updatedAt = occurredAt;
    }

    /**
     * 답변 수정.
     *
     * @param newContent 새 답변 내용
     * @param occurredAt 수정 시각
     * @throws QnaAlreadyClosedException Q&A가 종료된 경우
     */
    public void editAnswer(QnaContent newContent, Instant occurredAt) {
        validateNotClosed();
        if (!hasAnswer()) {
            throw new IllegalStateException("답변이 존재하지 않아 수정할 수 없습니다");
        }
        this.answer.editContent(newContent, occurredAt);
        this.updatedAt = occurredAt;
    }

    /**
     * 답변 삭제.
     *
     * @param occurredAt 삭제 시각
     */
    public void deleteAnswer(Instant occurredAt) {
        if (!hasAnswer()) {
            throw new IllegalStateException("답변이 존재하지 않아 삭제할 수 없습니다");
        }
        this.answer.delete(occurredAt);
        this.status = QnaStatus.PENDING;
        this.updatedAt = occurredAt;
    }

    /**
     * Q&A 종료.
     *
     * @param occurredAt 종료 시각
     */
    public void close(Instant occurredAt) {
        this.status = QnaStatus.CLOSED;
        this.updatedAt = occurredAt;
    }

    /**
     * Q&A 소프트 삭제.
     *
     * @param occurredAt 삭제 시각
     */
    public void delete(Instant occurredAt) {
        this.deletionStatus = DeletionStatus.deletedAt(occurredAt);
        this.updatedAt = occurredAt;
    }

    // ========== Query Methods ==========

    public boolean isNew() {
        return id.isNew();
    }

    /** 원본(root) 질문인지 확인. parentId가 없으면 root. */
    public boolean isRoot() {
        return parentId == null;
    }

    /** 후속 질문(대댓글)인지 확인. */
    public boolean isFollowUp() {
        return parentId != null;
    }

    public boolean hasAnswer() {
        return answer != null && !answer.isDeleted();
    }

    public boolean isPending() {
        return status.isPending();
    }

    public boolean isAnswered() {
        return status.isAnswered();
    }

    public boolean isClosed() {
        return status.isClosed();
    }

    public boolean isDeleted() {
        return deletionStatus.isDeleted();
    }

    public boolean isSecret() {
        return secret;
    }

    public boolean isProductQna() {
        return qnaType == QnaType.PRODUCT;
    }

    public boolean isOrderQna() {
        return qnaType == QnaType.ORDER;
    }

    /**
     * 비밀글 마스킹이 필요한지 확인.
     *
     * @param viewerMemberId 조회자 회원 ID
     * @return 비밀글이면서 작성자/판매자가 아니면 true
     */
    public boolean needsMasking(LegacyMemberId viewerMemberId) {
        if (!secret) {
            return false;
        }
        return !isOwner(viewerMemberId);
    }

    /**
     * 작성자 본인 확인.
     *
     * @param viewerMemberId 조회자 회원 ID
     * @return 작성자 본인이면 true
     */
    public boolean isOwner(LegacyMemberId viewerMemberId) {
        if (viewerMemberId == null || legacyMemberId == null) {
            return false;
        }
        return legacyMemberId.value() == viewerMemberId.value();
    }

    /**
     * 마스킹 적용된 제목 반환.
     *
     * @param isOwner 작성자 본인 여부
     * @param isSeller 판매자 여부
     * @return 마스킹 적용 결과
     */
    public String displayTitle(boolean isOwner, boolean isSeller) {
        if (!secret) {
            return title.value();
        }
        return title.displayValue(isOwner, isSeller);
    }

    /**
     * 마스킹 적용된 내용 반환.
     *
     * @param isOwner 작성자 본인 여부
     * @param isSeller 판매자 여부
     * @return 마스킹 적용 결과
     */
    public String displayContent(boolean isOwner, boolean isSeller) {
        if (!secret) {
            return content.value();
        }
        return content.displayValue(isOwner, isSeller);
    }

    // ========== Accessor Methods ==========

    public QnaId id() {
        return id;
    }

    public String idValue() {
        return id.value();
    }

    public LegacyQnaId legacyId() {
        return legacyId;
    }

    public Long legacyIdValue() {
        return legacyId != null ? legacyId.value() : null;
    }

    public LegacyQnaId parentId() {
        return parentId;
    }

    public Long parentIdValue() {
        return parentId != null ? parentId.value() : null;
    }

    public LegacyMemberId legacyMemberId() {
        return legacyMemberId;
    }

    public Long legacyMemberIdValue() {
        return legacyMemberId != null ? legacyMemberId.value() : null;
    }

    public MemberId memberId() {
        return memberId;
    }

    public String memberIdValue() {
        return memberId != null ? memberId.value() : null;
    }

    public Long sellerId() {
        return sellerId;
    }

    public QnaType qnaType() {
        return qnaType;
    }

    public QnaDetailType detailType() {
        return detailType;
    }

    public QnaStatus status() {
        return status;
    }

    public QnaTitle title() {
        return title;
    }

    public String titleValue() {
        return title.value();
    }

    public QnaContent content() {
        return content;
    }

    public String contentValue() {
        return content.value();
    }

    public QnaAnswer answer() {
        return answer;
    }

    public DeletionStatus deletionStatus() {
        return deletionStatus;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    // ========== Private Methods ==========

    private void validateNotClosed() {
        if (isClosed()) {
            throw new QnaAlreadyClosedException(legacyId);
        }
    }
}
