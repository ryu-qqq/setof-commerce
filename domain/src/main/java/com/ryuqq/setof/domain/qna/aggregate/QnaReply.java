package com.ryuqq.setof.domain.qna.aggregate;

import com.ryuqq.setof.domain.qna.vo.QnaReplyId;
import com.ryuqq.setof.domain.qna.vo.ReplyContent;
import com.ryuqq.setof.domain.qna.vo.ReplyPath;
import com.ryuqq.setof.domain.qna.vo.ReplyWriterType;
import com.ryuqq.setof.domain.qna.vo.WriterId;
import java.time.LocalDateTime;
import java.util.Objects;

public class QnaReply {

    private final QnaReplyId id;
    private final long qnaId;
    private final Long parentReplyId;
    private final WriterId writerId;
    private final ReplyWriterType writerType;
    private final String writerName;
    private final ReplyContent content;
    private final ReplyPath path;
    private final LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    private QnaReply(
            QnaReplyId id,
            long qnaId,
            Long parentReplyId,
            WriterId writerId,
            ReplyWriterType writerType,
            String writerName,
            ReplyContent content,
            ReplyPath path,
            LocalDateTime createdAt,
            LocalDateTime deletedAt) {
        this.id = id;
        this.qnaId = qnaId;
        this.parentReplyId = parentReplyId;
        this.writerId = writerId;
        this.writerType = writerType;
        this.writerName = writerName;
        this.content = content;
        this.path = path;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
    }

    public static QnaReply forNewRootReply(
            long qnaId,
            WriterId writerId,
            ReplyWriterType writerType,
            String writerName,
            ReplyContent content) {
        return new QnaReply(
                null, qnaId, null, writerId, writerType, writerName, content, null, LocalDateTime.now(), null);
    }

    public static QnaReply forNewChildReply(
            long qnaId,
            Long parentReplyId,
            ReplyPath parentPath,
            WriterId writerId,
            ReplyWriterType writerType,
            String writerName,
            ReplyContent content) {
        return new QnaReply(
                null,
                qnaId,
                parentReplyId,
                writerId,
                writerType,
                writerName,
                content,
                parentPath,
                LocalDateTime.now(),
                null);
    }

    public static QnaReply reconstitute(
            QnaReplyId id,
            long qnaId,
            Long parentReplyId,
            WriterId writerId,
            ReplyWriterType writerType,
            String writerName,
            ReplyContent content,
            ReplyPath path,
            LocalDateTime createdAt,
            LocalDateTime deletedAt) {
        return new QnaReply(
                id, qnaId, parentReplyId, writerId, writerType, writerName, content, path, createdAt, deletedAt);
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    /**
     * 답변 내용 수정
     *
     * @param newContent 새 답변 내용
     * @return 수정된 QnaReply (새 인스턴스)
     */
    public QnaReply updateContent(ReplyContent newContent) {
        return new QnaReply(
                this.id,
                this.qnaId,
                this.parentReplyId,
                this.writerId,
                this.writerType,
                this.writerName,
                newContent,
                this.path,
                this.createdAt,
                this.deletedAt);
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public boolean isRootReply() {
        return parentReplyId == null;
    }

    public int getDepth() {
        return path.getDepth();
    }

    public QnaReplyId getId() {
        return id;
    }

    public long getQnaId() {
        return qnaId;
    }

    public Long getParentReplyId() {
        return parentReplyId;
    }

    public WriterId getWriterId() {
        return writerId;
    }

    public ReplyWriterType getWriterType() {
        return writerType;
    }

    public String getWriterName() {
        return writerName;
    }

    public ReplyContent getContent() {
        return content;
    }

    public ReplyPath getPath() {
        return path;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QnaReply qnaReply = (QnaReply) o;
        return Objects.equals(id, qnaReply.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
