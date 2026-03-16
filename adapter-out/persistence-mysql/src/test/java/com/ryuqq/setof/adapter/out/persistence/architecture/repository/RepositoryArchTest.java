package com.ryuqq.setof.adapter.out.persistence.architecture.repository;

import static com.ryuqq.setof.adapter.out.persistence.architecture.ArchUnitPackageConstants.*;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * RepositoryArchTest - Repository 아키텍처 규칙 검증.
 *
 * <p>PER-REP-001: JpaRepository는 Spring Data JPA 상속.
 *
 * <p>PER-REP-002: 모든 조회 로직은 QueryDslRepository에서 처리.
 *
 * <p>PER-REP-003: QueryDslRepository는 JPAQueryFactory 사용.
 *
 * <p>PER-REP-004: ConditionBuilder를 사용하여 동적 쿼리 구성.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("Repository 아키텍처 규칙 검증")
class RepositoryArchTest {

    private static JavaClasses allClasses;
    private static JavaClasses jpaRepositoryClasses;
    private static JavaClasses queryDslRepositoryClasses;

    @BeforeAll
    static void setUp() {
        allClasses =
                new ClassFileImporter()
                        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                        .importPackages(BASE);

        jpaRepositoryClasses =
                allClasses.that(
                        DescribedPredicate.describe(
                                "JpaRepository 인터페이스",
                                javaClass ->
                                        javaClass.getSimpleName().endsWith("JpaRepository")
                                                && javaClass.isInterface()));

        queryDslRepositoryClasses =
                allClasses.that(
                        DescribedPredicate.describe(
                                "QueryDslRepository 클래스",
                                javaClass ->
                                        javaClass.getSimpleName().endsWith("QueryDslRepository")
                                                && !javaClass.isInterface()));
    }

    // ========================================================================
    // 1. JpaRepository 규칙
    // ========================================================================

    @Nested
    @DisplayName("1. JpaRepository 규칙")
    class JpaRepositoryRules {

        @Test
        @DisplayName("규칙 1-1: JpaRepository는 인터페이스여야 합니다")
        void jpaRepository_MustBeInterface() {
            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("JpaRepository")
                            .and()
                            .resideInAPackage(REPOSITORY_ALL)
                            .should()
                            .beInterfaces()
                            .allowEmptyShould(true)
                            .because("JpaRepository는 인터페이스로 정의되어야 합니다");

            rule.check(jpaRepositoryClasses);
        }

        @Test
        @DisplayName("규칙 1-2: JpaRepository는 Spring Data JPA Repository를 상속해야 합니다 (PER-REP-001)")
        void jpaRepository_MustExtendJpaRepository() {
            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("JpaRepository")
                            .and()
                            .resideInAPackage(REPOSITORY_ALL)
                            .should()
                            .beAssignableTo(JpaRepository.class)
                            .allowEmptyShould(true)
                            .because(
                                    "JpaRepository는 Spring Data JPA Repository를 상속해야 합니다"
                                            + " (PER-REP-001)");

            rule.check(jpaRepositoryClasses);
        }
    }

    // ========================================================================
    // 2. QueryDslRepository 규칙
    // ========================================================================

    @Nested
    @DisplayName("2. QueryDslRepository 규칙")
    class QueryDslRepositoryRules {

        @Test
        @DisplayName("규칙 2-1: QueryDslRepository는 클래스여야 합니다")
        void queryDslRepository_MustBeClass() {
            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("QueryDslRepository")
                            .and()
                            .resideInAPackage(REPOSITORY_ALL)
                            .should()
                            .notBeInterfaces()
                            .allowEmptyShould(true)
                            .because("QueryDslRepository는 클래스로 정의되어야 합니다");

            rule.check(queryDslRepositoryClasses);
        }

        @Test
        @DisplayName("규칙 2-2: @Repository 어노테이션이 필수입니다")
        void queryDslRepository_MustHaveRepositoryAnnotation() {
            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("QueryDslRepository")
                            .and()
                            .resideInAPackage(REPOSITORY_ALL)
                            .should()
                            .beAnnotatedWith(Repository.class)
                            .allowEmptyShould(true)
                            .because("QueryDslRepository는 @Repository 어노테이션이 필수입니다");

            rule.check(queryDslRepositoryClasses);
        }

