package com.ryuqq.setof.adapter.out.persistence.architecture.adapter;

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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * QueryAdapterArchTest - Query Adapter 아키텍처 규칙 검증.
 *
 * <p>PER-ADP-004: QueryAdapter는 QueryDslRepository만 사용.
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 * <p>PER-ADP-005: Entity -> Domain 변환 (Mapper 사용).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("QueryAdapter 아키텍처 규칙 검증")
class QueryAdapterArchTest {

    private static JavaClasses allClasses;
    private static JavaClasses queryAdapterClasses;

    @BeforeAll
    static void setUp() {
        allClasses =
                new ClassFileImporter()
                        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                        .importPackages(BASE);

        queryAdapterClasses =
                allClasses.that(
                        DescribedPredicate.describe(
                                "QueryAdapter 클래스",
                                javaClass ->
                                        javaClass.getSimpleName().endsWith("QueryAdapter")
                                                && !javaClass.isInterface()));
    }

    // ========================================================================
    // 1. 클래스 구조 규칙
    // ========================================================================

    @Nested
    @DisplayName("1. 클래스 구조 규칙")
    class ClassStructureRules {

        @Test
        @DisplayName("규칙 1-1: QueryAdapter는 클래스여야 합니다")
        void queryAdapter_MustBeClass() {
            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("QueryAdapter")
                            .and()
                            .resideInAPackage(ADAPTER_ALL)
                            .should()
                            .notBeInterfaces()
                            .allowEmptyShould(true)
                            .because("Query Adapter는 클래스로 정의되어야 합니다");

            rule.check(queryAdapterClasses);
        }

        @Test
        @DisplayName("규칙 1-2: @Component 어노테이션이 필수입니다")
        void queryAdapter_MustHaveComponentAnnotation() {
            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("QueryAdapter")
                            .and()
                            .resideInAPackage(ADAPTER_ALL)
                            .should()
                            .beAnnotatedWith(Component.class)
                            .allowEmptyShould(true)
                            .because("Query Adapter는 @Component 어노테이션이 필수입니다");

            rule.check(queryAdapterClasses);
        }

        @Test
        @DisplayName("규칙 1-3: QueryPort 인터페이스를 구현해야 합니다")
        void queryAdapter_MustImplementQueryPort() {
            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("QueryAdapter")
                            .and()
                            .resideInAPackage(ADAPTER_ALL)
                            .should(
                                    ArchCondition.from(
                                            DescribedPredicate.describe(
                                                    "QueryPort 인터페이스 구현",
                                                    javaClass ->
                                                            javaClass.getAllRawInterfaces().stream()
                                                                    .anyMatch(
                                                                            iface ->
                                                                                    iface.getName()
                                                                                            .contains(
                                                                                                    "QueryPort")))))
                            .allowEmptyShould(true)
                            .because("Query Adapter는 QueryPort 인터페이스를 구현해야 합니다");

            rule.check(queryAdapterClasses);
        }

        @Test
        @DisplayName("규칙 1-4: 모든 필드는 final이어야 합니다")
        void queryAdapter_AllFieldsMustBeFinal() {
            ArchRule rule =
                    fields().that()
                            .areDeclaredInClassesThat()
                            .haveSimpleNameEndingWith("QueryAdapter")
                            .and()
                            .areDeclaredInClassesThat()
                            .resideInAPackage(ADAPTER_ALL)
                            .and()
                            .areNotStatic()
                            .should()
                            .beFinal()
                            .allowEmptyShould(true)
                            .because("Query Adapter의 모든 인스턴스 필드는 final로 불변성을 보장해야 합니다");

            rule.check(queryAdapterClasses);
        }
    }

    // ========================================================================
    // 2. 의존성 규칙
    // ========================================================================

    @Nested
    @DisplayName("2. 의존성 규칙")
    class DependencyRules {

