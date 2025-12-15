package com.setof.connectly.module.qna.service.image.fetch;

import com.setof.connectly.module.qna.entity.QnaImage;
import com.setof.connectly.module.qna.enums.QnaIssueType;

import java.util.List;

public interface QnaImageFindService {

    List<QnaImage> fetchQnaImageEntities(QnaIssueType qnaIssueType, long qnaAnswerId);

    List<QnaImage> fetchQnaImageEntitiesByQnaId(long qnaId);
    List<QnaImage> fetchQnaImageEntitiesByQnaId(List<Long> qnaIds);

}
