package com.setof.connectly.module.news.service.board;

import com.querydsl.jpa.impl.JPAQuery;
import com.setof.connectly.module.news.dto.board.BoardDto;
import com.setof.connectly.module.news.repository.board.BoardFindRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardFindServiceImpl implements BoardFindService {

    private final BoardFindRepository boardFindRepository;

    @Cacheable(cacheNames = "board", key = "#pageable.getPageSize()")
    @Override
    public Page<BoardDto> fetchBoards(Pageable pageable) {
        List<BoardDto> boards = boardFindRepository.fetchBoards(pageable);
        JPAQuery<Long> longJPAQuery = boardFindRepository.fetchBoardCounts();
        return PageableExecutionUtils.getPage(boards, pageable, longJPAQuery::fetchCount);
    }
}
