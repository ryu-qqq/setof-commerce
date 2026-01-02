package com.connectly.partnerAdmin.auth.entity;

import com.connectly.partnerAdmin.auth.enums.RoleType;
import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;




@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "admin_auth_group")
@Entity
public class AdminAuthGroup extends BaseEntity {

    @Id
    @Column(name = "admin_id")
    private long id;

    @Column(name = "auth_group_id")
    private long authGroupId;

    public AdminAuthGroup(long id, RoleType roleType){
        this.id = id;
        this.authGroupId = roleType.isSeller() ? 2L : 1L;
    }

    public void update(RoleType roleType){
        this.authGroupId = roleType.isSeller() ? 2L : 1L;
    }
}
