package com.connectly.partnerAdmin.module.qna.dto.filter;

import com.connectly.partnerAdmin.auth.validator.AuthorityValidate;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.common.filter.RoleFilter;
import com.connectly.partnerAdmin.module.common.filter.SearchAndDateFilter;
import com.connectly.partnerAdmin.module.qna.enums.QnaDetailType;
import com.connectly.partnerAdmin.module.qna.enums.QnaStatus;
import com.connectly.partnerAdmin.module.qna.enums.QnaType;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AuthorityValidate
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class QnaFilter extends SearchAndDateFilter implements RoleFilter {

    private QnaStatus qnaStatus;

    @NotNull(message = "QnaType is required.")
    private QnaType qnaType;

    private QnaDetailType qnaDetailType;

    private Yn privateYn;

    private Long lastDomainId;

    private Long sellerId;

    public boolean isProductQna(){
        return this.qnaType.isProductQna();
    }


    @Override
    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }
}
