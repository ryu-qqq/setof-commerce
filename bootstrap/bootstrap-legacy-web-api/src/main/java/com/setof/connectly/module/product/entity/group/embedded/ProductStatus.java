package com.setof.connectly.module.product.entity.group.embedded;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.setof.connectly.module.common.enums.Yn;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ProductStatus {

    @NotNull(message = "soldOutYn 필수입니다")
    @Column(name = "sold_out_yn")
    @Enumerated(EnumType.STRING)
    private Yn soldOutYn;

    @NotNull(message = "displayYn 필수입니다")
    @Column(name = "display_yn")
    @Enumerated(EnumType.STRING)
    private Yn displayYn;

    @Builder
    public ProductStatus(Yn soldOutYn, Yn displayYn) {
        this.soldOutYn = soldOutYn;
        this.displayYn = displayYn;
    }

    public ProductStatus(int qty, Yn displayYn) {
        this.soldOutYn = qty > 0 ? Yn.N : Yn.Y;
        this.displayYn = displayYn;
    }

    public void soldOut() {
        this.displayYn = Yn.N;
        this.soldOutYn = Yn.Y;
    }

    public void onStock() {
        this.displayYn = Yn.Y;
        this.soldOutYn = Yn.N;
    }

    @JsonIgnore
    public boolean isSoldOut() {
        return soldOutYn.equals(Yn.Y);
    }
}
