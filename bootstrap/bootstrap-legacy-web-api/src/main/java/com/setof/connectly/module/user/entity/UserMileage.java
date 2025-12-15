package com.setof.connectly.module.user.entity;

import com.setof.connectly.module.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
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
@Table(name = "USER_MILEAGE")
@Entity
public class UserMileage extends BaseEntity {

    @Id
    @Column(name = "USER_ID")
    private long id;

    @Column(name = "CURRENT_MILEAGE")
    private double currentMileage;

    public UserMileage(double currentMileage) {
        this.currentMileage = currentMileage;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "USER_ID")
    private Users users;

    public void setUsers(Users users) {
        this.users = users;
    }

    public void setCurrentMileage(double currentMileage) {
        this.currentMileage = currentMileage;
    }
}
