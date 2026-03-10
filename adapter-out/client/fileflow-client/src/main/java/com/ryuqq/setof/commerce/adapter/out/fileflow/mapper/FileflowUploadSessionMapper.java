package com.ryuqq.setof.commerce.adapter.out.fileflow.mapper;

import com.ryuqq.fileflow.sdk.model.session.CompleteSingleUploadSessionRequest;
import com.ryuqq.fileflow.sdk.model.session.CreateSingleUploadSessionRequest;
import com.ryuqq.fileflow.sdk.model.session.SingleUploadSessionResponse;
import com.ryuqq.setof.commerce.adapter.out.fileflow.config.FileflowClientProperties;
import com.ryuqq.setof.domain.session.vo.CompleteUploadSession;
import com.ryuqq.setof.domain.session.vo.UploadSessionRequest;
import com.ryuqq.setof.domain.session.vo.UploadSessionResult;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * FileFlow 업로드 세션 매퍼.
 *
 * <p>Domain VO ↔ FileFlow SDK 모델 변환을 담당합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class FileflowUploadSessionMapper {

    private static final String ACCESS_TYPE_PUBLIC = "PUBLIC";
    private static final String SOURCE = "setof-commerce";

    private final FileflowClientProperties properties;

    public FileflowUploadSessionMapper(FileflowClientProperties properties) {
        this.properties = properties;
    }

    public CreateSingleUploadSessionRequest toSdkCreateRequest(UploadSessionRequest request) {
        return new CreateSingleUploadSessionRequest(
                request.filename(),
                request.contentType(),
                ACCESS_TYPE_PUBLIC,
                request.directory().path(),
                SOURCE);
    }

    public CompleteSingleUploadSessionRequest toSdkCompleteRequest(
            CompleteUploadSession completeUploadSession) {
        return new CompleteSingleUploadSessionRequest(
                completeUploadSession.fileSize(), completeUploadSession.etag());
    }

    public UploadSessionResult toDomainResult(SingleUploadSessionResponse session) {
        Instant expiresAt = parseInstantOrNull(session.expiresAt());
        String accessUrl = buildAccessUrl(session.s3Key());

        return new UploadSessionResult(
                session.sessionId(), session.presignedUrl(), session.s3Key(), expiresAt, accessUrl);
    }

    private String buildAccessUrl(String s3Key) {
        String cdnDomain = properties.cdnDomain();
        if (cdnDomain == null || cdnDomain.isBlank() || s3Key == null) {
            return null;
        }
        return "https://" + cdnDomain + "/" + s3Key;
    }

    private Instant parseInstantOrNull(String timestamp) {
        if (timestamp == null || timestamp.isBlank()) {
            return null;
        }
        try {
            return Instant.parse(timestamp);
        } catch (Exception e) {
            return null;
        }
    }
}
