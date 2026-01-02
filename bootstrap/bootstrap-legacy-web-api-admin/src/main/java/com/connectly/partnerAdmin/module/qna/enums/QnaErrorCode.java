package com.connectly.partnerAdmin.module.qna.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum QnaErrorCode implements EnumType {

    // 400 BAD REQUEST
    INVALID_QNA("QNA_001",HttpStatus.BAD_REQUEST),

    //404 NOT FOUND
    QNA_NOT_FOUND("QNA_002", HttpStatus.NOT_FOUND),



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
