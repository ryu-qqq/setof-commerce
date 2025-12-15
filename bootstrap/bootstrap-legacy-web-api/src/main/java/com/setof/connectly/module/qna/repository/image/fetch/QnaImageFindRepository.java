package com.setof.connectly.module.qna.repository.image.fetch;

import com.setof.connectly.module.qna.entity.QnaImage;
import com.setof.connectly.module.qna.enums.QnaIssueType;

import java.util.List;

public interface QnaImageFindRepository {

    List<QnaImage> fetchQnaImageEntities(QnaIssueType qnaIssueType, long qnaAnswerId);

    List<QnaImage> fetchQnaImageEntitiesByQnaId(long qnaId);
    List<QnaImage> fetchQnaImageEntitiesByQnaId(List<Long> qnaIds);

}