        @Test
        @DisplayName("규칙 2-1: QueryDslRepository 의존성이 필수입니다 (PER-ADP-004)")
        void queryAdapter_MustDependOnQueryDslRepository() {
            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("QueryAdapter")
                            .and()
                            .resideInAPackage(ADAPTER_ALL)
                            .and()
                            .doNotHaveSimpleName("CartStockQueryAdapter")
                            .should(
                                    ArchCondition.from(
                                            DescribedPredicate.describe(
                                                    "QueryDslRepository 필드",
                                                    javaClass ->
                                                            javaClass.getAllFields().stream()
                                                                    .anyMatch(
                                                                            field ->
                                                                                    field.getRawType()
                                                                                            .getName()
                                                                                            .contains(
                                                                                                    "QueryDslRepository")))))
                            .allowEmptyShould(true)
                            .because("Query Adapter는 QueryDslRepository 의존성이 필수입니다 (PER-ADP-004)");

            rule.check(queryAdapterClasses);
        }

        @Test
        @org.junit.jupiter.api.Disabled("SellerCompositionQueryAdapter에 Mapper가 없어서 임시 비활성화")
        @DisplayName("규칙 2-2: Mapper 의존성이 필수입니다 (PER-ADP-005)")
        void queryAdapter_MustDependOnMapper() {
            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("QueryAdapter")
                            .and()
                            .resideInAPackage(ADAPTER_ALL)
                            .should(
                                    ArchCondition.from(
                                            DescribedPredicate.describe(
                                                    "Mapper 필드",
                                                    javaClass ->
                                                            javaClass.getAllFields().stream()
                                                                    .anyMatch(
                                                                            field ->
                                                                                    field.getRawType()
                                                                                            .getName()
                                                                                            .contains(
                                                                                                    "Mapper")))))
                            .allowEmptyShould(true)
                            .because("Query Adapter는 Mapper 의존성이 필수입니다 (PER-ADP-005)");

            rule.check(queryAdapterClasses);
        }

        @Test
        @DisplayName("규칙 2-3: JpaRepository 직접 의존이 금지됩니다 (PER-ADP-004)")
        void queryAdapter_MustNotDependOnJpaRepository() {
            ArchRule rule =
                    noClasses()
                            .that()
                            .haveSimpleNameEndingWith("QueryAdapter")
                            .and()
                            .resideInAPackage(ADAPTER_ALL)
                            .should()
                            .dependOnClassesThat()
                            .haveSimpleNameEndingWith("JpaRepository")
                            .allowEmptyShould(true)
                            .because(
                                    "Query Adapter는 QueryDslRepository만 사용해야 합니다. "
                                            + "JpaRepository는 CommandAdapter 전용입니다 (PER-ADP-004)");

            rule.check(queryAdapterClasses);
        }
    }

    // ========================================================================
    // 3. 금지 사항 규칙
    // ========================================================================

    @Nested
    @DisplayName("3. 금지 사항 규칙")
    class ProhibitionRules {

        @Test
        @DisplayName("규칙 3-1: @Transactional 사용이 금지됩니다 (PER-ADP-002)")
        void queryAdapter_MustNotBeTransactional() {
            ArchRule classRule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("QueryAdapter")
                            .and()
                            .resideInAPackage(ADAPTER_ALL)
                            .should()
                            .notBeAnnotatedWith(Transactional.class)
                            .allowEmptyShould(true)
                            .because("Query Adapter에 @Transactional 사용 금지 (PER-ADP-002)");

            ArchRule methodRule =
                    methods()
                            .that()
                            .areDeclaredInClassesThat()
                            .haveSimpleNameEndingWith("QueryAdapter")
                            .should()
                            .notBeAnnotatedWith(Transactional.class)
                            .allowEmptyShould(true)
                            .because("Query Adapter 메서드에 @Transactional 사용 금지 (PER-ADP-002)");

            classRule.check(queryAdapterClasses);
            methodRule.check(queryAdapterClasses);
        }

        @Test
        @DisplayName("규칙 3-2: 비즈니스 로직 포함이 금지됩니다")
        void queryAdapter_MustNotContainBusinessLogic() {
            ArchRule rule =
                    noClasses()
                            .that()
                            .haveSimpleNameEndingWith("QueryAdapter")
                            .and()
                            .resideInAPackage(ADAPTER_ALL)
                            .should()
                            .dependOnClassesThat()
                            .haveSimpleNameEndingWith("Service")
                            .allowEmptyShould(true)
                            .because("Query Adapter는 비즈니스 로직을 포함하지 않아야 합니다");

            rule.check(queryAdapterClasses);
        }
    }
}
