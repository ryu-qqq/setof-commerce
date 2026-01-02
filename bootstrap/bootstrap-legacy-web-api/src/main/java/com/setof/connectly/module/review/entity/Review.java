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
    @Column(name = "review_id")
    private Long id;

    @Column(name = "product_group_id")
    private long productGroupId;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "order_id")
    private long orderId;

    @Column(name = "rating")
    private double rating;

    @Column(name = "content")
    private String content;

    public void delete() {
        this.setDeleteYn(Yn.Y);
    }
}
