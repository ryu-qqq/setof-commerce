package com.ryuqq.setof.application.qna.factory;

import com.ryuqq.setof.application.common.dto.command.UpdateContext;
import com.ryuqq.setof.application.qna.dto.bundle.OrderQnaBundle;
import com.ryuqq.setof.application.qna.dto.bundle.ProductQnaBundle;
import com.ryuqq.setof.application.qna.dto.command.ModifyQnaCommand;
import com.ryuqq.setof.application.qna.dto.command.RegisterQnaCommand;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.order.id.LegacyOrderId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.aggregate.QnaImage;
import com.ryuqq.setof.domain.qna.aggregate.QnaImages;
import com.ryuqq.setof.domain.qna.aggregate.QnaOrder;
import com.ryuqq.setof.domain.qna.aggregate.QnaProduct;
import com.ryuqq.setof.domain.qna.vo.QnaContent;
import com.ryuqq.setof.domain.qna.vo.QnaDetailType;
import com.ryuqq.setof.domain.qna.vo.QnaImageInfo;
import com.ryuqq.setof.domain.qna.vo.QnaTitle;
import com.ryuqq.setof.domain.qna.vo.QnaType;
import com.ryuqq.setof.domain.qna.vo.QnaUpdateData;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;

/**
 * QnaCommandFactory - Q&A 도메인 객체 생성 Factory.
 *
 * <p>Command DTO를 Bundle(Qna + 관련 Aggregate)로 변환합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class QnaCommandFactory {

    /**
     * 상품 Q&A 번들 생성.
     *
     * @param command Q&A 등록 커맨드
     * @return ProductQnaBundle (Qna + QnaProduct)
     */
    public ProductQnaBundle createProductBundle(RegisterQnaCommand command) {
        Instant now = Instant.now();
        Qna qna = createQna(command, now);
        QnaProduct qnaProduct =
                QnaProduct.forNew(ProductGroupId.of(command.productGroupId()), now);
        return new ProductQnaBundle(qna, qnaProduct);
    }

    /**
     * 주문 Q&A 번들 생성.
     *
     * @param command Q&A 등록 커맨드
     * @return OrderQnaBundle (Qna + QnaOrder + QnaImages)
     */
    public OrderQnaBundle createOrderBundle(RegisterQnaCommand command) {
        Instant now = Instant.now();
        Qna qna = createQna(command, now);
        QnaOrder qnaOrder = QnaOrder.forNew(LegacyOrderId.of(command.legacyOrderId()), now);
        QnaImages images = createImages(command.imageUrls());
        return new OrderQnaBundle(qna, qnaOrder, images);
    }

    /**
     * Q&A 수정 UpdateContext 생성.
     *
     * @param command 수정 커맨드
     * @return UpdateContext (LegacyQnaId, QnaUpdateData, changedAt)
     */
    public UpdateContext<Long, QnaUpdateData> createUpdateContext(ModifyQnaCommand command) {
        Instant now = Instant.now();
        QnaUpdateData updateData =
                QnaUpdateData.of(
                        QnaTitle.of(command.title()),
                        QnaContent.of(command.content()),
                        command.secret());
        return new UpdateContext<>(command.qnaId(), updateData, now);
    }

    /**
     * 이미지 URL 목록에서 QnaImages 일급 컬렉션 생성.
     *
     * @param imageUrls 이미지 URL 목록
     * @return QnaImages
     */
    public QnaImages createImages(List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return QnaImages.empty();
        }
        Instant now = Instant.now();
        AtomicInteger order = new AtomicInteger(0);
        List<QnaImage> images =
                imageUrls.stream()
                        .map(url -> QnaImage.create(QnaImageInfo.of(url), order.getAndIncrement(), now))
                        .toList();
        return QnaImages.of(images);
    }

    private Qna createQna(RegisterQnaCommand command, Instant now) {
        QnaType qnaType = QnaType.valueOf(command.qnaType());
        QnaDetailType detailType = QnaDetailType.valueOf(command.qnaDetailType());

        return Qna.forNew(
                LegacyMemberId.of(command.userId()),
                null,
                command.sellerId(),
                qnaType,
                detailType,
                QnaTitle.of(command.title()),
                QnaContent.of(command.content()),
                command.secret(),
                now);
    }
}
