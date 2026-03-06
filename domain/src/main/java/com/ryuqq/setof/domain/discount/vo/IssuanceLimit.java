package com.ryuqq.setof.domain.discount.vo;

/**
 * 쿠폰 발급 제한 Value Object.
 *
 * @param totalCount 총 발급 가능 수량 (1 이상)
 * @param perUserCount 인당 발급 가능 수량 (1 이상)
 */
public record IssuanceLimit(int totalCount, int perUserCount) {

    public IssuanceLimit {
        if (totalCount < 1) {
            throw new IllegalArgumentException("총 발급 수량은 1 이상이어야 합니다: " + totalCount);
        }
        if (perUserCount < 1) {
            throw new IllegalArgumentException("인당 발급 수량은 1 이상이어야 합니다: " + perUserCount);
        }
        if (perUserCount > totalCount) {
            throw new IllegalArgumentException("인당 발급 수량은 총 발급 수량을 초과할 수 없습니다");
        }
    }

    public static IssuanceLimit of(int totalCount, int perUserCount) {
        return new IssuanceLimit(totalCount, perUserCount);
    }

    /** 총 수량 기준으로 발급 가능한지 확인 */
    public boolean canIssue(int currentIssuedCount) {
        return currentIssuedCount < totalCount;
    }

    /** 인당 수량 기준으로 발급 가능한지 확인 */
    public boolean canIssueToUser(int userIssuedCount) {
        return userIssuedCount < perUserCount;
    }
}
