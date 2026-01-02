package com.setof.connectly.module.review.entity;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.common.enums.Yn;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "review")
@Entity
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVIEW_ID")
    private long id;

    @Column(name = "PRODUCT_GROUP_ID")
    private long productGroupId;

    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "ORDER_ID")
    private long orderId;

    @Column(name = "RATING")
    private double rating;

    @Column(name = "CONTENT")
    private String content;

    public void delete() {
        this.setDeleteYn(Yn.Y);
    }
}
