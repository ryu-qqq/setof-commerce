package com.ryuqq.setof.application.board.factory;

import com.ryuqq.setof.application.board.dto.query.BoardSearchParams;
import com.ryuqq.setof.application.common.factory.CommonVoFactory;
import com.ryuqq.setof.domain.board.query.BoardSearchCriteria;
import com.ryuqq.setof.domain.board.query.BoardSortKey;
import com.ryuqq.setof.domain.common.vo.PageRequest;
import com.ryuqq.setof.domain.common.vo.QueryContext;
import com.ryuqq.setof.domain.common.vo.SortDirection;
import org.springframework.stereotype.Component;

/**
 * Board Query Factory.
 *
 * <p>Query DTO를 Domain Criteria로 변환합니다.
 *
 * <p>기본값 처리는 REST API Layer (ApiMapper)에서 수행합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class BoardQueryFactory {

    private final CommonVoFactory commonVoFactory;

    public BoardQueryFactory(CommonVoFactory commonVoFactory) {
        this.commonVoFactory = commonVoFactory;
    }

    /**
     * BoardSearchParams로부터 BoardSearchCriteria 생성.
     *
     * <p>기본값 처리 없이 단순 변환만 수행합니다.
     *
     * @param params 검색 파라미터 (기본값 적용 완료된 상태)
     * @return BoardSearchCriteria
     */
    public BoardSearchCriteria createCriteria(BoardSearchParams params) {
        BoardSortKey sortKey = resolveSortKey(params.sortKey());
        SortDirection sortDirection = commonVoFactory.parseSortDirection(params.sortDirection());
        PageRequest pageRequest = commonVoFactory.createPageRequest(params.page(), params.size());

        QueryContext<BoardSortKey> queryContext =
                commonVoFactory.createQueryContext(
                        sortKey,
                        sortDirection,
                        pageRequest,
                        params.searchParams().includeDeleted());

        return BoardSearchCriteria.of(queryContext);
    }

    private BoardSortKey resolveSortKey(String sortKeyString) {
        if (sortKeyString == null || sortKeyString.isBlank()) {
            return BoardSortKey.defaultKey();
        }

        for (BoardSortKey key : BoardSortKey.values()) {
            if (key.fieldName().equalsIgnoreCase(sortKeyString)
                    || key.name().equalsIgnoreCase(sortKeyString)) {
                return key;
            }
        }

        return BoardSortKey.defaultKey();
    }
}
