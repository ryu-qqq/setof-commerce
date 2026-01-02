package com.connectly.partnerAdmin.auth.entity;

import com.connectly.partnerAdmin.auth.enums.ApprovalStatus;
import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "administrators")
@Entity
public class Administrators extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private long id;

    @Column(name = "PASSWORD_HASH", length = 60, nullable = false)
    private String passwordHash;

    @Column(name = "EMAIL", length = 100, nullable = false)
    private String email;

    @Column(name = "FULL_NAME", length = 45, nullable = false)
    private String fullName;

    @Column(name = "PHONE_NUMBER", length = 15, nullable = false)
    private String phoneNumber;

    @Column(name = "REFRESH_TOKEN", length = 256)
    private String refreshToken;

    @Column(name = "seller_id")
    private long sellerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status")
    private ApprovalStatus approvalStatus;


    public void update(Administrators administrators){
        passwordHash = administrators.getPasswordHash();
        email = administrators.getEmail();
        fullName = administrators.getFullName();
        phoneNumber = administrators.getPhoneNumber();
    }

    public void updateStatus(ApprovalStatus approvalStatus){
        this.approvalStatus = approvalStatus;
    }




}
