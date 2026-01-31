package com.ryuqq.setof.adapter.out.persistence.architecture;

/**
 * ArchUnit 테스트용 패키지 상수 정의.
 *
 * <p>persistence-mysql 모듈의 패키지 경로를 상수로 정의합니다.
 */
public final class ArchUnitPackageConstants {

    private ArchUnitPackageConstants() {}

    // ===== Base Package =====
    public static final String BASE = "com.ryuqq.setof.adapter.out.persistence";

    // ===== Sub Package Patterns =====
    public static final String ADAPTER_ALL = BASE + "..adapter..";
    public static final String ENTITY_ALL = BASE + "..entity..";
    public static final String MAPPER_ALL = BASE + "..mapper..";
    public static final String REPOSITORY_ALL = BASE + "..repository..";
    public static final String CONDITION_ALL = BASE + "..condition..";
    public static final String CONFIG_ALL = BASE + "..config..";
    public static final String COMMON_ALL = BASE + "..common..";

    // ===== Domain Package =====
    public static final String DOMAIN_ALL = "com.ryuqq.setof.domain..";

    // ===== Application Package =====
    public static final String APPLICATION_ALL = "com.ryuqq.setof.application..";

    // ===== Spring Data JPA =====
    public static final String SPRING_DATA_JPA = "org.springframework.data.jpa..";

    // ===== QueryDSL =====
    public static final String QUERYDSL = "com.querydsl..";
}
