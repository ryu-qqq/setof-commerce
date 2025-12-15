package com.setof.connectly.module.qna.dto.filter;


import com.setof.connectly.module.common.filter.SearchAndDateFilter;
import com.setof.connectly.module.qna.enums.QnaType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class QnaFilter extends SearchAndDateFilter {
    private Long lastDomainId;
    private QnaType qnaType;

    public boolean isProductQna(){
        return qnaType.isProductQna();
    }
}
