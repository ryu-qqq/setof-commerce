package com.ryuqq.setof.application.uploadsession.service.command;

import com.ryuqq.setof.application.uploadsession.dto.command.GenerateUploadUrlCommand;
import com.ryuqq.setof.application.uploadsession.dto.query.PresignedUrlResult;
import com.ryuqq.setof.application.uploadsession.factory.UploadSessionFactory;
import com.ryuqq.setof.application.uploadsession.manager.FileStorageManager;
import com.ryuqq.setof.application.uploadsession.port.in.command.GenerateUploadUrlUseCase;
import com.ryuqq.setof.domain.session.vo.UploadSessionRequest;
import com.ryuqq.setof.domain.session.vo.UploadSessionResult;
import org.springframework.stereotype.Service;

/**
 * Presigned URL 발급 서비스.
 *
 * <p>Command → Factory → Domain VO → Manager → Port → Adapter → Domain Result → Application
 * Response
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class GenerateUploadUrlService implements GenerateUploadUrlUseCase {

    private final UploadSessionFactory uploadSessionFactory;
    private final FileStorageManager fileStorageManager;

    public GenerateUploadUrlService(
            UploadSessionFactory uploadSessionFactory, FileStorageManager fileStorageManager) {
        this.uploadSessionFactory = uploadSessionFactory;
        this.fileStorageManager = fileStorageManager;
    }

    @Override
    public PresignedUrlResult execute(GenerateUploadUrlCommand command) {
        UploadSessionRequest domainRequest = uploadSessionFactory.toUploadSessionRequest(command);
        UploadSessionResult domainResult = fileStorageManager.generateUploadUrl(domainRequest);

        return new PresignedUrlResult(
                domainResult.sessionId(),
                domainResult.presignedUrl(),
                domainResult.objectKey(),
                domainResult.expiresAt(),
                domainResult.accessUrl());
    }
}
