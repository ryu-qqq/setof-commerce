package com.ryuqq.setof.application.qna;

import com.ryuqq.setof.application.qna.dto.bundle.OrderQnaBundle;
import com.ryuqq.setof.application.qna.dto.bundle.ProductQnaBundle;
import com.ryuqq.setof.application.qna.dto.command.ModifyQnaAnswerCommand;
import com.ryuqq.setof.application.qna.dto.command.ModifyQnaCommand;
import com.ryuqq.setof.application.qna.dto.command.RegisterQnaAnswerCommand;
import com.ryuqq.setof.application.qna.dto.command.RegisterQnaCommand;
import com.ryuqq.setof.domain.qna.QnaFixtures;
import java.util.List;

/**
 * Qna Application Command 테스트 Fixtures.
 *
 * <p>Q&A 등록/수정 Command 및 Bundle 객체들을 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class QnaCommandFixtures {

    private QnaCommandFixtures() {}

    // ===== 공통 상수 =====

    public static final Long USER_ID = 100L;
    public static final Long SELLER_ID = 1L;
    public static final Long PRODUCT_GROUP_ID = 500L;
    public static final Long LEGACY_ORDER_ID = 700L;
    public static final Long QNA_ID = 1001L;
    public static final Long QNA_ANSWER_ID = 2001L;

    // ===== RegisterQnaCommand (PRODUCT) =====

    public static RegisterQnaCommand registerProductQnaCommand() {
        return RegisterQnaCommand.of(
                USER_ID,
                SELLER_ID,
                "PRODUCT",
                "SHIPMENT",
                PRODUCT_GROUP_ID,
                null,
                "배송은 언제 오나요?",
                "주문한 지 3일이 지났는데 배송 현황을 알고 싶습니다.",
                false,
                List.of());
    }

    public static RegisterQnaCommand registerProductQnaCommandSecret() {
        return RegisterQnaCommand.of(
                USER_ID,
                SELLER_ID,
                "PRODUCT",
                "SIZE",
                PRODUCT_GROUP_ID,
                null,
                "사이즈 문의",
                "비밀로 사이즈 문의드립니다.",
                true,
                List.of());
    }

    public static RegisterQnaCommand registerProductQnaCommandWithImages() {
        return RegisterQnaCommand.of(
                USER_ID,
                SELLER_ID,
                "PRODUCT",
                "SHIPMENT",
                PRODUCT_GROUP_ID,
                null,
                "이미지 첨부 질문",
                "이미지를 첨부합니다.",
                false,
                List.of("https://example.com/image1.jpg", "https://example.com/image2.jpg"));
    }

    // ===== RegisterQnaCommand (ORDER) =====

    public static RegisterQnaCommand registerOrderQnaCommand() {
        return RegisterQnaCommand.of(
                USER_ID,
                SELLER_ID,
                "ORDER",
                "ORDER_PAYMENT",
                null,
                LEGACY_ORDER_ID,
                "주문 결제 문의",
                "결제가 정상 처리되었는지 확인하고 싶습니다.",
                false,
                List.of());
    }

    public static RegisterQnaCommand registerOrderQnaCommandWithImages() {
        return RegisterQnaCommand.of(
                USER_ID,
                SELLER_ID,
                "ORDER",
                "ORDER_PAYMENT",
                null,
                LEGACY_ORDER_ID,
                "주문 이미지 첨부",
                "사진을 첨부합니다.",
                false,
                List.of("https://example.com/order-image1.jpg"));
    }

    // ===== ModifyQnaCommand =====

    public static ModifyQnaCommand modifyQnaCommand() {
        return ModifyQnaCommand.of(
                QNA_ID,
                USER_ID,
                "수정된 질문 제목",
                "수정된 질문 내용입니다.",
                false,
                List.of());
    }

    public static ModifyQnaCommand modifyProductQnaCommand() {
        return ModifyQnaCommand.of(
                QNA_ID,
                USER_ID,
                "수정된 상품 질문",
                "수정된 상품 질문 내용입니다.",
                false,
                List.of());
    }

    public static ModifyQnaCommand modifyProductQnaCommandWithImages() {
        return ModifyQnaCommand.of(
                QNA_ID,
                USER_ID,
                "수정된 상품 질문",
                "수정된 내용입니다.",
                false,
                List.of("https://example.com/image1.jpg"));
    }

    public static ModifyQnaCommand modifyOrderQnaCommand() {
        return ModifyQnaCommand.of(
                QNA_ID,
                USER_ID,
                "수정된 주문 질문",
                "수정된 주문 질문 내용입니다.",
                false,
                List.of());
    }

    public static ModifyQnaCommand modifyOrderQnaCommandWithImages() {
        return ModifyQnaCommand.of(
                QNA_ID,
                USER_ID,
                "수정된 주문 질문",
                "수정된 내용입니다.",
                false,
                List.of("https://example.com/new-image1.jpg", "https://example.com/new-image2.jpg"));
    }

    // ===== RegisterQnaAnswerCommand =====

    public static RegisterQnaAnswerCommand registerQnaAnswerCommand() {
        return RegisterQnaAnswerCommand.of(QNA_ID, SELLER_ID, "안녕하세요. 현재 배송 준비 중이며 내일 출고 예정입니다.");
    }

    public static RegisterQnaAnswerCommand registerQnaAnswerCommand(Long qnaId) {
        return RegisterQnaAnswerCommand.of(qnaId, SELLER_ID, "배송 예정일은 내일입니다.");
    }

    // ===== ModifyQnaAnswerCommand =====

    public static ModifyQnaAnswerCommand modifyQnaAnswerCommand() {
        return ModifyQnaAnswerCommand.of(QNA_ID, QNA_ANSWER_ID, SELLER_ID, "수정된 답변 내용입니다.");
    }

    public static ModifyQnaAnswerCommand modifyQnaAnswerCommand(Long qnaId, Long qnaAnswerId) {
        return ModifyQnaAnswerCommand.of(qnaId, qnaAnswerId, SELLER_ID, "수정된 답변 내용입니다.");
    }

    // ===== Bundle Fixtures =====

    public static ProductQnaBundle productQnaBundle() {
        return new ProductQnaBundle(
                QnaFixtures.newProductQna(),
                QnaFixtures.newQnaProduct());
    }

    public static OrderQnaBundle orderQnaBundle() {
        return new OrderQnaBundle(
                QnaFixtures.newOrderQna(),
                QnaFixtures.newQnaOrder(),
                QnaFixtures.emptyQnaImages());
    }

    public static OrderQnaBundle orderQnaBundleWithImages() {
        return new OrderQnaBundle(
                QnaFixtures.newOrderQna(),
                QnaFixtures.newQnaOrder(),
                QnaFixtures.defaultQnaImages());
    }
}
