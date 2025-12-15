package com.setof.connectly.module.news.repository.board;

import static com.setof.connectly.module.news.entity.board.QBoard.board;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.setof.connectly.module.news.dto.board.BoardDto;
import com.setof.connectly.module.news.dto.board.QBoardDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BoardFindRepositoryImpl implements BoardFindRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BoardDto> fetchBoards(Pageable pageable) {
        return queryFactory
                .select(new QBoardDto(board.title, board.contents))
                .from(board)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }

    @Override
    public JPAQuery<Long> fetchBoardCounts() {
        return queryFactory.select(board.count()).from(board);
    }
}
