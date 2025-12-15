package com.setof.connectly.module.qna.service.image.query;

import com.setof.connectly.module.qna.dto.image.CreateQnaImage;
import com.setof.connectly.module.qna.enums.QnaIssueType;

import java.util.List;

public interface QnaImageQueryService {

    void updateQnaImages(long qnaId, Long qnaAnswerId, List<CreateQnaImage> images, QnaIssueType qnaIssueType);
    void saveQnaImages(long qnaId, Long qnaAnswerId, List<CreateQnaImage> images, QnaIssueType qnaIssueType);
}
