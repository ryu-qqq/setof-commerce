package com.connectly.partnerAdmin.module.discount.dto.query;

import com.connectly.partnerAdmin.module.discount.enums.IssueType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class CreateDiscountTarget {

    @NotNull(message = "issueType은 필수입니다.")
    private IssueType issueType;
    @Size(min = 1, message = "적어도 하나 이상의 targetId가 필요합니다.")
    private List<Long> targetIds;

}
