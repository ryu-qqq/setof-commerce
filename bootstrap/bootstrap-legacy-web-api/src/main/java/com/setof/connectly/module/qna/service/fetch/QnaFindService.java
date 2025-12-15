package com.setof.connectly.module.qna.service.fetch;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.notification.dto.qna.QnaSheet;
import com.setof.connectly.module.qna.dto.QnaResponse;
import com.setof.connectly.module.qna.dto.filter.QnaFilter;
import com.setof.connectly.module.qna.entity.Qna;
import com.setof.connectly.module.qna.enums.QnaStatus;
import com.setof.connectly.module.qna.enums.QnaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QnaFindService {
    Page<QnaResponse> fetchProductQuestions(long productGroupId, Pageable pageable);
    List<QnaResponse> fetchQnas(QnaType qnaType, List<Long> qnaIds);
    Qna fetchQnaEntity(long qnaId);
    QnaStatus fetchQnaStatus(long qnaId);
    CustomSlice<QnaResponse> fetchMyQnas(QnaFilter qnaFilter, Pageable pageable);
    QnaSheet fetchQnaSheet(long qnaId, QnaType qnaType);


}
