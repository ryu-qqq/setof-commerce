package com.ryuqq.setof.domain.shippingaddress.exception;

import com.ryuqq.setof.domain.common.exception.ErrorCode;

/**
 * ShippingAddress Bounded Context 에러 코드
 *
 * <p>배송지 도메인에서 발생하는 모든 비즈니스 예외의 에러 코드를 정의합니다.
 *
 * <p>코드 형식: SHP-XXX
 *
 * <ul>
 *   <li>SHP-001 ~ SHP-099: 배송지 조회 관련
 *   <li>SHP-100 ~ SHP-199: 배송지 정보 유효성 관련
 *   <li>SHP-200 ~ SHP-299: 배송지 비즈니스 규칙 관련
 * </ul>
 */
public enum ShippingAddressErrorCode implements ErrorCode {

    // === 배송지 조회 관련 (SHP-001 ~ SHP-099) ===
    SHIPPING_ADDRESS_NOT_FOUND("SHP-001", 404, "배송지를 찾을 수 없습니다."),

    // === 배송지 정보 유효성 관련 (SHP-100 ~ SHP-199) ===
    INVALID_SHIPPING_ADDRESS_ID("SHP-100", 400, "배송지 ID는 null이 아닌 양수여야 합니다."),
    INVALID_ADDRESS_NAME("SHP-101", 400, "배송지 이름이 올바르지 않습니다. (1~30자)"),
    INVALID_RECEIVER_NAME("SHP-102", 400, "수령인 이름이 올바르지 않습니다. (1~20자)"),
    INVALID_RECEIVER_PHONE("SHP-103", 400, "수령인 연락처가 올바르지 않습니다. (10~15자리 숫자)"),
    INVALID_DELIVERY_ADDRESS("SHP-104", 400, "배송 주소가 올바르지 않습니다."),
    ADDRESS_REQUIRED("SHP-105", 400, "도로명주소 또는 지번주소 중 하나는 필수입니다."),
    ADDRESS_TOO_LONG("SHP-106", 400, "주소가 최대 길이를 초과했습니다."),
    ZIP_CODE_REQUIRED("SHP-107", 400, "우편번호는 필수입니다."),
    INVALID_ZIP_CODE("SHP-108", 400, "우편번호 형식이 올바르지 않습니다. (5~10자)"),
    INVALID_DELIVERY_REQUEST("SHP-109", 400, "배송 요청사항이 최대 길이(200자)를 초과했습니다."),

    // === 배송지 비즈니스 규칙 관련 (SHP-200 ~ SHP-299) ===
    SHIPPING_ADDRESS_LIMIT_EXCEEDED("SHP-200", 400, "배송지는 최대 5개까지만 등록할 수 있습니다."),
    CANNOT_DELETE_ONLY_ADDRESS("SHP-201", 400, "유일한 배송지는 삭제할 수 없습니다."),
    NOT_OWNER("SHP-202", 403, "해당 배송지에 대한 권한이 없습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;

    ShippingAddressErrorCode(String code, int httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
