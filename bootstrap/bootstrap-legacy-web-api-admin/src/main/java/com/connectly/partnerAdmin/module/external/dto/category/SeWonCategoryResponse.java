package com.connectly.partnerAdmin.module.external.dto.category;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SeWonCategoryResponse {

    private String id;
    @JsonProperty("code_l")
    private String codeL;
    @JsonProperty("code_m")
    private String codeM;
    @JsonProperty("code_s")
    private String codeS;
    @JsonProperty("code_d")
    private String codeD;
    @JsonProperty("name_l")
    private String nameL;
    @JsonProperty("name_m")
    private String nameM;
    @JsonProperty("name_s")
    private String nameS;
    @JsonProperty("name_d")
    private String maneD;


}
