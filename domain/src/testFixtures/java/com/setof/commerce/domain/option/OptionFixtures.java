package com.setof.commerce.domain.option;

import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionGroup;
import com.ryuqq.setof.domain.productgroup.aggregate.SellerOptionValue;
import com.ryuqq.setof.domain.productgroup.id.ProductGroupId;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionGroupId;
import com.ryuqq.setof.domain.productgroup.id.SellerOptionValueId;
import com.ryuqq.setof.domain.productgroup.vo.OptionGroupName;
import com.ryuqq.setof.domain.productgroup.vo.OptionValueName;
import java.time.Instant;
import java.util.List;

/**
 * Option 도메인 테스트 Fixtures.
 *
 * <p>테스트에서 SellerOptionGroup, SellerOptionValue 관련 객체들을 생성합니다.
 */
public final class OptionFixtures {

    private OptionFixtures() {}

    // ===== 상수 =====
    public static final String DEFAULT_OPTION_VALUE_BLACK = "블랙";
    public static final String DEFAULT_OPTION_VALUE_WHITE = "화이트";
    public static final String DEFAULT_OPTION_VALUE_M = "M";
    public static final String DEFAULT_OPTION_VALUE_L = "L";

    private static final SellerOptionGroupId DEFAULT_GROUP_ID = SellerOptionGroupId.of(1L);
    private static final ProductGroupId DEFAULT_PRODUCT_GROUP_ID = ProductGroupId.of(1L);

    // ===== SellerOptionValue (구 OptionDetail) Fixtures =====

    /** 신규 옵션 상세 생성. */
    public static SellerOptionValue newDetail() {
        return SellerOptionValue.forNew(
                DEFAULT_GROUP_ID, OptionValueName.of(DEFAULT_OPTION_VALUE_BLACK), 0);
    }

    /** 신규 옵션 상세 생성 (특정 값, 순서). */
    public static SellerOptionValue newDetail(String value, int sortOrder) {
        return SellerOptionValue.forNew(DEFAULT_GROUP_ID, OptionValueName.of(value), sortOrder);
    }

    /** 활성 상태 옵션 상세 복원. */
    public static SellerOptionValue activeDetail() {
        return SellerOptionValue.reconstitute(
                SellerOptionValueId.of(1L),
                DEFAULT_GROUP_ID,
                OptionValueName.of(DEFAULT_OPTION_VALUE_BLACK),
                0,
                DeletionStatus.active());
    }

    /** 특정 ID의 활성 상태 옵션 상세 복원. */
    public static SellerOptionValue activeDetail(Long id) {
        return SellerOptionValue.reconstitute(
                SellerOptionValueId.of(id),
                DEFAULT_GROUP_ID,
                OptionValueName.of(DEFAULT_OPTION_VALUE_BLACK),
                0,
                DeletionStatus.active());
    }

    /** 특정 값의 활성 상태 옵션 상세 복원. */
    public static SellerOptionValue activeDetail(Long id, String value, int sortOrder) {
        return SellerOptionValue.reconstitute(
                SellerOptionValueId.of(id),
                DEFAULT_GROUP_ID,
                OptionValueName.of(value),
                sortOrder,
                DeletionStatus.active());
    }

    /** 기본 색상 옵션 상세 목록 (블랙, 화이트). */
    public static List<SellerOptionValue> defaultColorDetails() {
        return List.of(
                activeDetail(1L, DEFAULT_OPTION_VALUE_BLACK, 0),
                activeDetail(2L, DEFAULT_OPTION_VALUE_WHITE, 1));
    }

    /** 기본 사이즈 옵션 상세 목록 (M, L). */
    public static List<SellerOptionValue> defaultSizeDetails() {
        return List.of(
                activeDetail(3L, DEFAULT_OPTION_VALUE_M, 0),
                activeDetail(4L, DEFAULT_OPTION_VALUE_L, 1));
    }

    // ===== SellerOptionGroup (구 OptionGroup) Fixtures =====

    /** 신규 색상 옵션 그룹 생성 (상세 없음). */
    public static SellerOptionGroup newColorGroup() {
        return SellerOptionGroup.forNew(
                DEFAULT_PRODUCT_GROUP_ID, OptionGroupName.of("색상"), 0, List.of());
    }

    /** 신규 사이즈 옵션 그룹 생성 (상세 없음). */
    public static SellerOptionGroup newSizeGroup() {
        return SellerOptionGroup.forNew(
                DEFAULT_PRODUCT_GROUP_ID, OptionGroupName.of("사이즈"), 1, List.of());
    }

    /** 활성 상태의 색상 옵션 그룹 복원 (블랙, 화이트 포함). */
    public static SellerOptionGroup activeColorGroup() {
        return SellerOptionGroup.reconstitute(
                SellerOptionGroupId.of(1L),
                DEFAULT_PRODUCT_GROUP_ID,
                OptionGroupName.of("색상"),
                0,
                defaultColorDetails(),
                DeletionStatus.active());
    }

    /** 활성 상태의 사이즈 옵션 그룹 복원 (M, L 포함). */
    public static SellerOptionGroup activeSizeGroup() {
        return SellerOptionGroup.reconstitute(
                SellerOptionGroupId.of(2L),
                DEFAULT_PRODUCT_GROUP_ID,
                OptionGroupName.of("사이즈"),
                1,
                defaultSizeDetails(),
                DeletionStatus.active());
    }

    /** 특정 ID의 활성 상태 옵션 그룹 복원. */
    public static SellerOptionGroup activeColorGroup(Long id) {
        return SellerOptionGroup.reconstitute(
                SellerOptionGroupId.of(id),
                DEFAULT_PRODUCT_GROUP_ID,
                OptionGroupName.of("색상"),
                0,
                defaultColorDetails(),
                DeletionStatus.active());
    }

    /** 비활성 상태의 옵션 그룹 복원. */
    public static SellerOptionGroup inactiveGroup() {
        return SellerOptionGroup.reconstitute(
                SellerOptionGroupId.of(3L),
                DEFAULT_PRODUCT_GROUP_ID,
                OptionGroupName.of("색상"),
                0,
                defaultColorDetails(),
                DeletionStatus.active());
    }

    /** 삭제된 상태의 옵션 그룹 복원. */
    public static SellerOptionGroup deletedGroup() {
        return SellerOptionGroup.reconstitute(
                SellerOptionGroupId.of(4L),
                DEFAULT_PRODUCT_GROUP_ID,
                OptionGroupName.of("색상"),
                0,
                defaultColorDetails(),
                DeletionStatus.deletedAt(Instant.now()));
    }

    /** 상세 없는 빈 옵션 그룹 복원. */
    public static SellerOptionGroup emptyGroup() {
        return SellerOptionGroup.reconstitute(
                SellerOptionGroupId.of(5L),
                DEFAULT_PRODUCT_GROUP_ID,
                OptionGroupName.of("색상"),
                0,
                List.of(),
                DeletionStatus.active());
    }
}
