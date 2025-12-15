package com.setof.connectly.module.payload;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Getter
@SuperBuilder
public class CheckedErrorResponse extends ErrorResponse {

    private String title;

    public CheckedErrorResponse(int status, String message, String error, String title) {
        super(status, message, error);
        this.title = title;
    }

    public static CheckedErrorResponse of(
            HttpStatus httpStatus, String exceptionClassName, String detailMessage, String title) {
        return CheckedErrorResponse.builder()
                .error(exceptionClassName)
                .status(httpStatus.value())
                .message(detailMessage)
                .title(title)
                .build();
    }
}
