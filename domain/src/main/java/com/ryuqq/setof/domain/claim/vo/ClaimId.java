package com.ryuqq.setof.domain.claim.vo;

import java.util.Objects;
import java.util.UUID;

/**
 * ClaimId - 클레임 식별자 VO
 *
 * <p>UUID 기반의 클레임 고유 식별자입니다.
 *
 * @author development-team
 * @since 2.0.0
 */
public final class ClaimId {

    private final String value;

    private ClaimId(String value) {
        this.value = value;
    }

    /**
     * 새로운 ClaimId 생성
     *
     * @return 랜덤 UUID 기반 ClaimId
     */
    public static ClaimId generate() {
        return new ClaimId(UUID.randomUUID().toString());
    }

    /**
     * 문자열로부터 ClaimId 생성
     *
     * @param value UUID 문자열
     * @return ClaimId
     * @throws IllegalArgumentException value가 null이거나 비어있으면
     */
    public static ClaimId of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ClaimId must not be null or blank");
        }
        return new ClaimId(value);
    }

    /**
     * 클레임 ID 값 반환
     *
     * @return UUID 문자열
     */
    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClaimId claimId = (ClaimId) o;
        return Objects.equals(value, claimId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
