package com.ryuqq.setof.migration.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 마이그레이션 설정 Properties
 *
 * <p>application.yml의 migration.* 설정을 바인딩합니다.
 *
 * @author development-team
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "migration")
public class MigrationProperties {

    private final BatchProperties batch;
    private final ScheduleProperties schedule;

    public MigrationProperties(BatchProperties batch, ScheduleProperties schedule) {
        this.batch = batch != null ? batch : new BatchProperties(1000, 100, 3);
        this.schedule =
                schedule != null
                        ? schedule
                        : new ScheduleProperties(false, "0 0 2 * * ?", false, 60000);
    }

    public BatchProperties batch() {
        return batch;
    }

    public ScheduleProperties schedule() {
        return schedule;
    }

    /**
     * 배치 처리 설정
     *
     * @param chunkSize Chunk 크기 (기본값: 1000)
     * @param skipLimit Skip 허용 횟수 (기본값: 100)
     * @param retryLimit 재시도 횟수 (기본값: 3)
     */
    public record BatchProperties(int chunkSize, int skipLimit, int retryLimit) {

        public BatchProperties {
            if (chunkSize <= 0) {
                chunkSize = 1000;
            }
            if (skipLimit < 0) {
                skipLimit = 100;
            }
            if (retryLimit < 0) {
                retryLimit = 3;
            }
        }
    }

    /**
     * 스케줄링 설정
     *
     * @param enabled 초기 마이그레이션 스케줄링 활성화 여부
     * @param cron 초기 마이그레이션 Cron 표현식
     * @param incrementalEnabled 증분 동기화 스케줄링 활성화 여부
     * @param checkIntervalMs 체크포인트 확인 간격 (ms, 기본 60000 = 1분)
     */
    public record ScheduleProperties(
            boolean enabled, String cron, boolean incrementalEnabled, long checkIntervalMs) {

        public ScheduleProperties {
            if (cron == null || cron.isBlank()) {
                cron = "0 0 2 * * ?";
            }
            if (checkIntervalMs <= 0) {
                checkIntervalMs = 60000; // 기본값 1분
            }
        }

        public ScheduleProperties(boolean enabled, String cron) {
            this(enabled, cron, false, 60000);
        }
    }
}
