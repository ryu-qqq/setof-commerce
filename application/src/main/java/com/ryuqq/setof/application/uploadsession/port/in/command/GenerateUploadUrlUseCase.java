package com.ryuqq.setof.application.uploadsession.port.in.command;

import com.ryuqq.setof.application.uploadsession.dto.command.GenerateUploadUrlCommand;
import com.ryuqq.setof.application.uploadsession.dto.query.PresignedUrlResult;

/**
 * Presigned URL 발급 UseCase.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface GenerateUploadUrlUseCase {

    PresignedUrlResult execute(GenerateUploadUrlCommand command);
}
