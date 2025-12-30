package com.connectly.partnerAdmin.module.display.enums;

import com.connectly.partnerAdmin.module.common.enums.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ContentErrorCode implements EnumType {

    // 400 BAD REQUEST
    INVALID_CONTENT("CONTENT_001",HttpStatus.BAD_REQUEST),


    //404 NOT FOUND
    CONTENT_NOT_FOUND("CONTENT_004", HttpStatus.NOT_FOUND),



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
