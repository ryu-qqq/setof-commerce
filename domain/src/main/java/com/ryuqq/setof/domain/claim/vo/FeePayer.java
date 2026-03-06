package com.ryuqq.setof.domain.claim.vo;

/**
 * 배송비 부담자.
 *
 * <p>클레임 수거 배송비를 누가 부담하는지 나타냅니다.
 */
public enum FeePayer {
    BUYER("구매자"),
    SELLER("판매자");

    private final String displayName;

    FeePayer(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
