package com.connectly.partnerAdmin.module.product.entity.option;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.product.entity.stock.Product;
import com.connectly.partnerAdmin.module.product.enums.option.OptionName;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "product_option")
@Entity
public class ProductOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_option_id")
    private long id;

    @Column(name = "additional_price")
    private BigDecimal additionalPrice;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "option_group_id")
    private OptionGroup optionGroup;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "option_detail_id")
    private OptionDetail optionDetail;


    public ProductOption(OptionGroup optionGroup, OptionDetail optionDetail, BigDecimal additionalPrice) {
        this.setOptionGroup(optionGroup);
        this.setOptionDetail(optionDetail);
        this.additionalPrice =additionalPrice;
    }

    public void setProduct(Product product) {
        if (this.product != null && !this.product.equals(product)) {
            this.product.getProductOptions().remove(this);
        }
        this.product = product;
        if (product != null && !product.getProductOptions().contains(this)) {
            product.getProductOptions().add(this);
        }
    }

    public void setOptionName(OptionName optionName){
        this.optionGroup.setOptionName(optionName);
    }

    public void setOptionValue(String optionValue){
        this.optionDetail.setOptionValue(optionValue);
    }


    public void delete(){
        this.setDeleteYn(Yn.Y);
        this.optionDetail.delete();
    }

}


