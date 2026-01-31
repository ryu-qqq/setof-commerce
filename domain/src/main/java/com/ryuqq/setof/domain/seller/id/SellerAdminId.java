package com.ryuqq.setof.domain.seller.id;

/**
 * 셀러 관리자 ID Value Object.
 *
 * <p>셀러 관리자(SellerAdmin)를 식별하는 ID입니다.
 */
public record SellerAdminId(Long value) {

    public static SellerAdminId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("SellerAdminId 값은 null일 수 없습니다");
        }
        return new SellerAdminId(value);
    }

    public static SellerAdminId forNew() {
        return new SellerAdminId(null);
    }

    public boolean isNew() {
        return value == null;
    }
}
