package com.setof.connectly.module.search.entity;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.product.entity.group.ProductGroup;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "product_score")
@Entity
public class ProductScore extends BaseEntity {

    @Id
    @Column(name = "product_group_id")
    private long id;

    @Column(name = "score")
    private double score;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "product_group_id")
    private ProductGroup productGroup;
}
