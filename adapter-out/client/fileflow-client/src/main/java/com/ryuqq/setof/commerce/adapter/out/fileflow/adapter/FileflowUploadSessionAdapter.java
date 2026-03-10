package com.ryuqq.setof.commerce.adapter.out.fileflow.adapter;

import com.ryuqq.fileflow.sdk.api.SingleUploadSessionApi;
import com.ryuqq.fileflow.sdk.exception.FileFlowBadRequestException;
import com.ryuqq.fileflow.sdk.exception.FileFlowException;
import com.ryuqq.fileflow.sdk.model.common.ApiResponse;
import com.ryuqq.fileflow.sdk.model.session.SingleUploadSessionResponse;
import com.ryuqq.setof.application.common.port.out.UploadSessionPort;
import com.ryuqq.setof.commerce.adapter.out.fileflow.mapper.FileflowUploadSessionMapper;
import com.ryuqq.setof.domain.session.vo.CompleteUploadSession;
import com.ryuqq.setof.domain.session.vo.UploadSessionRequest;
import com.ryuqq.setof.domain.session.vo.UploadSessionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * FileFlow 업로드 세션 어댑터.
 *
 * <p>SingleUploadSessionApi를 사용하여 Presigned URL 발급 및 업로드 완료를 처리합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
@ConditionalOnProperty(prefix = "fileflow", name = "base-url")
public class FileflowUploadSessionAdapter implements UploadSessionPort {

    private static final Logger log = LoggerFactory.getLogger(FileflowUploadSessionAdapter.class);

    private final SingleUploadSessionApi singleUploadSessionApi;
    private final FileflowUploadSessionMapper mapper;

    public FileflowUploadSessionAdapter(
            SingleUploadSessionApi singleUploadSessionApi, FileflowUploadSessionMapper mapper) {
        this.singleUploadSessionApi = singleUploadSessionApi;
        this.mapper = mapper;
    }

    @Override
    public UploadSessionResult generateUploadUrl(UploadSessionRequest request) {
        log.debug(
                "FileFlow 업로드 URL 발급 요청: directory={}, filename={}",
                request.directory().path(),
                request.filename());

        try {
            ApiResponse<SingleUploadSessionResponse> response =
                    singleUploadSessionApi.create(mapper.toSdkCreateRequest(request));
            SingleUploadSessionResponse session = response.data();

            log.info(
                    "FileFlow 업로드 URL 발급 성공: sessionId={}, s3Key={}",
                    session.sessionId(),
                    session.s3Key());

            return mapper.toDomainResult(session);
        } catch (FileFlowBadRequestException e) {
            throw new IllegalArgumentException(
                    "FileFlow upload URL 생성 실패 (잘못된 요청): " + e.getErrorMessage(), e);
        } catch (FileFlowException e) {
            throw new RuntimeException("FileFlow upload URL 생성 실패: " + e.getErrorMessage(), e);
        }
    }

    @Override
    public void completeUploadSession(CompleteUploadSession completeUploadSession) {
        log.debug("FileFlow 업로드 세션 완료 요청: sessionId={}", completeUploadSession.sessionId());

        try {
            singleUploadSessionApi.complete(
                    completeUploadSession.sessionId(),
                    mapper.toSdkCompleteRequest(completeUploadSession));

            log.info("FileFlow 업로드 세션 완료 성공: sessionId={}", completeUploadSession.sessionId());
        } catch (FileFlowBadRequestException e) {
            throw new IllegalArgumentException(
                    "FileFlow 업로드 세션 완료 실패 (잘못된 요청): " + e.getErrorMessage(), e);
        } catch (FileFlowException e) {
            throw new RuntimeException("FileFlow 업로드 세션 완료 실패: " + e.getErrorMessage(), e);
        }
    }
}
