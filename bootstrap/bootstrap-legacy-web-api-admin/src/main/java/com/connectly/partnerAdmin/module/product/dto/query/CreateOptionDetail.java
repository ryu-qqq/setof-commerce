package com.connectly.partnerAdmin.module.product.dto.query;

import com.connectly.partnerAdmin.module.product.enums.option.OptionName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOptionDetail {

    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long optionGroupId;

    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long optionDetailId;

    private OptionName optionName;

    @Length(max = 100, message = "Option Value cannot exceed 100 characters.")
    private String optionValue;

    @JsonIgnore
    public String getFullOptionName(){
        return optionName.getOptionName() +" "+optionValue;
    }

}
