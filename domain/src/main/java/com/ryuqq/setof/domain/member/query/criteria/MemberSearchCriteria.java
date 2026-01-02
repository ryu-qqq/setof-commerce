package com.ryuqq.setof.domain.member.query.criteria;

import com.ryuqq.setof.domain.common.vo.PageRequest;

/**
 * MemberSearchCriteria - 회원 검색 조건 Domain VO
 *
 * <p>회원 목록 조회 시 사용되는 검색 조건입니다.
 *
 * <p><strong>사용 위치:</strong>
 *
 * <ul>
 *   <li>Domain Layer에서 정의
 *   <li>Persistence Layer Repository에서 사용
 *   <li>Application Layer에서 생성 (Factory/Service)
 * </ul>
 *
 * <p><strong>의존성 방향:</strong>
 *
 * <pre>{@code
 * Application Layer → Domain Layer ← Persistence Layer
 *                      (Criteria)
 * }</pre>
 *
 * @param name 회원명 검색 키워드 (부분 일치)
 * @param phoneNumber 핸드폰 번호 검색 (부분 일치)
 * @param status 회원 상태 (ACTIVE, INACTIVE, WITHDRAWN, null=전체)
 * @param pageRequest 페이징 정보
 * @author development-team
 * @since 1.0.0
 */
public record MemberSearchCriteria(
        String name, String phoneNumber, String status, PageRequest pageRequest) {

    /**
     * Static Factory Method - 전체 조건
     *
     * @param name 회원명 검색 키워드
     * @param phoneNumber 핸드폰 번호
     * @param status 회원 상태
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return MemberSearchCriteria 인스턴스
     */
    public static MemberSearchCriteria of(
            String name, String phoneNumber, String status, int page, int size) {
        return new MemberSearchCriteria(name, phoneNumber, status, PageRequest.of(page, size));
    }

    /**
     * Static Factory Method - 페이징만
     *
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return MemberSearchCriteria 인스턴스
     */
    public static MemberSearchCriteria ofPage(int page, int size) {
        return new MemberSearchCriteria(null, null, null, PageRequest.of(page, size));
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
     * offset 계산 (PageRequest 위임)
     *
     * @return offset 값
     */
    public long offset() {
        return pageRequest.offset();
    }

    /**
     * 페이지 크기 반환 (PageRequest 위임)
     *
     * @return 페이지 크기
     */
    public int size() {
        return pageRequest.size();
    }

    /**
     * 페이지 번호 반환 (PageRequest 위임)
     *
     * @return 페이지 번호
     */
    public int page() {
        return pageRequest.page();
    }
}
