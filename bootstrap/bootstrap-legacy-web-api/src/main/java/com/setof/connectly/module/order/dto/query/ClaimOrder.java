package com.setof.connectly.module.order.dto.query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonTypeName("claimOrder")
public class ClaimOrder extends AbstractUpdateOrder {

    @Length(max = 200, message = "사유는 200자를 넘어갈 수 없습니다.")
    private String changeReason;

    @Length(max = 500, message = "사유는 500자를 넘어갈 수 없습니다.")
    private String changeDetailReason;

    public String getChangeReason() {
        if (StringUtils.hasText(changeReason)) return changeReason;
        return "";
    }

    public String getChangeDetailReason() {
        if (StringUtils.hasText(changeDetailReason)) return changeDetailReason;
        return "";
    }

    public String getReason() {
        return getChangeReason() + " " + getChangeDetailReason();
    }
}
