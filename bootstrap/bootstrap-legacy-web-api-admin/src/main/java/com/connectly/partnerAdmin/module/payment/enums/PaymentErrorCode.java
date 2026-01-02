package com.connectly.partnerAdmin.module.payment.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PaymentErrorCode implements EnumType {
    // 400 BAD REQUEST
    INVALID_PAYMENT_REFUND_INFO("PAYMENT_001", HttpStatus.BAD_REQUEST),

    //404 NOT FOUND
    PAYMENT_NOT_FOUND("PAYMENT_002", HttpStatus.NOT_FOUND),



    ;

    private final String code;

    private final HttpStatus httpStatus;

    @Override
    public String getName() {
        return code;
    }

    @Override
    public String getDescription() {
        return name();
    }
}
