package com.ryuqq.setof.adapter.in.rest.admin.productnotice;

import com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.dto.command.RegisterProductNoticeApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.productnotice.dto.command.UpdateProductNoticeApiRequest;
import java.util.List;

/**
 * ProductNotice API 테스트 Fixtures.
 *
 * <p>상품 그룹 고시정보 API 테스트에서 사용되는 Request 객체들을 생성합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public final class ProductNoticeApiFixtures {

    private ProductNoticeApiFixtures() {}

    // ===== 기본 상수 =====
    public static final Long DEFAULT_PRODUCT_GROUP_ID = 1L;
    public static final Long DEFAULT_NOTICE_FIELD_ID = 10L;
    public static final String DEFAULT_FIELD_NAME = "소재";
    public static final String DEFAULT_FIELD_VALUE = "면 100%";

    // ===== Register Request Fixtures =====

    public static RegisterProductNoticeApiRequest registerRequest() {
        return new RegisterProductNoticeApiRequest(
                List.of(
                        new RegisterProductNoticeApiRequest.NoticeEntryApiRequest(
                                DEFAULT_NOTICE_FIELD_ID, DEFAULT_FIELD_NAME, DEFAULT_FIELD_VALUE)));
    }

    public static RegisterProductNoticeApiRequest registerRequest(
            List<RegisterProductNoticeApiRequest.NoticeEntryApiRequest> entries) {
        return new RegisterProductNoticeApiRequest(entries);
    }

    public static RegisterProductNoticeApiRequest.NoticeEntryApiRequest
            registerNoticeEntryRequest() {
        return new RegisterProductNoticeApiRequest.NoticeEntryApiRequest(
                DEFAULT_NOTICE_FIELD_ID, DEFAULT_FIELD_NAME, DEFAULT_FIELD_VALUE);
    }

    public static RegisterProductNoticeApiRequest.NoticeEntryApiRequest registerNoticeEntryRequest(
            Long noticeFieldId, String fieldName, String fieldValue) {
        return new RegisterProductNoticeApiRequest.NoticeEntryApiRequest(
                noticeFieldId, fieldName, fieldValue);
    }

    // ===== Update Request Fixtures =====

    public static UpdateProductNoticeApiRequest updateRequest() {
        return new UpdateProductNoticeApiRequest(
                List.of(
                        new UpdateProductNoticeApiRequest.NoticeEntryApiRequest(
                                DEFAULT_NOTICE_FIELD_ID, DEFAULT_FIELD_NAME, "폴리에스터 100%")));
    }

    public static UpdateProductNoticeApiRequest updateRequest(
            List<UpdateProductNoticeApiRequest.NoticeEntryApiRequest> entries) {
        return new UpdateProductNoticeApiRequest(entries);
    }

    public static UpdateProductNoticeApiRequest.NoticeEntryApiRequest updateNoticeEntryRequest() {
        return new UpdateProductNoticeApiRequest.NoticeEntryApiRequest(
                DEFAULT_NOTICE_FIELD_ID, DEFAULT_FIELD_NAME, "폴리에스터 100%");
    }
}
