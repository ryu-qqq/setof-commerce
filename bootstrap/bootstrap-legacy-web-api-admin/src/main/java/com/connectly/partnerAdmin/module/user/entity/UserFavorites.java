package com.connectly.partnerAdmin.module.user.entity;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.product.entity.group.ProductGroup;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user_favorites")
@Entity
public class UserFavorites extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_GROUP_ID", nullable = false)
    private ProductGroup productGroup;

}
