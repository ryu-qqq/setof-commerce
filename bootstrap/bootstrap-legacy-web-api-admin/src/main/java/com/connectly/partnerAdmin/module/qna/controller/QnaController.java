package com.connectly.partnerAdmin.module.qna.controller;

import com.connectly.partnerAdmin.module.common.dto.CustomPageable;
import com.connectly.partnerAdmin.module.payload.ApiResponse;
import com.connectly.partnerAdmin.module.qna.dto.CreateQnaAnswerResponse;
import com.connectly.partnerAdmin.module.qna.dto.fetch.DetailQnaResponse;
import com.connectly.partnerAdmin.module.qna.dto.fetch.FetchQnaResponse;
import com.connectly.partnerAdmin.module.qna.dto.filter.QnaFilter;
import com.connectly.partnerAdmin.module.qna.dto.query.CreateQnaAnswer;
import com.connectly.partnerAdmin.module.qna.dto.query.UpdateQnaAnswer;
import com.connectly.partnerAdmin.module.qna.service.QnaAnswerRegistrationService;
import com.connectly.partnerAdmin.module.qna.service.QnaFetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.connectly.partnerAdmin.module.common.config.EndPointsConstants.BASE_END_POINT_V1;
import static com.connectly.partnerAdmin.module.common.config.SecurityConstants.HAS_ANY_AUTHORITY_MASTER_SELLER;

@PreAuthorize(HAS_ANY_AUTHORITY_MASTER_SELLER)
@RestController
@RequestMapping(BASE_END_POINT_V1)
@RequiredArgsConstructor
public class QnaController {

    private final QnaFetchService qnaFetchService;

    private final QnaAnswerRegistrationService qnaAnswerRegistrationService;

    @GetMapping("/qna/{qnaId}")
    public ResponseEntity<ApiResponse<DetailQnaResponse>> fetchQna(@PathVariable("qnaId") long qnaId){
        return ResponseEntity.ok(ApiResponse.success(qnaFetchService.fetchQna(qnaId)));
    }

    @GetMapping("/qnas")
    public ResponseEntity<ApiResponse<CustomPageable<FetchQnaResponse>>> fetchQnas(@Validated  @ModelAttribute QnaFilter filterDto, Pageable pageable){
        return ResponseEntity.ok(ApiResponse.success(qnaFetchService.fetchQnas(filterDto, pageable)));
    }

    @PostMapping("/qna/reply")
    public ResponseEntity<ApiResponse<CreateQnaAnswerResponse>> replyQna(@RequestBody CreateQnaAnswer createQnaAnswer){
        return ResponseEntity.ok(ApiResponse.success(qnaAnswerRegistrationService.doAnswer(createQnaAnswer)));
    }

    @PutMapping("/qna/reply")
    public ResponseEntity<ApiResponse<CreateQnaAnswerResponse>> updateReplyQna(@RequestBody UpdateQnaAnswer updateQnaAnswer){
        return ResponseEntity.ok(ApiResponse.success(qnaAnswerRegistrationService.updateAnswer(updateQnaAnswer)));
    }


}
