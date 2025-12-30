package com.connectly.partnerAdmin.module.external.dto.product.buyma;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BuymaProductFailDto extends BuymaProductDto {

    private BuymaError errors;


    @Override
    public String toString() {
        return super.toString() + String.format("errors: %s", errors);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BuymaError {
        private List<String> comments;
        private List<String> variants;
        private List<String> name;

        @Override
        public String toString() {
            return "errors{comments=" + comments + "name=" + name + '}';
        }
    }
}
