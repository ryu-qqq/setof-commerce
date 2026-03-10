package com.ryuqq.setof.application.qna;

import com.ryuqq.setof.application.qna.dto.query.MyQnaSearchParams;
import com.ryuqq.setof.application.qna.dto.query.ProductQnaSearchParams;
import com.ryuqq.setof.application.qna.dto.response.MyQnaDetailResult;
import com.ryuqq.setof.application.qna.dto.response.MyQnaResult;
import com.ryuqq.setof.application.qna.dto.response.MyQnaSliceResult;
import com.ryuqq.setof.application.qna.dto.response.QnaAnswerDetailResult;
import com.ryuqq.setof.application.qna.dto.response.QnaAnswerResult;
import com.ryuqq.setof.application.qna.dto.response.QnaDetailResult;
import com.ryuqq.setof.application.qna.dto.response.QnaPageResult;
import com.ryuqq.setof.application.qna.dto.response.QnaWithAnswersResult;
import com.ryuqq.setof.domain.common.vo.SliceMeta;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Qna Application Query 테스트 Fixtures.
 *
 * <p>Q&A 조회 관련 Query 파라미터 및 Result 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class QnaQueryFixtures {

    private QnaQueryFixtures() {}

    // ===== 공통 상수 =====

    public static final Long USER_ID = 100L;
    public static final Long PRODUCT_GROUP_ID = 500L;
    public static final Long QNA_ID = 1001L;
    public static final Long QNA_ANSWER_ID = 2001L;

    private static final LocalDateTime NOW = LocalDateTime.of(2024, 1, 15, 10, 0, 0);
    private static final LocalDateTime YESTERDAY = LocalDateTime.of(2024, 1, 14, 10, 0, 0);

    // ===== ProductQnaSearchParams =====

    public static ProductQnaSearchParams productQnaSearchParams() {
        return ProductQnaSearchParams.of(PRODUCT_GROUP_ID, USER_ID, 0, 10);
    }

    public static ProductQnaSearchParams productQnaSearchParams(int page, int size) {
        return ProductQnaSearchParams.of(PRODUCT_GROUP_ID, USER_ID, page, size);
    }

    public static ProductQnaSearchParams productQnaSearchParamsAnonymous() {
        return ProductQnaSearchParams.of(PRODUCT_GROUP_ID, null, 0, 10);
    }

    public static ProductQnaSearchParams productQnaSearchParams(Long productGroupId, Long viewerUserId) {
        return ProductQnaSearchParams.of(productGroupId, viewerUserId, 0, 10);
    }

    // ===== MyQnaSearchParams =====

    public static MyQnaSearchParams myProductQnaSearchParams() {
        return MyQnaSearchParams.of(USER_ID, "PRODUCT", null, null, null, 10);
    }

    public static MyQnaSearchParams myOrderQnaSearchParams() {
        return MyQnaSearchParams.of(USER_ID, "ORDER", null, null, null, 10);
    }

    public static MyQnaSearchParams myQnaSearchParams(String qnaType) {
        return MyQnaSearchParams.of(USER_ID, qnaType, null, null, null, 10);
    }

    public static MyQnaSearchParams myQnaSearchParamsWithCursor(Long lastQnaId) {
        return MyQnaSearchParams.of(USER_ID, "PRODUCT", lastQnaId, null, null, 10);
    }

    // ===== QnaAnswerResult =====

    public static QnaAnswerResult qnaAnswerResult() {
        return QnaAnswerResult.of(
                QNA_ANSWER_ID,
                null,
                "SELLER",
                "배송 답변",
                "안녕하세요. 현재 배송 준비 중이며 내일 출고 예정입니다.",
                YESTERDAY,
                YESTERDAY);
    }

    // ===== QnaAnswerDetailResult =====

    public static QnaAnswerDetailResult qnaAnswerDetailResult() {
        return QnaAnswerDetailResult.of(
                QNA_ANSWER_ID,
                null,
                "SELLER",
                "배송 답변",
                "안녕하세요. 현재 배송 준비 중이며 내일 출고 예정입니다.",
                YESTERDAY,
                YESTERDAY);
    }

    // ===== QnaWithAnswersResult =====

    public static QnaWithAnswersResult qnaWithAnswersResult() {
        return QnaWithAnswersResult.of(
                QNA_ID,
                "배송은 언제 오나요?",
                "주문한 지 3일이 지났는데 배송 현황을 알고 싶습니다.",
                "N",
                "PENDING",
                "PRODUCT",
                "SHIPMENT",
                "BUYER",
                USER_ID,
                "홍길동",
                YESTERDAY,
                YESTERDAY,
                Set.of());
    }

    public static QnaWithAnswersResult qnaWithAnswersResult(long qnaId, long userId) {
        return QnaWithAnswersResult.of(
                qnaId,
                "배송 문의",
                "배송 현황 문의드립니다.",
                "N",
                "PENDING",
                "PRODUCT",
                "SHIPMENT",
                "BUYER",
                userId,
                "테스트유저",
                YESTERDAY,
                YESTERDAY,
                Set.of());
    }

    public static QnaWithAnswersResult qnaWithAnswersResultPrivate(long qnaId, long userId) {
        return QnaWithAnswersResult.of(
                qnaId,
                "비밀 질문",
                "비밀 내용입니다.",
                "Y",
                "PENDING",
                "PRODUCT",
                "SHIPMENT",
                "BUYER",
                userId,
                "홍길동",
                YESTERDAY,
                YESTERDAY,
                Set.of(qnaAnswerResult()));
    }

    public static QnaWithAnswersResult qnaWithAnswersResultWithAnswer() {
        return QnaWithAnswersResult.of(
                QNA_ID,
                "배송 문의",
                "배송 현황 문의드립니다.",
                "N",
                "ANSWERED",
                "PRODUCT",
                "SHIPMENT",
                "BUYER",
                USER_ID,
                "홍길동",
                YESTERDAY,
                NOW,
                Set.of(qnaAnswerResult()));
    }

    public static List<QnaWithAnswersResult> qnaWithAnswersResultList() {
        return List.of(
                qnaWithAnswersResult(QNA_ID, USER_ID),
                qnaWithAnswersResult(QNA_ID + 1, USER_ID + 1));
    }

    public static List<QnaWithAnswersResult> qnaWithAnswersResultList(int count) {
        return java.util.stream.LongStream.rangeClosed(1, count)
                .mapToObj(i -> qnaWithAnswersResult(QNA_ID + i - 1, USER_ID))
                .toList();
    }

    // ===== QnaDetailResult =====

    public static QnaDetailResult qnaDetailResult() {
        return QnaDetailResult.of(
                QNA_ID,
                "배송은 언제 오나요?",
                "주문한 지 3일이 지났는데 배송 현황을 알고 싶습니다.",
                "N",
                "PENDING",
                "PRODUCT",
                "SHIPMENT",
                "BUYER",
                USER_ID,
                "홍길**",
                YESTERDAY,
                YESTERDAY,
                Set.of());
    }

    // ===== QnaPageResult =====

    public static QnaPageResult qnaPageResult() {
        return QnaPageResult.of(List.of(qnaDetailResult()), 0, 10, 1L);
    }

    public static QnaPageResult emptyQnaPageResult() {
        return QnaPageResult.empty(0, 10);
    }

    // ===== MyQnaResult =====

    public static MyQnaResult myProductQnaResult() {
        return MyQnaResult.ofProduct(
                QNA_ID,
                "배송 문의",
                "배송 현황을 알고 싶습니다.",
                "N",
                "PENDING",
                "PRODUCT",
                "SHIPMENT",
                "BUYER",
                USER_ID,
                "홍길동",
                PRODUCT_GROUP_ID,
                "테스트 상품그룹",
                "https://example.com/product-image.jpg",
                1L,
                "테스트브랜드",
                YESTERDAY,
                YESTERDAY,
                Set.of());
    }

    public static MyQnaResult myOrderQnaResult() {
        return MyQnaResult.ofOrder(
                QNA_ID + 100,
                "주문 결제 문의",
                "결제가 정상 처리되었는지 확인하고 싶습니다.",
                "N",
                "PENDING",
                "ORDER",
                "ORDER_PAYMENT",
                "BUYER",
                USER_ID,
                "홍길동",
                PRODUCT_GROUP_ID,
                "테스트 스냅샷 상품",
                "https://example.com/order-image.jpg",
                1L,
                "테스트브랜드",
                700L,
                800L,
                50000L,
                2,
                "COLOR:RED / SIZE:XL",
                YESTERDAY,
                YESTERDAY,
                Set.of());
    }

    public static List<MyQnaResult> myProductQnaResultList() {
        return List.of(myProductQnaResult());
    }

    public static List<MyQnaResult> myProductQnaResultList(int count) {
        return java.util.stream.LongStream.rangeClosed(1, count)
                .mapToObj(i -> myProductQnaResult())
                .toList();
    }

    // ===== MyQnaDetailResult =====

    public static MyQnaDetailResult myQnaDetailResult() {
        return MyQnaDetailResult.of(
                QNA_ID,
                "배송 문의",
                "배송 현황을 알고 싶습니다.",
                "N",
                "PENDING",
                "PRODUCT",
                "SHIPMENT",
                "BUYER",
                USER_ID,
                "홍길동",
                PRODUCT_GROUP_ID,
                "테스트 상품그룹",
                "https://example.com/product-image.jpg",
                1L,
                "테스트브랜드",
                null,
                null,
                null,
                null,
                null,
                YESTERDAY,
                YESTERDAY,
                Set.of());
    }

    // ===== MyQnaSliceResult =====

    public static MyQnaSliceResult myQnaSliceResult() {
        List<MyQnaDetailResult> items = List.of(myQnaDetailResult());
        SliceMeta sliceMeta = SliceMeta.withCursor((Long) null, 10, false, items.size());
        return MyQnaSliceResult.of(items, sliceMeta);
    }

    public static MyQnaSliceResult myQnaSliceResultWithNext() {
        List<MyQnaDetailResult> items = List.of(myQnaDetailResult());
        SliceMeta sliceMeta = SliceMeta.withCursor(QNA_ID, 10, true, items.size());
        return MyQnaSliceResult.of(items, sliceMeta);
    }

    public static MyQnaSliceResult emptyMyQnaSliceResult() {
        return MyQnaSliceResult.empty();
    }
}
