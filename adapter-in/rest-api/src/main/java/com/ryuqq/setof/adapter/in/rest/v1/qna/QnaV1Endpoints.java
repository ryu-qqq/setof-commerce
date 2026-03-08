package com.ryuqq.setof.adapter.in.rest.v1.qna;

/**
 * QnaV1Endpoints - Q&A V1 Public API 엔드포인트 상수.
 *
 * <p>API-END-001: Endpoints final class + private 생성자.
 *
 * <p>API-END-002: static final 상수.
 *
 * <p>API-END-003: Path Variable 상수.
 *
 * <p>엔드포인트 매핑:
 *
 * <ul>
 *   <li>GET /api/v1/qna/product/{productGroupId} - 상품 Q&A 목록 조회
 *   <li>GET /api/v1/qna/my-page - 내 Q&A 목록 조회 (인증 필요)
 *   <li>POST /api/v1/qna - Q&A 질문 등록 (인증 필요)
 *   <li>PUT /api/v1/qna/{qnaId} - Q&A 질문 수정 (인증 필요)
 *   <li>POST /api/v1/qna/{qnaId}/reply - Q&A 답변 등록 (인증 필요)
 *   <li>PUT /api/v1/qna/{qnaId}/reply/{qnaAnswerId} - Q&A 답변 수정 (인증 필요)
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class QnaV1Endpoints {

    private QnaV1Endpoints() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /** V1 기본 경로 */
    public static final String BASE_V1 = "/api/v1";

    /** Q&A 기본 경로 (POST /api/v1/qna) */
    public static final String QNA = BASE_V1 + "/qna";

    /** 상품 Q&A 조회 경로 (GET /api/v1/qna/product/{productGroupId}) */
    public static final String QNA_PRODUCT = QNA + "/product";

    /** 내 Q&A 조회 경로 (GET /api/v1/qna/my-page) */
    public static final String QNA_MY_PAGE = QNA + "/my-page";

    /** Q&A 단건 Path Variable 세그먼트 */
    public static final String QNA_ID = "/{qnaId}";

    /** Q&A 단건 전체 경로 (PUT /api/v1/qna/{qnaId}) */
    public static final String QNA_BY_ID = QNA + QNA_ID;

    /** Q&A 답변 경로 세그먼트 */
    public static final String REPLY = "/reply";

    /** Q&A 답변 등록 전체 경로 (POST /api/v1/qna/{qnaId}/reply) */
    public static final String QNA_REPLY = QNA + QNA_ID + REPLY;

    /** Q&A 답변 단건 Path Variable 세그먼트 */
    public static final String QNA_ANSWER_ID = "/{qnaAnswerId}";

    /** Q&A 답변 수정 전체 경로 (PUT /api/v1/qna/{qnaId}/reply/{qnaAnswerId}) */
    public static final String QNA_REPLY_BY_ID = QNA + QNA_ID + REPLY + QNA_ANSWER_ID;

    /** 상품그룹 Path Variable 세그먼트 */
    public static final String PRODUCT_GROUP_ID = "/{productGroupId}";

    /** qnaId Path Variable 이름 */
    public static final String PATH_QNA_ID = "qnaId";

    /** qnaAnswerId Path Variable 이름 */
    public static final String PATH_QNA_ANSWER_ID = "qnaAnswerId";

    /** productGroupId Path Variable 이름 */
    public static final String PATH_PRODUCT_GROUP_ID = "productGroupId";
}
