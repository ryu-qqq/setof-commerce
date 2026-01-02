package com.setof.connectly.module.review.entity;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.product.enums.image.ProductGroupImageType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "review_image")
@Entity
public class ReviewImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_image_id")
    private long id;

    @Column(name = "review_id")
    private long reviewId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "review_image_type")
    private ProductGroupImageType reviewImageType;

    @Column(name = "image_url")
    private String imageUrl;
}
