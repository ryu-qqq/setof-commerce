package com.setof.connectly.module.qna.mapper;

import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.qna.dto.QnaResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QnaSliceMapper {

    CustomSlice<QnaResponse> toSlice(List<QnaResponse> qnaResponses, Pageable pageable);
}
