package com.connectly.partnerAdmin.infra.config.fileflow;

import com.connectly.partnerAdmin.infra.config.fileflow.dto.CompleteSingleUploadRequest;
import com.connectly.partnerAdmin.infra.config.fileflow.dto.CompleteSingleUploadResponse;
import com.connectly.partnerAdmin.infra.config.fileflow.dto.InitSingleUploadRequest;
import com.connectly.partnerAdmin.infra.config.fileflow.dto.InitSingleUploadResponse;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Fileflow 서비스 클라이언트.
 *
 * <p>Fileflow 서비스를 통해 Presigned URL을 발급받고 업로드를 관리합니다.
 * 내부 서비스 간 통신에는 Service Token 인증을 사용합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
public class FileflowClient {

    private static final Logger log = LoggerFactory.getLogger(FileflowClient.class);

    private static final String INIT_SINGLE_UPLOAD_PATH = "/api/v1/file/upload-sessions/single";
    private static final String COMPLETE_SINGLE_UPLOAD_PATH =
            "/api/v1/file/upload-sessions/{sessionId}/single/complete";

    private final WebClient webClient;

    public FileflowClient(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * 단일 업로드 세션 초기화.
     *
     * <p>Fileflow 서비스에 업로드 세션을 생성하고 Presigned URL을 발급받습니다.
     *
     * @param fileName 파일명 (확장자 포함)
     * @param fileSize 파일 크기 (bytes)
     * @param contentType Content-Type (MIME 타입)
     * @param uploadCategory 업로드 카테고리 (예: PRODUCT, REVIEW, QNA)
     * @return InitSingleUploadResponse Presigned URL 및 세션 정보
     * @throws FileflowClientException Fileflow 서비스 호출 실패 시
     */
    public InitSingleUploadResponse initSingleUpload(
            String fileName, long fileSize, String contentType, String uploadCategory) {

        String idempotencyKey = UUID.randomUUID().toString();

        InitSingleUploadRequest request =
                InitSingleUploadRequest.builder()
                        .idempotencyKey(idempotencyKey)
                        .fileName(fileName)
                        .fileSize(fileSize)
                        .contentType(contentType)
                        .uploadCategory(uploadCategory)
                        .build();

        return initSingleUpload(request);
    }

    /**
     * 단일 업로드 세션 초기화 (상세 요청).
     *
     * @param request InitSingleUploadRequest
     * @return InitSingleUploadResponse
     * @throws FileflowClientException Fileflow 서비스 호출 실패 시
     */
    public InitSingleUploadResponse initSingleUpload(InitSingleUploadRequest request) {
        log.debug(
                "Fileflow initSingleUpload 요청: fileName={}, category={}",
                request.fileName(),
                request.uploadCategory());

        try {
            InitSingleUploadResponse response =
                    webClient
                            .post()
                            .uri(INIT_SINGLE_UPLOAD_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(request)
                            .retrieve()
                            .onStatus(
                                    HttpStatusCode::isError,
                                    clientResponse ->
                                            clientResponse
                                                    .bodyToMono(String.class)
                                                    .flatMap(
                                                            body ->
                                                                    Mono.error(
                                                                            new FileflowClientException(
                                                                                    "Fileflow initSingleUpload 실패: status="
                                                                                            + clientResponse.statusCode()
                                                                                            + ", body="
                                                                                            + body))))
                            .bodyToMono(InitSingleUploadResponse.class)
                            .block();

            if (response != null) {
                log.debug("Fileflow initSingleUpload 성공: sessionId={}", response.sessionId());
            }
            return response;

        } catch (FileflowClientException e) {
            throw e;
        } catch (Exception e) {
            log.error("Fileflow initSingleUpload 호출 실패: {}", e.getMessage(), e);
            throw new FileflowClientException("Fileflow 서비스 호출 실패", e);
        }
    }

    /**
     * 단일 업로드 완료 처리.
     *
     * <p>클라이언트가 Presigned URL로 업로드를 완료한 후 호출합니다.
     *
     * @param sessionId 세션 ID
     * @param etag S3 업로드 후 반환된 ETag
     * @return CompleteSingleUploadResponse 완료 결과
     * @throws FileflowClientException Fileflow 서비스 호출 실패 시
     */
    public CompleteSingleUploadResponse completeSingleUpload(String sessionId, String etag) {
        CompleteSingleUploadRequest request = CompleteSingleUploadRequest.of(etag);
        return completeSingleUpload(sessionId, request);
    }

    /**
     * 단일 업로드 완료 처리.
     *
     * @param sessionId 세션 ID
     * @param request CompleteSingleUploadRequest
     * @return CompleteSingleUploadResponse
     * @throws FileflowClientException Fileflow 서비스 호출 실패 시
     */
    public CompleteSingleUploadResponse completeSingleUpload(
            String sessionId, CompleteSingleUploadRequest request) {

        log.debug("Fileflow completeSingleUpload 요청: sessionId={}", sessionId);

        try {
            CompleteSingleUploadResponse response =
                    webClient
                            .patch()
                            .uri(COMPLETE_SINGLE_UPLOAD_PATH, sessionId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(request)
                            .retrieve()
                            .onStatus(
                                    HttpStatusCode::isError,
                                    clientResponse ->
                                            clientResponse
                                                    .bodyToMono(String.class)
                                                    .flatMap(
                                                            body ->
                                                                    Mono.error(
                                                                            new FileflowClientException(
                                                                                    "Fileflow completeSingleUpload 실패: status="
                                                                                            + clientResponse.statusCode()
                                                                                            + ", body="
                                                                                            + body))))
                            .bodyToMono(CompleteSingleUploadResponse.class)
                            .block();

            if (response != null) {
                log.debug(
                        "Fileflow completeSingleUpload 성공: sessionId={}, status={}",
                        sessionId,
                        response.status());
            }
            return response;

        } catch (FileflowClientException e) {
            throw e;
        } catch (Exception e) {
            log.error(
                    "Fileflow completeSingleUpload 호출 실패: sessionId={}, error={}",
                    sessionId,
                    e.getMessage(),
                    e);
            throw new FileflowClientException("Fileflow 서비스 호출 실패", e);
        }
    }
}
