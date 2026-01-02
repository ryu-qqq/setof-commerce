package com.ryuqq.setof.domain.qna.aggregate;

import com.ryuqq.setof.domain.qna.exception.ImageLimitExceededException;
import com.ryuqq.setof.domain.qna.exception.ProductQnaCannotHaveImagesException;
import com.ryuqq.setof.domain.qna.exception.QnaAlreadyClosedException;
import com.ryuqq.setof.domain.qna.vo.QnaContent;
import com.ryuqq.setof.domain.qna.vo.QnaDetailType;
import com.ryuqq.setof.domain.qna.vo.QnaId;
import com.ryuqq.setof.domain.qna.vo.QnaImage;
import com.ryuqq.setof.domain.qna.vo.QnaStatus;
import com.ryuqq.setof.domain.qna.vo.QnaType;
import com.ryuqq.setof.domain.qna.vo.WriterId;
import com.ryuqq.setof.domain.qna.vo.WriterType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Qna {

    private static final int MAX_IMAGE_COUNT = 3;

    private final QnaId id;
    private final QnaType type;
    private final QnaDetailType detailType;
    private final Long targetId;
    private final WriterId writerId;
    private final WriterType writerType;
    private final String writerName;
    private final QnaContent content;
    private final boolean isSecret;
    private final List<QnaImage> images;
    private QnaStatus status;
    private int replyCount;
    private final LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    private Qna(
            QnaId id,
            QnaType type,
            QnaDetailType detailType,
            Long targetId,
            WriterId writerId,
            WriterType writerType,
            String writerName,
            QnaContent content,
            boolean isSecret,
            List<QnaImage> images,
            QnaStatus status,
            int replyCount,
            LocalDateTime createdAt,
            LocalDateTime deletedAt) {
        this.id = id;
        this.type = type;
        this.detailType = detailType;
        this.targetId = targetId;
        this.writerId = writerId;
        this.writerType = writerType;
        this.writerName = writerName;
        this.content = content;
        this.isSecret = isSecret;
        this.images = images != null ? new ArrayList<>(images) : new ArrayList<>();
        this.status = status;
        this.replyCount = replyCount;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
    }

    public static Qna forNewProductQna(
            Long productGroupId,
            WriterId writerId,
            WriterType writerType,
            String writerName,
            QnaContent content,
            QnaDetailType detailType,
            boolean isSecret) {
        return new Qna(
                null,
                QnaType.PRODUCT,
                detailType,
                productGroupId,
                writerId,
                writerType,
                writerName,
                content,
                isSecret,
                Collections.emptyList(),
                QnaStatus.OPEN,
                0,
                LocalDateTime.now(),
                null);
    }

    public static Qna forNewOrderQna(
            Long orderId,
            WriterId writerId,
            WriterType writerType,
            String writerName,
            QnaContent content,
            QnaDetailType detailType,
            boolean isSecret,
            List<QnaImage> images) {
        validateOrderQnaImages(images);
        return new Qna(
                null,
                QnaType.ORDER,
                detailType,
                orderId,
                writerId,
                writerType,
                writerName,
                content,
                isSecret,
                images,
                QnaStatus.OPEN,
                0,
                LocalDateTime.now(),
                null);
    }

    public static Qna reconstitute(
            QnaId id,
            QnaType type,
            QnaDetailType detailType,
            Long targetId,
            WriterId writerId,
            WriterType writerType,
            String writerName,
            QnaContent content,
            boolean isSecret,
            List<QnaImage> images,
            QnaStatus status,
            int replyCount,
            LocalDateTime createdAt,
            LocalDateTime deletedAt) {
        return new Qna(
                id,
                type,
                detailType,
                targetId,
                writerId,
                writerType,
                writerName,
                content,
                isSecret,
                images,
                status,
                replyCount,
                createdAt,
                deletedAt);
    }

    private static void validateOrderQnaImages(List<QnaImage> images) {
        if (images != null && images.size() > MAX_IMAGE_COUNT) {
            throw new ImageLimitExceededException(images.size());
        }
    }

    public void close() {
        if (status.isClosed()) {
            throw new QnaAlreadyClosedException(id.getValue());
        }
        this.status = QnaStatus.CLOSED;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void incrementReplyCount() {
        this.replyCount++;
    }

    /**
     * 문의 내용 수정
     *
     * <p>OPEN 상태의 문의만 수정 가능합니다.
     *
     * @param newContent 새로운 문의 내용
     * @return 수정된 Qna 객체 (불변성 유지)
     */
    public Qna updateContent(QnaContent newContent) {
        if (status.isClosed()) {
            throw new QnaAlreadyClosedException(id.getValue());
        }
        return new Qna(
                this.id,
                this.type,
                this.detailType,
                this.targetId,
                this.writerId,
                this.writerType,
                this.writerName,
                newContent,
                this.isSecret,
                this.images,
                this.status,
                this.replyCount,
                this.createdAt,
                this.deletedAt);
    }

    public void addImages(List<QnaImage> newImages) {
        if (type.isProductQna()) {
            throw new ProductQnaCannotHaveImagesException();
        }
        int totalCount = this.images.size() + newImages.size();
        if (totalCount > MAX_IMAGE_COUNT) {
            throw new ImageLimitExceededException(totalCount);
        }
        this.images.addAll(newImages);
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public boolean isOpen() {
        return status.isOpen();
    }

    public boolean canBeReplied() {
        return isOpen() && !isDeleted();
    }

    public QnaId getId() {
        return id;
    }

    public QnaType getType() {
        return type;
    }

    public QnaDetailType getDetailType() {
        return detailType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public WriterId getWriterId() {
        return writerId;
    }

    public WriterType getWriterType() {
        return writerType;
    }

    public String getWriterName() {
        return writerName;
    }

    public QnaContent getContent() {
        return content;
    }

    public boolean isSecret() {
        return isSecret;
    }

    public List<QnaImage> getImages() {
        return Collections.unmodifiableList(images);
    }

    public QnaStatus getStatus() {
        return status;
    }

    public int getReplyCount() {
        return replyCount;
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
        Qna qna = (Qna) o;
        return Objects.equals(id, qna.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
