package com.ryuqq.setof.application.seller.dto.response;

/**
 * CS 정보 응답 DTO
 *
 * @param email CS 이메일
 * @param mobilePhone CS 휴대폰
 * @param landlinePhone CS 유선전화
 * @author development-team
 * @since 1.0.0
 */
public record CsInfoResponse(String email, String mobilePhone, String landlinePhone) {

    /**
     * Static Factory Method
     *
     * @param email CS 이메일
     * @param mobilePhone CS 휴대폰
     * @param landlinePhone CS 유선전화
     * @return CsInfoResponse 인스턴스
     */
    public static CsInfoResponse of(String email, String mobilePhone, String landlinePhone) {
        return new CsInfoResponse(email, mobilePhone, landlinePhone);
    }
}
