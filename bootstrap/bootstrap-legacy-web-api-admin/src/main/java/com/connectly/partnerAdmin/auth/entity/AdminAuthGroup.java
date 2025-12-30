package com.connectly.partnerAdmin.auth.entity;

import com.connectly.partnerAdmin.auth.enums.RoleType;
import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;




@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "ADMIN_AUTH_GROUP")
@Entity
public class AdminAuthGroup extends BaseEntity {

    @Id
    @Column(name = "ADMIN_ID")
    private long id;

    @Column(name = "AUTH_GROUP_ID")
    private long authGroupId;

    public AdminAuthGroup(long id, RoleType roleType){
        this.id = id;
        this.authGroupId = roleType.isSeller() ? 2L : 1L;
    }

    public void update(RoleType roleType){
        this.authGroupId = roleType.isSeller() ? 2L : 1L;
    }
}
