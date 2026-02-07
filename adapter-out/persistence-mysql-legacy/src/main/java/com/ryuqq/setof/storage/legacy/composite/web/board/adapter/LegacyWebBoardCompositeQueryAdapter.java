package com.ryuqq.setof.storage.legacy.composite.web.board.adapter;

import com.ryuqq.setof.application.legacy.board.dto.response.LegacyBoardPageResult;
import com.ryuqq.setof.domain.legacy.board.dto.query.LegacyBoardSearchCondition;
import com.ryuqq.setof.storage.legacy.composite.web.board.dto.LegacyWebBoardQueryDto;
import com.ryuqq.setof.storage.legacy.composite.web.board.mapper.LegacyWebBoardMapper;
import com.ryuqq.setof.storage.legacy.composite.web.board.repository.LegacyWebBoardCompositeQueryDslRepository;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * LegacyWebBoardCompositeQueryAdapter - 레거시 게시판 Composite 조회 Adapter.
 *
 * <p>Persistence Layer의 진입점입니다.
 *
 * <p>TODO: Application Layer의 LegacyBoardCompositeQueryPort implements 추가.
 *
 * <p>PER-ADP-001: Adapter는 @Component로 등록.
 *
 * <p>PER-ADP-002: Repository와 Mapper 조합으로 결과 반환.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class LegacyWebBoardCompositeQueryAdapter {

    private final LegacyWebBoardCompositeQueryDslRepository repository;
    private final LegacyWebBoardMapper mapper;

    public LegacyWebBoardCompositeQueryAdapter(
            LegacyWebBoardCompositeQueryDslRepository repository, LegacyWebBoardMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * 게시판 페이지 조회.
     *
     * @param condition 검색 조건
     * @return 페이지 결과
     */
    public LegacyBoardPageResult fetchBoards(LegacyBoardSearchCondition condition) {
        List<LegacyWebBoardQueryDto> dtos = repository.fetchBoards(condition);
        long totalCount = repository.countBoards();
        return mapper.toPageResult(dtos, totalCount, condition.pageNumber(), condition.pageSize());
    }

    /**
     * 게시글 전체 개수 조회.
     *
     * @return 전체 게시글 개수
     */
    public long countBoards() {
        return repository.countBoards();
    }
}
