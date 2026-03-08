package com.ryuqq.setof.adapter.in.rest.v1.faq;

import com.ryuqq.setof.adapter.in.rest.v1.faq.dto.request.SearchFaqsV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.faq.dto.response.FaqV1ApiResponse;
import com.ryuqq.setof.application.faq.dto.response.FaqResult;
import com.ryuqq.setof.domain.faq.vo.FaqType;
import java.util.List;

/**
 * FAQ V1 API 테스트 Fixtures.
 *
 * <p>FAQ 관련 API Request/Response 및 Application Result 객체를 생성하는 테스트 유틸리티입니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class FaqApiFixtures {

    private FaqApiFixtures() {}

    // ===== SearchFaqsV1ApiRequest =====

    public static SearchFaqsV1ApiRequest searchRequest() {
        return new SearchFaqsV1ApiRequest(FaqType.MEMBER_LOGIN);
    }

    public static SearchFaqsV1ApiRequest searchRequest(FaqType faqType) {
        return new SearchFaqsV1ApiRequest(faqType);
    }

    // ===== FaqV1ApiResponse =====

    public static FaqV1ApiResponse faqResponse() {
        return new FaqV1ApiResponse("MEMBER_LOGIN", "회원 가입은 어떻게 하나요?", "회원 가입 방법 안내입니다.");
    }

    public static FaqV1ApiResponse faqResponse(String faqType, String title, String contents) {
        return new FaqV1ApiResponse(faqType, title, contents);
    }

    public static List<FaqV1ApiResponse> faqResponseList() {
        return List.of(faqResponse(), faqResponse("SHIPPING", "배송 기간은 얼마나 되나요?", "배송 기간 안내입니다."));
    }

    // ===== FaqResult =====

    public static FaqResult faqResult(long faqId) {
        return FaqResult.of(
                faqId, FaqType.MEMBER_LOGIN, "회원 가입은 어떻게 하나요?", "회원 가입 방법 안내입니다.", 0, null);
    }

    public static FaqResult faqResult(
            long faqId,
            FaqType faqType,
            String title,
            String contents,
            int displayOrder,
            Integer topDisplayOrder) {
        return FaqResult.of(faqId, faqType, title, contents, displayOrder, topDisplayOrder);
    }

    public static List<FaqResult> faqResultList() {
        return List.of(
                faqResult(1L),
                faqResult(2L, FaqType.SHIPPING, "배송 기간은 얼마나 되나요?", "배송 기간 안내입니다.", 1, null));
    }
}
