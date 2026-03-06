package com.setof.commerce.domain.productnotice;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNotice;
import com.ryuqq.setof.domain.productnotice.aggregate.ProductNoticeEntry;
import com.ryuqq.setof.domain.productnotice.id.NoticeFieldId;
import com.ryuqq.setof.domain.productnotice.id.ProductNoticeEntryId;
import com.ryuqq.setof.domain.productnotice.id.ProductNoticeId;
import com.ryuqq.setof.domain.productnotice.vo.NoticeFieldValue;
import com.ryuqq.setof.domain.productnotice.vo.ProductNoticeUpdateData;
import java.util.List;

/**
 * ProductNotice 도메인 테스트 Fixtures.
 *
 * <p>테스트에서 ProductNotice 관련 객체들을 생성합니다.
 */
public final class ProductNoticeFixtures {

    private ProductNoticeFixtures() {}

    // ===== 상수 =====
    public static final String DEFAULT_FIELD_NAME = "소재";
    public static final String DEFAULT_FIELD_VALUE = "면 100%";
    public static final Long DEFAULT_PRODUCT_GROUP_ID = 100L;

    // ===== ProductNotice Aggregate Fixtures =====

    /** 기본 엔트리를 포함한 신규 상품고시를 생성한다. */
    public static ProductNotice newNotice() {
        return ProductNotice.forNew(
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                defaultEntries(),
                CommonVoFixtures.now());
    }

    /** 특정 ProductGroupId로 신규 상품고시를 생성한다. */
    public static ProductNotice newNotice(ProductGroupId productGroupId) {
        return ProductNotice.forNew(productGroupId, defaultEntries(), CommonVoFixtures.now());
    }

    /** ID가 할당된 활성 상태의 상품고시를 복원한다. */
    public static ProductNotice activeNotice() {
        return ProductNotice.reconstitute(
                ProductNoticeId.of(1L),
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                reconstitutedDefaultEntries(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 특정 ID로 활성 상태의 상품고시를 복원한다. */
    public static ProductNotice activeNotice(Long id) {
        return ProductNotice.reconstitute(
                ProductNoticeId.of(id),
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                reconstitutedDefaultEntries(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    /** 엔트리가 비어있는 상품고시를 복원한다. */
    public static ProductNotice emptyNotice() {
        return ProductNotice.reconstitute(
                ProductNoticeId.of(2L),
                ProductGroupId.of(DEFAULT_PRODUCT_GROUP_ID),
                List.of(),
                CommonVoFixtures.yesterday(),
                CommonVoFixtures.yesterday());
    }

    // ===== ProductNoticeEntry Fixtures =====

    /** 기본 필드값을 가진 신규 고시 항목을 생성한다. */
    public static ProductNoticeEntry defaultNoticeEntry() {
        return ProductNoticeEntry.forNew(NoticeFieldId.forNew(), defaultFieldValue(), 1);
    }

    /** 커스텀 필드명/필드값을 가진 신규 고시 항목을 생성한다. */
    public static ProductNoticeEntry noticeEntry(String fieldName, String fieldValue) {
        return ProductNoticeEntry.forNew(
                NoticeFieldId.forNew(), NoticeFieldValue.of(fieldName, fieldValue), 1);
    }

    /** 일반적인 상품고시 항목 목록(신규)을 반환한다. */
    public static List<ProductNoticeEntry> defaultEntries() {
        return List.of(
                ProductNoticeEntry.forNew(
                        NoticeFieldId.forNew(), NoticeFieldValue.of("소재", "면 100%"), 1),
                ProductNoticeEntry.forNew(
                        NoticeFieldId.forNew(), NoticeFieldValue.of("세탁방법", "손세탁"), 2),
                ProductNoticeEntry.forNew(
                        NoticeFieldId.forNew(), NoticeFieldValue.of("제조국", "대한민국"), 3),
                ProductNoticeEntry.forNew(
                        NoticeFieldId.forNew(), NoticeFieldValue.of("제조자", "테스트제조사"), 4));
    }

    /** 복원된(ID 할당된) 기본 항목 목록을 반환한다. */
    public static List<ProductNoticeEntry> reconstitutedDefaultEntries() {
        return List.of(
                ProductNoticeEntry.reconstitute(
                        ProductNoticeEntryId.of(10L),
                        ProductNoticeId.of(1L),
                        NoticeFieldId.of(1L),
                        NoticeFieldValue.of("소재", "면 100%"),
                        1),
                ProductNoticeEntry.reconstitute(
                        ProductNoticeEntryId.of(11L),
                        ProductNoticeId.of(1L),
                        NoticeFieldId.of(2L),
                        NoticeFieldValue.of("세탁방법", "손세탁"),
                        2),
                ProductNoticeEntry.reconstitute(
                        ProductNoticeEntryId.of(12L),
                        ProductNoticeId.of(1L),
                        NoticeFieldId.of(3L),
                        NoticeFieldValue.of("제조국", "대한민국"),
                        3),
                ProductNoticeEntry.reconstitute(
                        ProductNoticeEntryId.of(13L),
                        ProductNoticeId.of(1L),
                        NoticeFieldId.of(4L),
                        NoticeFieldValue.of("제조자", "테스트제조사"),
                        4));
    }

    // ===== VO Fixtures =====

    /** 기본 NoticeFieldValue를 반환한다. */
    public static NoticeFieldValue defaultFieldValue() {
        return NoticeFieldValue.of(DEFAULT_FIELD_NAME, DEFAULT_FIELD_VALUE);
    }

    /** 기본 엔트리 목록으로 ProductNoticeUpdateData를 생성한다. */
    public static ProductNoticeUpdateData defaultUpdateData() {
        return ProductNoticeUpdateData.of(defaultEntries(), CommonVoFixtures.now());
    }

    /** 빈 엔트리 목록으로 ProductNoticeUpdateData를 생성한다. */
    public static ProductNoticeUpdateData emptyUpdateData() {
        return ProductNoticeUpdateData.of(List.of(), CommonVoFixtures.now());
    }

    // ===== NoticeFieldId Fixtures =====

    /** 기본 NoticeFieldId를 반환한다. */
    public static NoticeFieldId defaultNoticeFieldId() {
        return NoticeFieldId.of(1L);
    }

    /** 신규 NoticeFieldId를 반환한다. */
    public static NoticeFieldId newNoticeFieldId() {
        return NoticeFieldId.forNew();
    }
}
