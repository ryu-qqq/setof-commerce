package com.ryuqq.setof.application.common.port.out;

import com.ryuqq.setof.domain.session.vo.CompleteUploadSession;
import com.ryuqq.setof.domain.session.vo.UploadSessionRequest;
import com.ryuqq.setof.domain.session.vo.UploadSessionResult;

/**
 * 업로드 세션 아웃바운드 포트.
 *
 * <p>Presigned URL 발급 및 업로드 세션 완료 처리를 담당합니다.
 *
 * <p><strong>구현체:</strong>
 *
 * <ul>
 *   <li>FileflowUploadSessionAdapter - FileFlow SDK 기반
 * </ul>
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public interface UploadSessionPort {

    /**
     * 업로드용 프리사인드 URL을 발급합니다.
     *
     * @param request 업로드 세션 요청 (도메인 VO)
     * @return 업로드 세션 결과 (도메인 VO)
     */
    UploadSessionResult generateUploadUrl(UploadSessionRequest request);

    /**
     * 업로드 세션 완료 처리.
     *
     * <p>클라이언트가 Presigned URL로 S3에 업로드 완료 후 호출합니다.
     *
     * @param completeUploadSession 업로드 세션 완료 정보 (도메인 VO)
     */
    void completeUploadSession(CompleteUploadSession completeUploadSession);
}
