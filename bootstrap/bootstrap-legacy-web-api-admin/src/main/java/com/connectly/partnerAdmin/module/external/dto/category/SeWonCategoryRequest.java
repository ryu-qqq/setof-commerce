package com.connectly.partnerAdmin.module.external.dto.category;


import com.connectly.partnerAdmin.module.external.dto.SeWonRequestDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeWonCategoryRequest extends SeWonRequestDto {

    public SeWonCategoryRequest(String customerId, String apiKey) {
        super(customerId, apiKey);
    }
}
