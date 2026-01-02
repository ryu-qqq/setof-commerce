package com.connectly.partnerAdmin.auth.dto;

import com.connectly.partnerAdmin.auth.entity.Administrators;
import com.connectly.partnerAdmin.auth.enums.ApprovalStatus;
import com.connectly.partnerAdmin.auth.enums.RoleType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdministratorsInsertRequestDto {

    private RoleType roleType;
    private String passwordHash;
    private String email;
    private String fullName;
    private String phoneNumber;
    private long sellerId;

    public Administrators toAdministratorsEntity(String passwordHash){
        return Administrators.builder()
                .passwordHash(passwordHash)
                .fullName(fullName)
                .email(email)
                .phoneNumber(phoneNumber)
                .sellerId(sellerId)
                .approvalStatus(ApprovalStatus.PENDING)
                .build();
    }

}
