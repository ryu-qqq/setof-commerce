package com.setof.connectly.module.user.entity;

import com.setof.connectly.module.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user_favorite")
@Entity
public class UserFavorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long id;

    private long userId;
    private long productGroupId;

    public UserFavorite(long userId, long productGroupId) {
        this.userId = userId;
        this.productGroupId = productGroupId;
    }
}
