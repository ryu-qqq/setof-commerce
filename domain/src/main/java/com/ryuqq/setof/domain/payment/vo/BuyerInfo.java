package com.ryuqq.setof.domain.payment.vo;

/** 구매자 정보 Value Object. */
public record BuyerInfo(String name, String email, String phone) {

    public BuyerInfo {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("구매자명은 필수입니다");
        }
    }

    public static BuyerInfo of(String name, String email, String phone) {
        return new BuyerInfo(name, email, phone);
    }
}
