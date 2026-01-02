package com.connectly.partnerAdmin.module.user.entity;


import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.user.enums.Gender;
import com.connectly.partnerAdmin.module.user.enums.SocialLoginType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "USERS")
@Entity
public class Users extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    @Setter(AccessLevel.PRIVATE)
    private long id;

    @Column(name = "SOCIAL_PK_ID")
    private String socialPkId;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "SOCIAL_LOGIN_TYPE")
    @Enumerated(EnumType.STRING)
    private SocialLoginType socialLoginType;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD_HASH")
    private String passwordHash;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DATE_OF_BIRTH")
    private Date dateOfBirth;

    @Column(name = "GENDER")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "PRIVACY_CONSENT")
    @Enumerated(value = EnumType.STRING)
    private Yn privacyConsent;

    @Column(name = "SERVICE_TERMS_CONSENT")
    @Enumerated(value = EnumType.STRING)
    private Yn serviceTermsConsent;

    @Column(name = "AD_CONSENT")
    @Enumerated(value = EnumType.STRING)
    private Yn adConsent;

    @Column(name = "WITHDRAWAL_YN")
    @Enumerated(value = EnumType.STRING)
    private Yn withdrawalYn;

    @Column(name = "EXMALL_USER_YN")
    @Enumerated(value = EnumType.STRING)
    private Yn exMallUserYn;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_GRADE_ID", nullable = false)
    private UserGrade userGrade;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<ShippingAddress> shippingAddresses = new LinkedHashSet<>();

    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private UserMileage userMileage;

    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private RefundAccount refundAccount;

    public void setUserMileage(UserMileage userMileage) {
        if (this.userMileage != null && this.userMileage.equals(userMileage)) {
            return;
        }

        if (this.userMileage != null) {
            this.userMileage.setUsers(null);
        }
        this.userMileage = userMileage;

        if (userMileage != null) {
            userMileage.setUsers(this);
        }
    }

}
