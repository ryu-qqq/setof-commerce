package com.ryuqq.setof.domain.shipment.vo;

/** 발송인 정보 Value Object. */
public record SenderInfo(String name, String email, String phone) {

    public SenderInfo {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("발송인명은 필수입니다");
        }
    }

    public static SenderInfo of(String name, String email, String phone) {
        return new SenderInfo(name, email, phone);
    }
}
