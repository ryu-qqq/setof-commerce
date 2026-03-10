package com.ryuqq.setof.application.uploadsession.factory;

import com.ryuqq.setof.application.uploadsession.dto.command.CompleteUploadSessionCommand;
import com.ryuqq.setof.application.uploadsession.dto.command.GenerateUploadUrlCommand;
import com.ryuqq.setof.domain.session.vo.CompleteUploadSession;
import com.ryuqq.setof.domain.session.vo.UploadSessionRequest;
import org.springframework.stereotype.Component;

/**
 * UploadSessionFactory - 업로드 세션 도메인 객체 생성 팩토리.
 *
 * <p>Application Command를 Domain VO로 변환합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class UploadSessionFactory {

    public UploadSessionRequest toUploadSessionRequest(GenerateUploadUrlCommand command) {
        return UploadSessionRequest.of(
                command.directory(),
                command.filename(),
                command.contentType(),
                command.contentLength());
    }

    public CompleteUploadSession toCompleteUploadSession(CompleteUploadSessionCommand command) {
        if (command.fileSize() <= 0) {
            return CompleteUploadSession.withDefaultSize(command.sessionId(), command.etag());
        }
        return CompleteUploadSession.of(command.sessionId(), command.fileSize(), command.etag());
    }
}
