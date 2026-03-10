package com.ryuqq.setof.application.uploadsession.dto.command;

import com.ryuqq.setof.domain.session.vo.UploadDirectory;

/**
 * Presigned URL 발급 커맨드.
 *
 * @param directory 업로드 디렉토리
 * @param filename 파일명
 * @param contentType MIME 타입
 * @param contentLength 파일 크기 (바이트)
 * @author ryu-qqq
 * @since 1.1.0
 */
public record GenerateUploadUrlCommand(
        UploadDirectory directory, String filename, String contentType, long contentLength) {}
