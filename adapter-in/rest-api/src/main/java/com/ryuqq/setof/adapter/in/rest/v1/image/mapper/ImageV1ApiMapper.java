package com.ryuqq.setof.adapter.in.rest.v1.image.mapper;

import com.ryuqq.setof.adapter.in.rest.v1.image.dto.request.PreSignedUrlV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.image.dto.request.UploadCompleteV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.image.dto.response.PreSignedUrlV1ApiResponse;
import com.ryuqq.setof.application.uploadsession.dto.command.CompleteUploadSessionCommand;
import com.ryuqq.setof.application.uploadsession.dto.command.GenerateUploadUrlCommand;
import com.ryuqq.setof.application.uploadsession.dto.query.PresignedUrlResult;
import com.ryuqq.setof.domain.session.vo.UploadDirectory;
import org.springframework.stereotype.Component;

/**
 * ImageV1ApiMapper - 이미지 업로드 V1 API 매퍼.
 *
 * <p>레거시 요청/응답 형식을 Application Layer 커맨드/결과로 변환합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ImageV1ApiMapper {

    /**
     * contentType을 파일 확장자로부터 추론합니다.
     *
     * @param fileName 파일명
     * @return MIME 타입
     */
    private String resolveContentType(String fileName) {
        if (fileName == null) {
            return "application/octet-stream";
        }
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        if (lower.endsWith(".png")) {
            return "image/png";
        }
        if (lower.endsWith(".gif")) {
            return "image/gif";
        }
        if (lower.endsWith(".webp")) {
            return "image/webp";
        }
        if (lower.endsWith(".svg")) {
            return "image/svg+xml";
        }
        if (lower.endsWith(".bmp")) {
            return "image/bmp";
        }
        return "application/octet-stream";
    }

    public GenerateUploadUrlCommand toGenerateCommand(PreSignedUrlV1ApiRequest request) {
        UploadDirectory directory = UploadDirectory.fromName(request.imagePath());
        String contentType = resolveContentType(request.fileName());

        return new GenerateUploadUrlCommand(directory, request.fileName(), contentType, 0L);
    }

    public CompleteUploadSessionCommand toCompleteCommand(UploadCompleteV1ApiRequest request) {
        return CompleteUploadSessionCommand.of(request.sessionId(), request.etag());
    }

    public PreSignedUrlV1ApiResponse toApiResponse(PresignedUrlResult result) {
        return new PreSignedUrlV1ApiResponse(
                result.sessionId(), result.presignedUrl(), result.objectKey());
    }
}
