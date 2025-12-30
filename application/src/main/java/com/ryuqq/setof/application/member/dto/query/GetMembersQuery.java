package com.ryuqq.setof.application.member.dto.query;

/**
 * 회원 목록 조회 Query DTO
 *
 * <p>회원 목록 조회 시 사용되는 검색 조건입니다.
 *
 * @param name 회원명 검색 키워드 (부분 일치)
 * @param phoneNumber 핸드폰 번호 검색 (부분 일치)
 * @param status 회원 상태 (ACTIVE, INACTIVE, WITHDRAWN, null=전체)
 * @param page 페이지 번호 (0부터 시작)
 * @param size 페이지 크기
 * @author development-team
 * @since 1.0.0
 */
public record GetMembersQuery(String name, String phoneNumber, String status, int page, int size) {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 100;

    /** Compact Constructor - 검증 및 기본값 설정 */
    public GetMembersQuery {
        page = Math.max(page, DEFAULT_PAGE);
        size = size <= 0 ? DEFAULT_SIZE : Math.min(size, MAX_SIZE);
    }

    /**
     * Static Factory Method - 전체 조건
     *
     * @param name 회원명 검색 키워드
     * @param phoneNumber 핸드폰 번호
     * @param status 회원 상태
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return GetMembersQuery 인스턴스
     */
    public static GetMembersQuery of(
            String name, String phoneNumber, String status, int page, int size) {
        return new GetMembersQuery(name, phoneNumber, status, page, size);
    }

    /**
     * Static Factory Method - 기본 조회 (페이징만)
     *
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return GetMembersQuery 인스턴스
     */
    public static GetMembersQuery ofPage(int page, int size) {
        return new GetMembersQuery(null, null, null, page, size);
    }

    /**
     * 이름 검색 조건 존재 여부
     *
     * @return 이름 조건이 존재하면 true
     */
    public boolean hasName() {
        return name != null && !name.isBlank();
    }

    /**
     * 핸드폰 번호 검색 조건 존재 여부
     *
     * @return 핸드폰 번호 조건이 존재하면 true
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

    /**
     * offset 계산
     *
     * @return offset 값
     */
    public long offset() {
        return (long) page * size;
    }
}
