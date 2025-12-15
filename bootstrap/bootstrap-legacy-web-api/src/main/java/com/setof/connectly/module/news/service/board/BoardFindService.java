package com.setof.connectly.module.news.service.board;

import com.setof.connectly.module.news.dto.board.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardFindService {

    Page<BoardDto> fetchBoards(Pageable pageable);
}
