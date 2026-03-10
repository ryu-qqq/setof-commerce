package com.ryuqq.setof.application.uploadsession.service.command;

import com.ryuqq.setof.application.uploadsession.dto.command.CompleteUploadSessionCommand;
import com.ryuqq.setof.application.uploadsession.factory.UploadSessionFactory;
import com.ryuqq.setof.application.uploadsession.manager.FileStorageManager;
import com.ryuqq.setof.application.uploadsession.port.in.command.CompleteUploadSessionUseCase;
import com.ryuqq.setof.domain.session.vo.CompleteUploadSession;
import org.springframework.stereotype.Service;

/**
 * 업로드 세션 완료 서비스.
 *
 * <p>Command → Factory → Domain VO → Manager → Port → Adapter
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Service
public class CompleteUploadSessionService implements CompleteUploadSessionUseCase {

    private final UploadSessionFactory uploadSessionFactory;
    private final FileStorageManager fileStorageManager;

    public CompleteUploadSessionService(
            UploadSessionFactory uploadSessionFactory, FileStorageManager fileStorageManager) {
        this.uploadSessionFactory = uploadSessionFactory;
        this.fileStorageManager = fileStorageManager;
    }

    @Override
    public void execute(CompleteUploadSessionCommand command) {
        CompleteUploadSession domainVO = uploadSessionFactory.toCompleteUploadSession(command);
        fileStorageManager.completeUploadSession(domainVO);
    }
}
