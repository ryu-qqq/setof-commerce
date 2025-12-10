package com.ryuqq.setof.adapter.out.persistence.refreshtoken.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * RefreshTokenJpaEntity - Refresh Token JPA Entity
 *
 * <p>Persistence Layer의 JPA Entity로서 refresh_tokens 테이블과 매핑됩니다.
 *
 * <p><strong>Auto Increment PK:</strong>
 *
 * <ul>
 *   <li>토큰은 내부 관리용이므로 순차 ID 사용
 *   <li>외부 노출되지 않음 (토큰 값으로만 접근)
 * </ul>
 *
 * <p><strong>Long FK 전략:</strong>
 *
 * <ul>
 *   <li>JPA 관계 어노테이션 사용 금지 (@ManyToOne 등)
 *   <li>memberId: String UUID로 직접 관리
 * </ul>
 *
 * <p><strong>Lombok 금지:</strong>
 *
 * <ul>
 *   <li>Plain Java getter 사용
 *   <li>Setter 제공 금지
 *   <li>명시적 생성자 제공
 * </ul>
 *
 * @author setof-commerce
 * @since 1.0.0
 */
@Entity
@Table(name = "refresh_tokens")
public class RefreshTokenJpaEntity {

    /**
     * 기본 키 (Auto Increment)
     *
     * <p>내부 관리용 순차 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * 회원 ID (UUID v7 문자열)
     *
     * <p>Long FK 전략: FK 관계 없이 String으로 관리
     */
    @Column(name = "member_id", nullable = false, length = 36)
    private String memberId;

    /**
     * Refresh Token 값
     *
     * <p>JWT 형식, unique 제약
     */
    @Column(name = "token", nullable = false, length = 512, unique = true)
    private String token;

    /** 만료 일시 (UTC 기준) */
    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    /** 생성 일시 (UTC 기준) */
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    /**
     * JPA 기본 생성자 (protected)
     *
     * <p>JPA 스펙 요구사항으로 반드시 필요합니다.
     */
    protected RefreshTokenJpaEntity() {
        // JPA 기본 생성자
    }

    /**
     * 전체 필드 생성자 (private)
     *
     * <p>직접 호출 금지, of() 스태틱 메서드로만 생성하세요.
     */
    private RefreshTokenJpaEntity(
            String memberId, String token, Instant expiresAt, Instant createdAt) {
        this.memberId = memberId;
        this.token = token;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    /**
     * of() 스태틱 팩토리 메서드
     *
     * <p>Entity 생성은 반드시 이 메서드를 통해서만 가능합니다.
     *
     * @param memberId 회원 ID (UUID v7 문자열)
     * @param token Refresh Token 값
     * @param expiresAt 만료 일시 (UTC 기준 Instant)
     * @param createdAt 생성 일시 (UTC 기준 Instant)
     * @return RefreshTokenJpaEntity 인스턴스
     */
    public static RefreshTokenJpaEntity of(
            String memberId, String token, Instant expiresAt, Instant createdAt) {
        return new RefreshTokenJpaEntity(memberId, token, expiresAt, createdAt);
    }

    // ===== Getters (Setter 제공 금지) =====

    public Long getId() {
        return id;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getToken() {
        return token;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
