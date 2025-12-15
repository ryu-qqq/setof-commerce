package com.setof.connectly.module.payload;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Getter
@SuperBuilder
public class ErrorResponse extends Response {

    private final String timestamp =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    private final String error;

    public ErrorResponse(int status, String message, String error) {
        super(status, message);
        this.error = error;
    }

    public static ErrorResponse of(
            HttpStatus httpStatus, String exceptionClassName, String detailMessage) {
        return ErrorResponse.builder()
                .error(exceptionClassName)
                .status(httpStatus.value())
                .message(detailMessage)
                .build();
    }
}
