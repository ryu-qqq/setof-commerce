package com.ryuqq.setof.domain.faq.vo;

/**
 * 상단 노출 설정 Value Object
 *
 * <p>FAQ의 상단 노출 여부와 순서를 캡슐화한 불변 Value Object입니다.
 *
 * <p>Domain Layer Zero-Tolerance 규칙:
 *
 * <ul>
 *   <li>Lombok 금지 - Pure Java 사용
 *   <li>불변성 보장 - record 사용
 *   <li>검증 로직 포함
 * </ul>
 */
public record TopSetting(boolean isTop, int topOrder) {

    private static final int MIN_ORDER = 0;

    /**
     * 컴팩트 생성자 - 검증 로직
     *
     * @throws IllegalArgumentException topOrder가 음수일 때
     */
    public TopSetting {
        if (topOrder < MIN_ORDER) {
            throw new IllegalArgumentException("상단 순서는 0 이상이어야 합니다: " + topOrder);
        }
        // 상단이 아닌 경우 순서를 0으로 초기화
        if (!isTop) {
            topOrder = 0;
        }
    }

    /**
     * 상단 미노출 기본 설정 생성
     *
     * @return 상단 미노출 TopSetting
     */
    public static TopSetting notTop() {
        return new TopSetting(false, 0);
    }

    /**
     * 상단 노출 설정 생성
     *
     * @param order 상단 순서
     * @return 상단 노출 TopSetting
     */
    public static TopSetting top(int order) {
        return new TopSetting(true, order);
    }

    /**
     * 상단 노출 처리
     *
     * @param order 상단 순서
     * @return 상단 노출된 TopSetting
     */
    public TopSetting setTop(int order) {
        return new TopSetting(true, order);
    }

    /**
     * 상단 노출 해제
     *
     * @return 상단 미노출 TopSetting
     */
    public TopSetting unsetTop() {
        return new TopSetting(false, 0);
    }

    /**
     * 순서 변경
     *
     * @param newOrder 새로운 순서
     * @return 순서가 변경된 TopSetting
     * @throws IllegalStateException 상단이 아닌 상태에서 호출 시
     */
    public TopSetting updateOrder(int newOrder) {
        if (!isTop) {
            throw new IllegalStateException("상단이 아닌 FAQ의 순서를 변경할 수 없습니다");
        }
        return new TopSetting(true, newOrder);
    }
}