        @Test
        @DisplayName("규칙 2-3: JPAQueryFactory 의존성이 필수입니다 (PER-REP-003)")
        void queryDslRepository_MustDependOnJPAQueryFactory() {
            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("QueryDslRepository")
                            .and()
                            .resideInAPackage(REPOSITORY_ALL)
                            .should(
                                    ArchCondition.from(
                                            DescribedPredicate.describe(
                                                    "JPAQueryFactory 필드",
                                                    javaClass ->
                                                            javaClass.getAllFields().stream()
                                                                    .anyMatch(
                                                                            field ->
                                                                                    field.getRawType()
                                                                                            .getName()
                                                                                            .contains(
                                                                                                    "JPAQueryFactory")))))
                            .allowEmptyShould(true)
                            .because(
                                    "QueryDslRepository는 JPAQueryFactory 의존성이 필수입니다 (PER-REP-003)");

            rule.check(queryDslRepositoryClasses);
        }

        @Test
        @DisplayName("규칙 2-4: ConditionBuilder 의존성이 필수입니다 (PER-REP-004)")
        void queryDslRepository_MustDependOnConditionBuilder() {
            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("QueryDslRepository")
                            .and()
                            .resideInAPackage(REPOSITORY_ALL)
                            .and()
                            .doNotHaveSimpleName("DiscountOutboxQueryDslRepository")
                            .and()
                            .doNotHaveSimpleName("ComponentFixedProductQueryDslRepository")
                            .and()
                            .doNotHaveSimpleName("ImageVariantQueryDslRepository")
                            .and()
                            .doNotHaveSimpleName("SellerOptionGroupQueryDslRepository")
                            .and()
                            .doNotHaveSimpleName("DescriptionImageQueryDslRepository")
                            .and()
                            .doNotHaveSimpleName("ProductGroupImageQueryDslRepository")
                            .and()
                            .doNotHaveSimpleName("ProductGroupPriceQueryDslRepository")
                            .should(
                                    ArchCondition.from(
                                            DescribedPredicate.describe(
                                                    "ConditionBuilder 필드",
                                                    javaClass ->
                                                            javaClass.getAllFields().stream()
                                                                    .anyMatch(
                                                                            field ->
                                                                                    field.getRawType()
                                                                                            .getName()
                                                                                            .contains(
                                                                                                    "ConditionBuilder")))))
                            .allowEmptyShould(true)
                            .because(
                                    "QueryDslRepository는 ConditionBuilder 의존성이 필수입니다"
                                            + " (PER-REP-004)");

            rule.check(queryDslRepositoryClasses);
        }

        @Test
        @DisplayName("규칙 2-5: 모든 필드는 final이어야 합니다")
        void queryDslRepository_AllFieldsMustBeFinal() {
            ArchRule rule =
                    fields().that()
                            .areDeclaredInClassesThat()
                            .haveSimpleNameEndingWith("QueryDslRepository")
                            .and()
                            .areDeclaredInClassesThat()
                            .resideInAPackage(REPOSITORY_ALL)
                            .and()
                            .areNotStatic()
                            .and()
                            .areNotAnnotatedWith(jakarta.persistence.PersistenceContext.class)
                            .should()
                            .beFinal()
                            .allowEmptyShould(true)
                            .because("QueryDslRepository의 모든 인스턴스 필드는 final로 불변성을 보장해야 합니다");

            rule.check(queryDslRepositoryClasses);
        }
    }

    // ========================================================================
    // 3. 네이밍 규칙
    // ========================================================================

    @Nested
    @DisplayName("3. 네이밍 규칙")
    class NamingRules {

        @Test
        @DisplayName("규칙 3-1: Repository는 repository 패키지에 위치해야 합니다")
        void repository_MustResideInRepositoryPackage() {
            ArchRule jpaRule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("JpaRepository")
                            .should()
                            .resideInAPackage(REPOSITORY_ALL)
                            .allowEmptyShould(true)
                            .because("JpaRepository는 repository 패키지에 위치해야 합니다");

            ArchRule queryDslRule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("QueryDslRepository")
                            .should()
                            .resideInAPackage(REPOSITORY_ALL)
                            .allowEmptyShould(true)
                            .because("QueryDslRepository는 repository 패키지에 위치해야 합니다");

            jpaRule.check(jpaRepositoryClasses);
            queryDslRule.check(queryDslRepositoryClasses);
        }
    }
}
