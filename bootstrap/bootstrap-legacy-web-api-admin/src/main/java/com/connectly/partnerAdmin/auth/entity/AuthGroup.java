package com.connectly.partnerAdmin.auth.entity;

import com.connectly.partnerAdmin.auth.enums.RoleType;
import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "AUTH_GROUP")
@Entity
public class AuthGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AUTH_GROUP_ID")
    private long id;

    @Column(name = "AUTH_GROUP_TYPE")
    @Enumerated(EnumType.STRING)
    private RoleType authGroupType;

    @Column(name = "GROUP_DESCRIPTION", length = 200)
    private String groupDescription;

}
