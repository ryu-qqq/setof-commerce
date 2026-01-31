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
 * CommandAdapterArchTest - Command Adapter 아키텍처 규칙 검증.
 *
 * <p>PER-ADP-001: CommandAdapter는 JpaRepository만 사용.
 *
 * <p>PER-ADP-002: Adapter에서 @Transactional 금지.
 *
 * <p>PER-ADP-003: Domain 반환 (DTO 반환 금지).
 *
 * <p>PER-ADP-005: Domain -> Entity 변환 (Mapper 사용).
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("CommandAdapter 아키텍처 규칙 검증")
class CommandAdapterArchTest {

    private static JavaClasses allClasses;
    private static JavaClasses commandAdapterClasses;

    @BeforeAll
    static void setUp() {
        allClasses =
                new ClassFileImporter()
                        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                        .importPackages(BASE);

        commandAdapterClasses =
                allClasses.that(
                        DescribedPredicate.describe(
                                "CommandAdapter 클래스",
                                javaClass ->
                                        javaClass.getSimpleName().endsWith("CommandAdapter")
                                                && !javaClass.isInterface()));
    }

    // ========================================================================
    // 1. 클래스 구조 규칙
    // ========================================================================

    @Nested
    @DisplayName("1. 클래스 구조 규칙")
    class ClassStructureRules {

        @Test
        @DisplayName("규칙 1-1: CommandAdapter는 클래스여야 합니다")
        void commandAdapter_MustBeClass() {
            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("CommandAdapter")
                            .and()
                            .resideInAPackage(ADAPTER_ALL)
                            .should()
                            .notBeInterfaces()
                            .allowEmptyShould(true)
                            .because("Command Adapter는 클래스로 정의되어야 합니다");

            rule.check(commandAdapterClasses);
        }

        @Test
        @DisplayName("규칙 1-2: @Component 어노테이션이 필수입니다")
        void commandAdapter_MustHaveComponentAnnotation() {
            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("CommandAdapter")
                            .and()
                            .resideInAPackage(ADAPTER_ALL)
                            .should()
                            .beAnnotatedWith(Component.class)
                            .allowEmptyShould(true)
                            .because("Command Adapter는 @Component 어노테이션이 필수입니다");

            rule.check(commandAdapterClasses);
        }

        @Test
        @DisplayName("규칙 1-3: CommandPort 인터페이스를 구현해야 합니다")
        void commandAdapter_MustImplementCommandPort() {
            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("CommandAdapter")
                            .and()
                            .resideInAPackage(ADAPTER_ALL)
                            .should(
                                    ArchCondition.from(
                                            DescribedPredicate.describe(
                                                    "CommandPort 인터페이스 구현",
                                                    javaClass ->
                                                            javaClass.getAllRawInterfaces().stream()
                                                                    .anyMatch(
                                                                            iface ->
                                                                                    iface.getName()
                                                                                            .contains(
                                                                                                    "CommandPort")))))
                            .allowEmptyShould(true)
                            .because("Command Adapter는 CommandPort 인터페이스를 구현해야 합니다");

            rule.check(commandAdapterClasses);
        }

        @Test
        @DisplayName("규칙 1-4: 모든 필드는 final이어야 합니다")
        void commandAdapter_AllFieldsMustBeFinal() {
            ArchRule rule =
                    fields().that()
                            .areDeclaredInClassesThat()
                            .haveSimpleNameEndingWith("CommandAdapter")
                            .and()
                            .areDeclaredInClassesThat()
                            .resideInAPackage(ADAPTER_ALL)
                            .and()
                            .areNotStatic()
                            .should()
                            .beFinal()
                            .allowEmptyShould(true)
                            .because("Command Adapter의 모든 인스턴스 필드는 final로 불변성을 보장해야 합니다");

            rule.check(commandAdapterClasses);
        }
    }

    // ========================================================================
    // 2. 의존성 규칙
    // ========================================================================

    @Nested
    @DisplayName("2. 의존성 규칙")
    class DependencyRules {

        @Test
        @DisplayName("규칙 2-1: JpaRepository 의존성이 필수입니다 (PER-ADP-001)")
        void commandAdapter_MustDependOnJpaRepository() {
            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("CommandAdapter")
                            .and()
                            .resideInAPackage(ADAPTER_ALL)
                            .should(
                                    ArchCondition.from(
                                            DescribedPredicate.describe(
                                                    "JpaRepository 필드",
                                                    javaClass ->
                                                            javaClass.getAllFields().stream()
                                                                    .anyMatch(
                                                                            field ->
                                                                                    field.getRawType()
                                                                                            .getName()
                                                                                            .contains(
                                                                                                    "JpaRepository")))))
                            .allowEmptyShould(true)
                            .because("Command Adapter는 JpaRepository 의존성이 필수입니다 (PER-ADP-001)");

            rule.check(commandAdapterClasses);
        }

        @Test
        @DisplayName("규칙 2-2: Mapper 의존성이 필수입니다 (PER-ADP-005)")
        void commandAdapter_MustDependOnMapper() {
            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("CommandAdapter")
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
                            .because("Command Adapter는 Mapper 의존성이 필수입니다 (PER-ADP-005)");

            rule.check(commandAdapterClasses);
        }

        @Test
        @DisplayName("규칙 2-3: QueryDslRepository 의존성이 금지됩니다 (PER-ADP-001)")
        void commandAdapter_MustNotDependOnQueryDslRepository() {
            ArchRule rule =
                    noClasses()
                            .that()
                            .haveSimpleNameEndingWith("CommandAdapter")
                            .and()
                            .resideInAPackage(ADAPTER_ALL)
                            .should()
                            .dependOnClassesThat()
                            .haveSimpleNameEndingWith("QueryDslRepository")
                            .allowEmptyShould(true)
                            .because(
                                    "Command Adapter는 JpaRepository만 사용해야 합니다. QueryDslRepository는"
                                            + " QueryAdapter 전용입니다 (PER-ADP-001)");

            rule.check(commandAdapterClasses);
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
        void commandAdapter_MustNotBeTransactional() {
            ArchRule classRule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("CommandAdapter")
                            .and()
                            .resideInAPackage(ADAPTER_ALL)
                            .should()
                            .notBeAnnotatedWith(Transactional.class)
                            .allowEmptyShould(true)
                            .because("Command Adapter에 @Transactional 사용 금지 (PER-ADP-002)");

            ArchRule methodRule =
                    methods()
                            .that()
                            .areDeclaredInClassesThat()
                            .haveSimpleNameEndingWith("CommandAdapter")
                            .should()
                            .notBeAnnotatedWith(Transactional.class)
                            .allowEmptyShould(true)
                            .because("Command Adapter 메서드에 @Transactional 사용 금지 (PER-ADP-002)");

            classRule.check(commandAdapterClasses);
            methodRule.check(commandAdapterClasses);
        }

        @Test
        @DisplayName("규칙 3-2: 비즈니스 로직 포함이 금지됩니다")
        void commandAdapter_MustNotContainBusinessLogic() {
            ArchRule rule =
                    noClasses()
                            .that()
                            .haveSimpleNameEndingWith("CommandAdapter")
                            .and()
                            .resideInAPackage(ADAPTER_ALL)
                            .should()
                            .dependOnClassesThat()
                            .haveSimpleNameEndingWith("Service")
                            .allowEmptyShould(true)
                            .because("Command Adapter는 비즈니스 로직을 포함하지 않아야 합니다");

            rule.check(commandAdapterClasses);
        }
    }
}
