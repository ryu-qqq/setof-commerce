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
@Table(name = "users")
@Entity
public class Users extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @Setter(AccessLevel.PRIVATE)
    private long id;

    @Column(name = "social_pk_id")
    private String socialPkId;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "social_login_type")
    @Enumerated(EnumType.STRING)
    private SocialLoginType socialLoginType;

    @Column(name = "email")
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "name")
    private String name;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "privacy_consent")
    @Enumerated(value = EnumType.STRING)
    private Yn privacyConsent;

    @Column(name = "service_terms_consent")
    @Enumerated(value = EnumType.STRING)
    private Yn serviceTermsConsent;

    @Column(name = "ad_consent")
    @Enumerated(value = EnumType.STRING)
    private Yn adConsent;

    @Column(name = "withdrawal_yn")
    @Enumerated(value = EnumType.STRING)
    private Yn withdrawalYn;

    @Column(name = "exmall_user_yn")
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
