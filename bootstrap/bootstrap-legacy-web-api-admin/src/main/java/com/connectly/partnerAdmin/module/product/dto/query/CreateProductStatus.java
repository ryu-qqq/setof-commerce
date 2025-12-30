package com.connectly.partnerAdmin.module.product.dto.query;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateProductStatus {

    @NotNull(message = "Sold out status is required.")
    private Yn soldOutYn;

    @NotNull(message = "Display status is required.")
    private Yn displayYn;

    public void forSheIn(){
        this.displayYn = Yn.N;
    }

}
