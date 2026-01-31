package com.ryuqq.setof.application.common.dto;

/**
 * 배치 처리 결과.
 *
 * <p>스케줄러 등 배치 작업의 처리 결과를 담습니다.
 *
 * @param total 전체 처리 대상 수
 * @param success 성공 수
 * @param failed 실패 수
 */
public record BatchProcessingResult(int total, int success, int failed) {

    public static BatchProcessingResult of(int total, int success, int failed) {
        return new BatchProcessingResult(total, success, failed);
    }

    public static BatchProcessingResult empty() {
        return new BatchProcessingResult(0, 0, 0);
    }

    public boolean hasFailures() {
        return failed > 0;
    }
}
