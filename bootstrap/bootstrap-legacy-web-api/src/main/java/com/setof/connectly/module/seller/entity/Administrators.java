package com.setof.connectly.module.seller.entity;

import com.setof.connectly.module.common.BaseEntity;
import com.setof.connectly.module.seller.enums.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "administrators")
@Entity
public class Administrators extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private long id;

    private String passwordHash;
    private String email;
    private String fullName;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    private String refreshToken;
}
