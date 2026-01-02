package com.ryuqq.setof.domain.board.vo;

import java.time.Instant;

/**
 * 노출 기간 Value Object
 *
 * <p>게시물의 노출 시작/종료 시간을 캡슐화한 불변 Value Object입니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>불변성 보장 - record 사용
 *   <li>검증 로직 포함
 * </ul>
 */
public record DisplayPeriod(Instant startAt, Instant endAt) {

    /**
     * 컴팩트 생성자 - 검증 로직
     *
     * @throws IllegalArgumentException 종료일이 시작일보다 이전일 때
     */
    public DisplayPeriod {
        if (startAt != null && endAt != null && endAt.isBefore(startAt)) {
            throw new IllegalArgumentException("종료일시는 시작일시 이후여야 합니다");
        }
    }

    /**
     * 기간 미설정 인스턴스 생성 (무기한)
     *
     * @return 시작/종료가 모두 null인 DisplayPeriod
     */
    public static DisplayPeriod unlimited() {
        return new DisplayPeriod(null, null);
    }

    /**
     * 시작일만 설정된 인스턴스 생성
     *
     * @param startAt 시작일시
     * @return 시작일만 설정된 DisplayPeriod
     */
    public static DisplayPeriod startingFrom(Instant startAt) {
        return new DisplayPeriod(startAt, null);
    }

    /**
     * 종료일만 설정된 인스턴스 생성
     *
     * @param endAt 종료일시
     * @return 종료일만 설정된 DisplayPeriod
     */
    public static DisplayPeriod until(Instant endAt) {
        return new DisplayPeriod(null, endAt);
    }

    /**
     * 기간이 설정되어 있는지 확인
     *
     * @return 시작 또는 종료가 설정되어 있으면 true
     */
    public boolean hasPeriod() {
        return startAt != null || endAt != null;
    }

    /**
     * 주어진 시점이 노출 기간 내인지 확인
     *
     * @param instant 확인할 시점
     * @return 노출 가능하면 true
     */
    public boolean isWithinPeriod(Instant instant) {
        if (instant == null) {
            return false;
        }
        boolean afterStart = startAt == null || !instant.isBefore(startAt);
        boolean beforeEnd = endAt == null || !instant.isAfter(endAt);
        return afterStart && beforeEnd;
    }

    /**
     * 현재 시점이 노출 기간 내인지 확인
     *
     * @return 현재 노출 가능하면 true
     */
    public boolean isCurrentlyDisplayable() {
        return isWithinPeriod(Instant.now());
    }

    /**
     * 노출이 시작되었는지 확인
     *
     * @return 시작되었으면 true
     */
    public boolean hasStarted() {
        return startAt == null || !Instant.now().isBefore(startAt);
    }

    /**
     * 노출이 종료되었는지 확인
     *
     * @return 종료되었으면 true
     */
    public boolean hasEnded() {
        return endAt != null && Instant.now().isAfter(endAt);
    }

    /**
     * 시작일 업데이트
     *
     * @param newStartAt 새로운 시작일시
     * @return 시작일이 업데이트된 DisplayPeriod
     */
    public DisplayPeriod updateStartAt(Instant newStartAt) {
        return new DisplayPeriod(newStartAt, this.endAt);
    }

    /**
     * 종료일 업데이트
     *
     * @param newEndAt 새로운 종료일시
     * @return 종료일이 업데이트된 DisplayPeriod
     */
    public DisplayPeriod updateEndAt(Instant newEndAt) {
        return new DisplayPeriod(this.startAt, newEndAt);
    }
}
