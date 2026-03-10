package com.ryuqq.setof.application.uploadsession.port.in.command;

import com.ryuqq.setof.application.uploadsession.dto.command.CompleteUploadSessionCommand;

/**
 * 업로드 세션 완료 UseCase.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface CompleteUploadSessionUseCase {

    void execute(CompleteUploadSessionCommand command);
}
