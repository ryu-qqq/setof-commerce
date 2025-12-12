package com.ryuqq.setof.adapter.out.client.portone.dto;

/**
 * PortOne API Response Wrapper
 *
 * <p>포트원 API 공통 응답 형식
 *
 * @param <T> 응답 데이터 타입
 * @param code 응답 코드 (0: 성공, 음수: 실패)
 * @param message 응답 메시지
 * @param response 응답 데이터
 * @author development-team
 * @since 1.0.0
 */
public record PortOneApiResponse<T>(int code, String message, T response) {

    /**
     * 성공 여부 확인
     *
     * @return 성공 시 true
     */
    public boolean isSuccess() {
        return code == 0;
    }
}
