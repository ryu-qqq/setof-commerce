package com.ryuqq.setof.domain.qna;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.order.id.LegacyOrderId;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.qna.aggregate.Qna;
import com.ryuqq.setof.domain.qna.aggregate.QnaAnswer;
import com.ryuqq.setof.domain.qna.aggregate.QnaImage;
import com.ryuqq.setof.domain.qna.aggregate.QnaImages;
import com.ryuqq.setof.domain.qna.aggregate.QnaOrder;
import com.ryuqq.setof.domain.qna.aggregate.QnaProduct;
import com.ryuqq.setof.domain.qna.id.LegacyQnaAnswerId;
import com.ryuqq.setof.domain.qna.id.LegacyQnaId;
import com.ryuqq.setof.domain.qna.id.QnaAnswerId;
import com.ryuqq.setof.domain.qna.id.QnaId;
import com.ryuqq.setof.domain.qna.id.QnaImageId;
import com.ryuqq.setof.domain.qna.vo.QnaContent;
import com.ryuqq.setof.domain.qna.vo.QnaDetailType;
import com.ryuqq.setof.domain.qna.vo.QnaImageInfo;
import com.ryuqq.setof.domain.qna.vo.QnaStatus;
import com.ryuqq.setof.domain.qna.vo.QnaTitle;
import com.ryuqq.setof.domain.qna.vo.QnaType;
import com.ryuqq.setof.domain.qna.vo.QnaUpdateData;
import java.time.Instant;
import java.util.List;

