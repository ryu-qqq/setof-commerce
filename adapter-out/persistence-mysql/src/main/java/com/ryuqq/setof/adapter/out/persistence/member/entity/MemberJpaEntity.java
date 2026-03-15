package com.ryuqq.setof.adapter.out.persistence.member.entity;

import com.ryuqq.setof.adapter.out.persistence.common.entity.SoftDeletableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;

/**
 * MemberJpaEntity - 회원 JPA 엔티티.
 *
 * <p>PER-ENT-001: @Entity, @Table 필수.
 *
 * <p>PER-ENT-002: JPA 연관관계 어노테이션 금지.
 *
 * <p>PER-ENT-004: Lombok 금지.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Entity
@Table(name = "members")
public class MemberJpaEntity extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    protected MemberJpaEntity() {
        super();
    }

    private MemberJpaEntity(
            Long id,
            String name,
            String email,
            String phoneNumber,
            LocalDate dateOfBirth,
            String gender,
            String status,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        super(createdAt, updatedAt, deletedAt);
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.status = status;
    }

    public static MemberJpaEntity create(
            Long id,
            String name,
            String email,
            String phoneNumber,
            LocalDate dateOfBirth,
            String gender,
            String status,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt) {
        return new MemberJpaEntity(
                id,
                name,
                email,
                phoneNumber,
                dateOfBirth,
                gender,
                status,
                createdAt,
                updatedAt,
                deletedAt);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public String getStatus() {
        return status;
    }
}
