package com.setof.connectly.module.qna.mapper;

import com.setof.connectly.module.qna.dto.QnaResponse;
import com.setof.connectly.module.qna.dto.image.CreateQnaImage;
import com.setof.connectly.module.qna.dto.query.CreateQna;
import com.setof.connectly.module.qna.entity.Qna;
import com.setof.connectly.module.qna.entity.QnaAnswer;
import com.setof.connectly.module.qna.entity.QnaImage;
import com.setof.connectly.module.qna.enums.QnaIssueType;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface QnaMapper {

    List<QnaResponse> toQnaList(List<QnaResponse> qnaResponses);
    List<QnaResponse> toQnaResponse(List<QnaResponse> qnaResponses, List<QnaImage> qnaImages);
    Qna toEntity(CreateQna createQna);

    QnaAnswer toQnaAnswerEntity(long qnaId, CreateQna createQna, Optional<Long> lastQnaAnswerId);

    QnaImage toQnaImageEntity(long qnaId, long qnaAnswerId, CreateQnaImage createQnaImage, QnaIssueType qnaIssueType);
    CompletableFuture<List<QnaImage>> toQnaImageEntities(long qnaId, Long qnaAnswerId, List<CreateQnaImage> images, QnaIssueType qnaIssueType);
}
