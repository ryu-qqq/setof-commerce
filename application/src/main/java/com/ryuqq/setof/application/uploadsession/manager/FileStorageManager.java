package com.ryuqq.setof.application.uploadsession.manager;

import com.ryuqq.setof.application.common.port.out.UploadSessionPort;
import com.ryuqq.setof.domain.session.vo.CompleteUploadSession;
import com.ryuqq.setof.domain.session.vo.UploadSessionRequest;
import com.ryuqq.setof.domain.session.vo.UploadSessionResult;
import org.springframework.stereotype.Component;

/**
 * FileStorageManager - 업로드 세션 관리자.
 *
 * <p>도메인 VO를 받아 UploadSessionPort에 위임합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class FileStorageManager {

    private final UploadSessionPort uploadSessionPort;

    public FileStorageManager(UploadSessionPort uploadSessionPort) {
        this.uploadSessionPort = uploadSessionPort;
    }

    public UploadSessionResult generateUploadUrl(UploadSessionRequest request) {
        return uploadSessionPort.generateUploadUrl(request);
    }

    public void completeUploadSession(CompleteUploadSession completeUploadSession) {
        uploadSessionPort.completeUploadSession(completeUploadSession);
    }
}
