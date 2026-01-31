package com.ryuqq.setof.adapter.out.persistence.architecture.mapper;

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

/**
 * MapperArchTest - Entity-Domain Mapper 아키텍처 규칙 검증.
 *
 * <p>PER-MAP-001: Mapper는 @Component로 등록.
 *
 * <p>PER-MAP-002: toEntity(Domain) + toDomain(Entity) 메서드 제공.
 *
 * <p>PER-MAP-003: 순수 변환 로직만.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("Mapper 아키텍처 규칙 검증")
class MapperArchTest {

    private static JavaClasses allClasses;
    private static JavaClasses mapperClasses;

    @BeforeAll
    static void setUp() {
        allClasses =
                new ClassFileImporter()
                        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                        .importPackages(BASE);

        mapperClasses =
                allClasses.that(
                        DescribedPredicate.describe(
                                "Mapper 클래스",
                                javaClass ->
                                        javaClass.getSimpleName().endsWith("JpaEntityMapper")
                                                && !javaClass.isInterface()));
    }

    // ========================================================================
    // 1. 클래스 구조 규칙
    // ========================================================================

    @Nested
    @DisplayName("1. 클래스 구조 규칙")
    class ClassStructureRules {

        @Test
        @DisplayName("규칙 1-1: Mapper는 클래스여야 합니다")
        void mapper_MustBeClass() {
            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("JpaEntityMapper")
                            .and()
                            .resideInAPackage(MAPPER_ALL)
                            .should()
                            .notBeInterfaces()
                            .allowEmptyShould(true)
                            .because("Mapper는 클래스로 정의되어야 합니다");

            rule.check(mapperClasses);
        }

        @Test
        @DisplayName("규칙 1-2: @Component 어노테이션이 필수입니다 (PER-MAP-001)")
        void mapper_MustHaveComponentAnnotation() {
            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("JpaEntityMapper")
                            .and()
                            .resideInAPackage(MAPPER_ALL)
                            .should()
                            .beAnnotatedWith(Component.class)
                            .allowEmptyShould(true)
                            .because("Mapper는 @Component 어노테이션이 필수입니다 (PER-MAP-001)");

            rule.check(mapperClasses);
        }
    }

    // ========================================================================
    // 2. 메서드 규칙
    // ========================================================================

    @Nested
    @DisplayName("2. 메서드 규칙")
    class MethodRules {

        @Test
        @DisplayName("규칙 2-1: toEntity 메서드가 필수입니다 (PER-MAP-002)")
        void mapper_MustHaveToEntityMethod() {
            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("JpaEntityMapper")
                            .and()
                            .resideInAPackage(MAPPER_ALL)
                            .should(
                                    ArchCondition.from(
                                            DescribedPredicate.describe(
                                                    "toEntity 메서드",
                                                    javaClass ->
                                                            javaClass.getMethods().stream()
                                                                    .anyMatch(
                                                                            method ->
                                                                                    method.getName()
                                                                                            .equals(
                                                                                                    "toEntity")))))
                            .allowEmptyShould(true)
                            .because("Mapper는 toEntity 메서드가 필수입니다 (PER-MAP-002)");

            rule.check(mapperClasses);
        }

        @Test
        @DisplayName("규칙 2-2: toDomain 메서드가 필수입니다 (PER-MAP-002)")
        void mapper_MustHaveToDomainMethod() {
            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("JpaEntityMapper")
                            .and()
                            .resideInAPackage(MAPPER_ALL)
                            .should(
                                    ArchCondition.from(
                                            DescribedPredicate.describe(
                                                    "toDomain 메서드",
                                                    javaClass ->
                                                            javaClass.getMethods().stream()
                                                                    .anyMatch(
                                                                            method ->
                                                                                    method.getName()
                                                                                            .equals(
                                                                                                    "toDomain")))))
                            .allowEmptyShould(true)
                            .because("Mapper는 toDomain 메서드가 필수입니다 (PER-MAP-002)");

            rule.check(mapperClasses);
        }
    }

    // ========================================================================
    // 3. 의존성 규칙
    // ========================================================================

    @Nested
    @DisplayName("3. 의존성 규칙")
    class DependencyRules {

        @Test
        @DisplayName("규칙 3-1: Repository 의존성이 금지됩니다 (PER-MAP-003)")
        void mapper_MustNotDependOnRepository() {
            ArchRule rule =
                    noClasses()
                            .that()
                            .haveSimpleNameEndingWith("JpaEntityMapper")
                            .and()
                            .resideInAPackage(MAPPER_ALL)
                            .should()
                            .dependOnClassesThat()
                            .haveSimpleNameEndingWith("Repository")
                            .allowEmptyShould(true)
                            .because("Mapper는 Repository에 의존할 수 없습니다 (PER-MAP-003)");

            rule.check(mapperClasses);
        }

        @Test
        @DisplayName("규칙 3-2: Service 의존성이 금지됩니다 (PER-MAP-003)")
        void mapper_MustNotDependOnService() {
            ArchRule rule =
                    noClasses()
                            .that()
                            .haveSimpleNameEndingWith("JpaEntityMapper")
                            .and()
                            .resideInAPackage(MAPPER_ALL)
                            .should()
                            .dependOnClassesThat()
                            .haveSimpleNameEndingWith("Service")
                            .allowEmptyShould(true)
                            .because("Mapper는 Service에 의존할 수 없습니다 (PER-MAP-003)");

            rule.check(mapperClasses);
        }

        @Test
        @DisplayName("규칙 3-3: Adapter 의존성이 금지됩니다 (PER-MAP-003)")
        void mapper_MustNotDependOnAdapter() {
            ArchRule rule =
                    noClasses()
                            .that()
                            .haveSimpleNameEndingWith("JpaEntityMapper")
                            .and()
                            .resideInAPackage(MAPPER_ALL)
                            .should()
                            .dependOnClassesThat()
                            .haveSimpleNameEndingWith("Adapter")
                            .allowEmptyShould(true)
                            .because("Mapper는 Adapter에 의존할 수 없습니다 (PER-MAP-003)");

            rule.check(mapperClasses);
        }
    }

    // ========================================================================
    // 4. Lombok 금지 규칙
    // ========================================================================

    @Nested
    @DisplayName("4. Lombok 금지 규칙")
    class LombokProhibitionRules {

        @Test
        @DisplayName("규칙 4-1: Lombok 의존성이 금지됩니다")
        void mapper_MustNotUseLombok() {
            ArchRule rule =
                    noClasses()
                            .that()
                            .haveSimpleNameEndingWith("JpaEntityMapper")
                            .and()
                            .resideInAPackage(MAPPER_ALL)
                            .should()
                            .dependOnClassesThat()
                            .resideInAPackage("lombok..")
                            .allowEmptyShould(true)
                            .because("Mapper에서 Lombok 사용 금지");

            rule.check(mapperClasses);
        }
    }

    // ========================================================================
    // 5. 네이밍 규칙
    // ========================================================================

    @Nested
    @DisplayName("5. 네이밍 규칙")
    class NamingRules {

        @Test
        @DisplayName("규칙 5-1: Mapper는 mapper 패키지에 위치해야 합니다")
        void mapper_MustResideInMapperPackage() {
            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("JpaEntityMapper")
                            .should()
                            .resideInAPackage(MAPPER_ALL)
                            .allowEmptyShould(true)
                            .because("Mapper는 mapper 패키지에 위치해야 합니다");

            rule.check(mapperClasses);
        }
    }
}
