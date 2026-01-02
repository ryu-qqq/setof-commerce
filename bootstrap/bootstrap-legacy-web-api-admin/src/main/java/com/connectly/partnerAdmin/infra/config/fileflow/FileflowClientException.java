package com.connectly.partnerAdmin.infra.config.fileflow;

/**
 * Fileflow 클라이언트 예외.
 *
 * <p>Fileflow 서비스 호출 중 발생하는 예외를 나타냅니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public class FileflowClientException extends RuntimeException {

    public FileflowClientException(String message) {
        super(message);
    }

    public FileflowClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
