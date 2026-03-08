package com.ryuqq.setof.application.common.factory;

import com.ryuqq.setof.domain.common.vo.CursorPageRequest;
import com.ryuqq.setof.domain.common.vo.CursorQueryContext;
import com.ryuqq.setof.domain.common.vo.DateRange;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import com.ryuqq.setof.domain.common.vo.SortKey;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

/**
 * Common VO Factory
 *
 * <p>Domain Layer의 공통 VO 생성을 담당합니다.
 *
 * <p><strong>설계 원칙:</strong>
 *
 * <ul>
 *   <li>단순 변환만 수행 - 비즈니스 로직/검증 로직 금지
 *   <li>null 체크 금지 - VO 내부 검증 또는 API Mapper에서 처리
 *   <li>기본값 처리 금지 - API Mapper에서 처리
 *   <li>파라미터를 그대로 VO.of()에 전달
 * </ul>
 *
 * <p><strong>사용 예시:</strong>
 *
 * <pre>{@code
 * @Component
 * public class OrderQueryFactory {
 *     private final CommonVoFactory commonVoFactory;
 *
 *     public OrderSearchCriteria create(SearchOrdersQuery query) {
 *         DateRange dateRange = commonVoFactory.createDateRange(
 *             query.startDate(), query.endDate()
 *         );
 *         // ...
 *     }
 * }
 * }</pre>
 *
 * @author development-team
 * @since 1.0.0
 */
@Component
public class CommonVoFactory {

    /**
     * DateRange 생성
     *
     * <p>null 체크 금지 - 둘 다 null이면 null DateRange 생성됨
     *
     * @param startDate 시작일
     * @param endDate 종료일
     * @return DateRange
     */
    public DateRange createDateRange(LocalDate startDate, LocalDate endDate) {
        return DateRange.of(startDate, endDate);
    }

    /**
     * PageRequest 생성
     *
     * @param page 페이지 번호 (0부터 시작)
     * @param size 페이지 크기
     * @return PageRequest
     */
    public PageRequest createPageRequest(Integer page, Integer size) {
        return PageRequest.of(page, size);
    }

    /**
     * SortDirection 파싱
     *
     * @param direction 정렬 방향 문자열 ("ASC", "DESC")
     * @return SortDirection
     */
    public SortDirection parseSortDirection(String direction) {
        return SortDirection.valueOf(direction);
    }

    /**
     * QueryContext 생성
     *
     * @param sortKey 정렬 키
     * @param sortDirection 정렬 방향
     * @param pageRequest 페이징 정보
     * @param <K> 정렬 키 타입
     * @return QueryContext
     */
    public <K extends SortKey> QueryContext<K> createQueryContext(
            K sortKey, SortDirection sortDirection, PageRequest pageRequest) {
        return QueryContext.of(sortKey, sortDirection, pageRequest, false);
    }

    /**
     * QueryContext 생성 (includeDeleted 포함)
     *
     * @param sortKey 정렬 키
     * @param sortDirection 정렬 방향
     * @param pageRequest 페이징 정보
     * @param includeDeleted 삭제된 항목 포함 여부
     * @param <K> 정렬 키 타입
     * @return QueryContext
     */
    public <K extends SortKey> QueryContext<K> createQueryContext(
            K sortKey,
            SortDirection sortDirection,
            PageRequest pageRequest,
            boolean includeDeleted) {
        return QueryContext.of(sortKey, sortDirection, pageRequest, includeDeleted);
    }

    /**
     * CursorPageRequest 생성 (첫 페이지)
     *
     * <p>커서 없이 첫 페이지를 조회할 때 사용합니다.
     *
     * @param size 페이지 크기
     * @param <C> 커서 타입
     * @return CursorPageRequest (cursor=null)
     */
    public <C> CursorPageRequest<C> createCursorPageRequest(int size) {
        return CursorPageRequest.first(size);
    }

    /**
     * CursorPageRequest 생성 (커서 포함)
     *
     * <p>이전 페이지의 마지막 커서 값을 기반으로 다음 페이지를 조회할 때 사용합니다.
     *
     * @param cursor 커서 값
     * @param size 페이지 크기
     * @param <C> 커서 타입
     * @return CursorPageRequest
     */
    public <C> CursorPageRequest<C> createCursorPageRequest(C cursor, int size) {
        return CursorPageRequest.of(cursor, size);
    }

    /**
     * CursorPageRequest 생성 (Long ID 기반 afterId)
     *
     * <p>마지막 항목의 Long ID를 커서로 사용하는 페이징 방식입니다. Cart 등 ID 기반 커서 페이징에서 사용합니다.
     *
     * @param cursor 마지막 항목의 ID
     * @param size 페이지 크기
     * @return CursorPageRequest&lt;Long&gt;
     */
    public CursorPageRequest<Long> createCursorPageRequestAfterCursor(Long cursor, int size) {
        return CursorPageRequest.afterId(cursor, size);
    }

    /**
     * CursorQueryContext 생성
     *
     * @param sortKey 정렬 키
     * @param sortDirection 정렬 방향
     * @param pageRequest 커서 기반 페이징 정보
     * @param <K> 정렬 키 타입
     * @param <C> 커서 타입
     * @return CursorQueryContext
     */
    public <K extends SortKey, C> CursorQueryContext<K, C> createCursorQueryContext(
            K sortKey, SortDirection sortDirection, CursorPageRequest<C> pageRequest) {
        return CursorQueryContext.of(sortKey, sortDirection, pageRequest);
    }

    /**
     * CursorQueryContext 생성 (includeDeleted 포함)
     *
     * @param sortKey 정렬 키
     * @param sortDirection 정렬 방향
     * @param pageRequest 커서 기반 페이징 정보
     * @param includeDeleted 삭제된 항목 포함 여부
     * @param <K> 정렬 키 타입
     * @param <C> 커서 타입
     * @return CursorQueryContext
     */
    public <K extends SortKey, C> CursorQueryContext<K, C> createCursorQueryContext(
            K sortKey,
            SortDirection sortDirection,
            CursorPageRequest<C> pageRequest,
            boolean includeDeleted) {
        return CursorQueryContext.of(sortKey, sortDirection, pageRequest, includeDeleted);
    }
}
