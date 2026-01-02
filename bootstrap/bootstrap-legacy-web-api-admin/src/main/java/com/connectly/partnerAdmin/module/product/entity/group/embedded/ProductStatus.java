package com.connectly.partnerAdmin.module.product.entity.group.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Embeddable
public class ProductStatus {

    @Column(name = "SOLD_OUT_YN", length = 1, nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn soldOutYn;

    @Setter
    @Column(name = "DISPLAY_YN", length = 1, nullable = false)
    @Enumerated(EnumType.STRING)
    private Yn displayYn;

    public ProductStatus(int qty) {
        this.soldOutYn = qty > 0 ? Yn.N: Yn.Y;
        this.displayYn = qty > 0 ? Yn.Y: Yn.N;
    }

    public void soldOut(){
        this.soldOutYn = Yn.Y;
        this.displayYn = Yn.N;
    }

    public void onStock(){
        this.soldOutYn = Yn.N;
        this.displayYn = Yn.Y;
    }

    @JsonIgnore
    public boolean isSoldOut(){
        return soldOutYn.isYes();
    }

}
