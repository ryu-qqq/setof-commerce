package com.setof.connectly.module.qna.controller;


import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.payload.ApiResponse;
import com.setof.connectly.module.qna.dto.QnaResponse;
import com.setof.connectly.module.qna.dto.filter.QnaFilter;
import com.setof.connectly.module.qna.dto.query.CreateQna;
import com.setof.connectly.module.qna.entity.Qna;
import com.setof.connectly.module.qna.entity.QnaAnswer;
import com.setof.connectly.module.qna.service.answer.query.QnaAnswerQueryService;
import com.setof.connectly.module.qna.service.fetch.QnaFindService;
import com.setof.connectly.module.qna.service.query.QnaQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class QnaController {

    private final QnaFindService qnaFindService;
    private final QnaQueryService qnaQueryService;
    private final QnaAnswerQueryService qnaAnswerQueryService;

    @GetMapping("/qna/product/{productGroupId}")
    public ResponseEntity<ApiResponse<Page<QnaResponse>>> fetchProductQnas(@PathVariable("productGroupId") long productGroupId, Pageable pageable){
        return ResponseEntity.ok(ApiResponse.success(qnaFindService.fetchProductQuestions(productGroupId, pageable)));
    }

    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PostMapping("/qna")
    public ResponseEntity<ApiResponse<Qna>> doQuestion(@RequestBody @Validated CreateQna createQna){
        return ResponseEntity.ok(ApiResponse.success(qnaQueryService.doQuestion(createQna)));
    }

    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PutMapping("/qna/{qnaId}")
    public ResponseEntity<ApiResponse<Qna>> updateQuestion(@PathVariable("qnaId") long qnaId, @RequestBody @Validated CreateQna createQna){
        return ResponseEntity.ok(ApiResponse.success(qnaQueryService.updateQuestion(qnaId, createQna)));
    }

    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PostMapping("/qna/{qnaId}/reply")
    public ResponseEntity<ApiResponse<QnaAnswer>> doReply(@PathVariable("qnaId") long qnaId, @RequestBody @Validated CreateQna createQna){
        return ResponseEntity.ok(ApiResponse.success(qnaAnswerQueryService.replyQna(qnaId, createQna)));
    }

    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @PutMapping("/qna/{qnaId}/reply/{qnaAnswerId}")
    public ResponseEntity<ApiResponse<QnaAnswer>> updateReply(@PathVariable("qnaId") long qnaId, @PathVariable("qnaAnswerId") long qnaAnswerId, @RequestBody @Validated CreateQna createQna){
        return ResponseEntity.ok(ApiResponse.success(qnaAnswerQueryService.updateReplyQna(qnaId, qnaAnswerId, createQna)));
    }

    @PreAuthorize("hasAnyAuthority('NORMAL_GRADE')")
    @GetMapping("/qna/my-page")
    public ResponseEntity<ApiResponse<CustomSlice<QnaResponse>>> fetchMyQnas(@ModelAttribute QnaFilter qnaFilter, Pageable pageable){
        return ResponseEntity.ok(ApiResponse.success(qnaFindService.fetchMyQnas(qnaFilter, pageable)));
    }

}
