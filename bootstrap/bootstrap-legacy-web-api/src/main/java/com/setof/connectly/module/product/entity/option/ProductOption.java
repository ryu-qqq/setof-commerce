package com.setof.connectly.module.product.entity.option;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.product.entity.group.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

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
    private Long id;

    private long additionalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_group_id")
    private OptionGroup optionGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_detail_id")
    private OptionDetail optionDetail;
}