/**
 * Qna 도메인 테스트 Fixtures.
 *
 * <p>테스트에서 Qna 관련 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class QnaFixtures {

    private QnaFixtures() {}

    // ===== ID Fixtures =====

    public static QnaId defaultQnaId() {
        return QnaId.of("qna-uuid-0001");
    }

    public static LegacyQnaId defaultLegacyQnaId() {
        return LegacyQnaId.of(1001L);
    }

    public static LegacyQnaId legacyQnaId(Long value) {
        return LegacyQnaId.of(value);
    }

    public static QnaAnswerId defaultQnaAnswerId() {
        return QnaAnswerId.of("qna-answer-uuid-0001");
    }

    public static LegacyQnaAnswerId defaultLegacyQnaAnswerId() {
        return LegacyQnaAnswerId.of(2001L);
    }

    public static QnaImageId defaultQnaImageId() {
        return QnaImageId.of(3001L);
    }

    // ===== VO Fixtures =====

    public static LegacyMemberId defaultLegacyMemberId() {
        return LegacyMemberId.of(100L);
    }

    public static LegacyMemberId legacyMemberId(long value) {
        return LegacyMemberId.of(value);
    }

    public static MemberId defaultMemberId() {
        return MemberId.of(1L);
    }

    public static QnaTitle defaultQnaTitle() {
        return QnaTitle.of("배송은 언제 오나요?");
    }

    public static QnaTitle qnaTitle(String value) {
        return QnaTitle.of(value);
    }

    public static QnaContent defaultQnaContent() {
        return QnaContent.of("주문한 지 3일이 지났는데 배송 현황을 알고 싶습니다.");
    }

    public static QnaContent qnaContent(String value) {
        return QnaContent.of(value);
    }

    public static QnaContent defaultAnswerContent() {
        return QnaContent.of("안녕하세요. 현재 배송 준비 중이며 내일 출고 예정입니다.");
    }

    public static QnaImageInfo defaultQnaImageInfo() {
        return QnaImageInfo.of("https://example.com/qna-image.jpg");
    }

    public static QnaImageInfo qnaImageInfo(String url) {
        return QnaImageInfo.of(url);
    }

    public static QnaUpdateData defaultQnaUpdateData() {
        return QnaUpdateData.of(
                QnaTitle.of("수정된 질문 제목"),
                QnaContent.of("수정된 질문 내용입니다."),
                false);
    }

    public static QnaUpdateData secretQnaUpdateData() {
        return QnaUpdateData.of(
                QnaTitle.of("비밀 질문 제목"),
                QnaContent.of("비밀 질문 내용입니다."),
                true);
    }

    // ===== QnaImage Fixtures =====

    public static QnaImage newQnaImage() {
        return QnaImage.create(defaultQnaImageInfo(), 1, CommonVoFixtures.now());
    }

    public static QnaImage newQnaImage(int displayOrder) {
        return QnaImage.create(defaultQnaImageInfo(), displayOrder, CommonVoFixtures.now());
    }

    public static QnaImage activeQnaImage() {
        return QnaImage.reconstitute(
                defaultQnaImageId(),
                defaultQnaImageInfo(),
                1,
                DeletionStatus.active(),
                CommonVoFixtures.yesterday());
    }

    public static QnaImage activeQnaImage(Long id, String url, int displayOrder) {
        return QnaImage.reconstitute(
                QnaImageId.of(id),
                QnaImageInfo.of(url),
                displayOrder,
                DeletionStatus.active(),
                CommonVoFixtures.yesterday());
    }

    // ===== QnaImages Fixtures =====

    public static QnaImages emptyQnaImages() {
        return QnaImages.empty();
    }

    public static QnaImages defaultQnaImages() {
        return QnaImages.of(List.of(newQnaImage()));
    }

    public static QnaImages qnaImagesWithCount(int count) {
        List<QnaImage> images = new java.util.ArrayList<>();
        for (int i = 1; i <= count; i++) {
            images.add(QnaImage.create(QnaImageInfo.of("https://example.com/image-" + i + ".jpg"), i, CommonVoFixtures.now()));
        }
        return QnaImages.of(images);
    }

    // ===== QnaAnswer Fixtures =====

    public static QnaAnswer newQnaAnswer() {
        return QnaAnswer.create(defaultAnswerContent(), CommonVoFixtures.now());
    }

    public static QnaAnswer activeQnaAnswer() {
        return QnaAnswer.reconstitute(
                defaultQnaAnswerId(),
                defaultLegacyQnaAnswerId(),
                defaultAnswerContent(),
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static QnaAnswer deletedQnaAnswer() {
        Instant deletedAt = CommonVoFixtures.yesterday();
        return QnaAnswer.reconstitute(
                defaultQnaAnswerId(),
                defaultLegacyQnaAnswerId(),
                defaultAnswerContent(),
                DeletionStatus.deletedAt(deletedAt),
                CommonVoFixtures.yesterday(),
                deletedAt);
    }

    // ===== Qna Aggregate Fixtures =====

    public static Qna newProductQna() {
        return Qna.forNew(
                defaultLegacyMemberId(),
                defaultMemberId(),
                1L,
                QnaType.PRODUCT,
                QnaDetailType.SHIPMENT,
                defaultQnaTitle(),
                defaultQnaContent(),
                false,
                CommonVoFixtures.now());
    }

    public static Qna newOrderQna() {
        return Qna.forNew(
                defaultLegacyMemberId(),
                defaultMemberId(),
                1L,
                QnaType.ORDER,
                QnaDetailType.ORDER_PAYMENT,
                defaultQnaTitle(),
                defaultQnaContent(),
                false,
                CommonVoFixtures.now());
    }

    public static Qna newSecretQna() {
        return Qna.forNew(
                defaultLegacyMemberId(),
                defaultMemberId(),
                1L,
                QnaType.PRODUCT,
                QnaDetailType.SIZE,
                defaultQnaTitle(),
                defaultQnaContent(),
                true,
                CommonVoFixtures.now());
    }

    public static Qna pendingQna() {
        return Qna.reconstitute(
                defaultQnaId(),
                defaultLegacyQnaId(),
                null,
                defaultLegacyMemberId(),
                defaultMemberId(),
                1L,
                QnaType.PRODUCT,
                QnaDetailType.SHIPMENT,
                QnaStatus.PENDING,
                defaultQnaTitle(),
                defaultQnaContent(),
                false,
                null,
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static Qna answeredQna() {
        return Qna.reconstitute(
                defaultQnaId(),
                defaultLegacyQnaId(),
                null,
                defaultLegacyMemberId(),
                defaultMemberId(),
                1L,
                QnaType.PRODUCT,
                QnaDetailType.SHIPMENT,
                QnaStatus.ANSWERED,
                defaultQnaTitle(),
                defaultQnaContent(),
                false,
                activeQnaAnswer(),
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static Qna closedQna() {
        return Qna.reconstitute(
                defaultQnaId(),
                defaultLegacyQnaId(),
                null,
                defaultLegacyMemberId(),
                defaultMemberId(),
                1L,
                QnaType.PRODUCT,
                QnaDetailType.SHIPMENT,
                QnaStatus.CLOSED,
                defaultQnaTitle(),
                defaultQnaContent(),
                false,
                null,
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static Qna deletedQna() {
        Instant deletedAt = CommonVoFixtures.yesterday();
        return Qna.reconstitute(
                defaultQnaId(),
                defaultLegacyQnaId(),
                null,
                defaultLegacyMemberId(),
                defaultMemberId(),
                1L,
                QnaType.PRODUCT,
                QnaDetailType.SHIPMENT,
                QnaStatus.PENDING,
                defaultQnaTitle(),
                defaultQnaContent(),
                false,
                null,
                DeletionStatus.deletedAt(deletedAt),
                CommonVoFixtures.yesterday(),
                deletedAt);
    }

    public static Qna secretQna() {
        return Qna.reconstitute(
                defaultQnaId(),
                defaultLegacyQnaId(),
                null,
                defaultLegacyMemberId(),
                defaultMemberId(),
                1L,
                QnaType.PRODUCT,
                QnaDetailType.SIZE,
                QnaStatus.PENDING,
                defaultQnaTitle(),
                defaultQnaContent(),
                true,
                null,
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    public static Qna followUpQna() {
        return Qna.reconstitute(
                QnaId.of("qna-uuid-follow-0001"),
                LegacyQnaId.of(1002L),
                defaultLegacyQnaId(),
                defaultLegacyMemberId(),
                defaultMemberId(),
                1L,
                QnaType.PRODUCT,
                QnaDetailType.SHIPMENT,
                QnaStatus.PENDING,
                QnaTitle.of("추가 질문입니다"),
                QnaContent.of("답변 감사합니다. 추가로 여쭤보고 싶습니다."),
                false,
                null,
                DeletionStatus.active(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    // ===== QnaProduct Fixtures =====

    public static QnaProduct newQnaProduct() {
        return QnaProduct.forNew(
                ProductGroupId.of(500L),
                CommonVoFixtures.now());
    }

    public static QnaProduct activeQnaProduct() {
        return QnaProduct.reconstitute(
                defaultLegacyQnaId(),
                ProductGroupId.of(500L),
                CommonVoFixtures.yesterday());
    }

    // ===== QnaOrder Fixtures =====

    public static QnaOrder newQnaOrder() {
        return QnaOrder.forNew(
                LegacyOrderId.of(700L),
                CommonVoFixtures.now());
    }

    public static QnaOrder activeQnaOrder() {
        return QnaOrder.reconstitute(
                defaultLegacyQnaId(),
                LegacyOrderId.of(700L),
                CommonVoFixtures.yesterday());
    }
}
