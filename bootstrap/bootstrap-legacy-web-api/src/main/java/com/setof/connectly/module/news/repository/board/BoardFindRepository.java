package com.setof.connectly.module.news.repository.board;

import com.querydsl.jpa.impl.JPAQuery;
import com.setof.connectly.module.news.dto.board.BoardDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BoardFindRepository {

    List<BoardDto> fetchBoards(Pageable pageable);

    JPAQuery<Long> fetchBoardCounts();
}
