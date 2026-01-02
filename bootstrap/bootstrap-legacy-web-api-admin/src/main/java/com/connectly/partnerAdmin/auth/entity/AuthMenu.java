package com.connectly.partnerAdmin.auth.entity;

import com.connectly.partnerAdmin.auth.enums.AccessType;
import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@IdClass(AuthMenuId.class)
@Table(name = "AUTH_MENU")
@Entity
public class AuthMenu extends BaseEntity {

    @Id
    @Column(name = "AUTH_GROUP_ID")
    private long authGroupId;

    @Id
    @Column(name = "MENU_ID")
    private long menuId;

    @Column(name = "ACCESS_TYPE", length = 20)
    @Enumerated(EnumType.STRING)
    private AccessType accessType;

}
