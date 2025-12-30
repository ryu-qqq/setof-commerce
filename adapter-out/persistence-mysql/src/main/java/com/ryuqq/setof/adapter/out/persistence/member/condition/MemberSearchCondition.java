package com.ryuqq.setof.adapter.out.persistence.member.condition;

/**
 * MemberSearchCondition - Persistence Layer 전용 검색 조건
 *
 * <p>Domain Layer의 MemberSearchCriteria를 Persistence Layer로 변환한 조건 DTO
 *
 * <p>Repository에서 Domain VO에 직접 의존하지 않도록 분리
 *
 * @param name 회원명 검색어 (nullable, 부분 일치)
 * @param phoneNumber 핸드폰 번호 검색어 (nullable, 부분 일치)
 * @param status 회원 상태 (nullable)
 * @param offset 페이징 offset
 * @param limit 페이징 limit
 * @author development-team
 * @since 1.0.0
 */
public record MemberSearchCondition(
        String name, String phoneNumber, String status, long offset, int limit) {

    /**
     * Static Factory Method
     *
     * @param name 회원명 검색어
     * @param phoneNumber 핸드폰 번호 검색어
     * @param status 회원 상태
     * @param offset 페이징 offset
     * @param limit 페이징 limit
     * @return MemberSearchCondition 인스턴스
     */
    public static MemberSearchCondition of(
            String name, String phoneNumber, String status, long offset, int limit) {
        return new MemberSearchCondition(name, phoneNumber, status, offset, limit);
    }

    /**
     * 이름 필터 존재 여부
     *
     * @return 이름 필터가 존재하면 true
     */
    public boolean hasName() {
        return name != null && !name.isBlank();
    }

    /**
     * 핸드폰 번호 필터 존재 여부
     *
     * @return 핸드폰 번호 필터가 존재하면 true
     */
    public boolean hasPhoneNumber() {
        return phoneNumber != null && !phoneNumber.isBlank();
    }

    /**
     * 상태 필터 존재 여부
     *
     * @return 상태 필터가 존재하면 true
     */
    public boolean hasStatus() {
        return status != null && !status.isBlank();
    }
}
