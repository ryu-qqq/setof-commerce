package com.ryuqq.setof.domain.board.vo;

/**
 * 상단 고정 설정 Value Object
 *
 * <p>게시물의 상단 고정 여부와 순서를 캡슐화한 불변 Value Object입니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>불변성 보장 - record 사용
 *   <li>검증 로직 포함
 * </ul>
 */
public record PinSetting(boolean isPinned, int pinOrder) {

    private static final int MIN_ORDER = 0;

    /**
     * 컴팩트 생성자 - 검증 로직
     *
     * @throws IllegalArgumentException pinOrder가 음수일 때
     */
    public PinSetting {
        if (pinOrder < MIN_ORDER) {
            throw new IllegalArgumentException("고정 순서는 0 이상이어야 합니다: " + pinOrder);
        }
        // 고정되지 않은 경우 순서를 0으로 초기화
        if (!isPinned) {
            pinOrder = 0;
        }
    }

    /**
     * 고정되지 않은 기본 설정 생성
     *
     * @return 고정되지 않은 PinSetting
     */
    public static PinSetting unpinned() {
        return new PinSetting(false, 0);
    }

    /**
     * 고정 설정 생성
     *
     * @param order 고정 순서
     * @return 고정된 PinSetting
     */
    public static PinSetting pinned(int order) {
        return new PinSetting(true, order);
    }

    /**
     * 고정 처리
     *
     * @param order 고정 순서
     * @return 고정된 PinSetting
     */
    public PinSetting pin(int order) {
        return new PinSetting(true, order);
    }

    /**
     * 고정 해제
     *
     * @return 고정 해제된 PinSetting
     */
    public PinSetting unpin() {
        return new PinSetting(false, 0);
    }

    /**
     * 순서 변경
     *
     * @param newOrder 새로운 순서
     * @return 순서가 변경된 PinSetting
     * @throws IllegalStateException 고정되지 않은 상태에서 호출 시
     */
    public PinSetting updateOrder(int newOrder) {
        if (!isPinned) {
            throw new IllegalStateException("고정되지 않은 게시물의 순서를 변경할 수 없습니다");
        }
        return new PinSetting(true, newOrder);
    }
}
