package com.connectly.partnerAdmin.auth.entity;

import com.connectly.partnerAdmin.auth.enums.RoleType;
import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "auth_group")
@Entity
public class AuthGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_group_id")
    private long id;

    @Column(name = "auth_group_type")
    @Enumerated(EnumType.STRING)
    private RoleType authGroupType;

    @Column(name = "GROUP_DESCRIPTION", length = 200)
    private String groupDescription;

}
