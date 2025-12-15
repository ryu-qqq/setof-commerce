package com.setof.connectly.module.qna.mapper;


import com.setof.connectly.module.common.dto.CustomSlice;
import com.setof.connectly.module.common.mapper.AbstractGeneralSliceMapper;
import com.setof.connectly.module.qna.dto.QnaResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QnaSliceMapperImpl extends AbstractGeneralSliceMapper<QnaResponse> implements QnaSliceMapper{

    public CustomSlice<QnaResponse> toSlice(List<QnaResponse> qnaResponses, Pageable pageable){
        return super.toSlice(qnaResponses, pageable);
    }

}
