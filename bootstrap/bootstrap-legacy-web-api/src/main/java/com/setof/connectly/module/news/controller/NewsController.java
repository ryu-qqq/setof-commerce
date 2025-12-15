package com.setof.connectly.module.news.controller;

import com.setof.connectly.module.news.dto.board.BoardDto;
import com.setof.connectly.module.news.dto.faq.FaqDto;
import com.setof.connectly.module.news.dto.faq.filter.FaqFilter;
import com.setof.connectly.module.news.service.board.BoardFindService;
import com.setof.connectly.module.news.service.faq.FaqFindService;
import com.setof.connectly.module.payload.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class NewsController {
    private final FaqFindService faqFindService;
    private final BoardFindService boardFindService;

    @GetMapping("/faq")
    public ResponseEntity<ApiResponse<List<FaqDto>>> fetchFaq(
            @ModelAttribute @Validated FaqFilter filter) {
        return ResponseEntity.ok(ApiResponse.success(faqFindService.fetchFaqDto(filter)));
    }

    @GetMapping("/board")
    public ResponseEntity<ApiResponse<Page<BoardDto>>> fetchBoard(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(boardFindService.fetchBoards(pageable)));
    }
}
