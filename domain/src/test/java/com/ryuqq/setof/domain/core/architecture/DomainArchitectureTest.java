package com.ryuqq.setof.domain.core.architecture;

import com.ryuqq.setof.domain.core.exception.DomainException;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@DisplayName("Domain Layer 아키텍처 규칙")
class DomainArchitectureTest {

    private static JavaClasses domainClasses;

    @BeforeAll
    static void setUp() {
        domainClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.ryuqq.setof.domain.core");
    }

    @Nested
    @DisplayName("Value Object 규칙")
    class ValueObjectRules {

        @Test
        @DisplayName("VO 패키지의 record 클래스는 불변이어야 한다")
        void voRecordsShouldBeImmutable() {
            classes()
                    .that().resideInAPackage("..vo..")
                    .and().areRecords()
                    .should().haveModifier(com.tngtech.archunit.core.domain.JavaModifier.FINAL)
                    .because("Value Objects는 불변성을 보장해야 합니다")
                    .check(domainClasses);
        }

        @Test
        @DisplayName("VO 패키지의 비 Enum 클래스는 record여야 한다")
        void nonEnumVosShouldBeRecords() {
            classes()
                    .that().resideInAPackage("..vo..")
                    .and().areNotEnums()
                    .and().areNotInterfaces()
                    .and().haveSimpleNameNotEndingWith("Fixture")
                    .should().beRecords()
                    .because("Value Objects는 불변성을 보장하기 위해 record로 선언해야 합니다")
                    .check(domainClasses);
        }
    }

    @Nested
    @DisplayName("Exception 규칙")
    class ExceptionRules {

        @Test
        @DisplayName("Member 예외 패키지의 모든 예외는 DomainException을 상속해야 한다")
        void memberExceptionsShouldExtendDomainException() {
            classes()
                    .that().resideInAPackage("..member.exception..")
                    .and().areNotInterfaces()
                    .should().beAssignableTo(DomainException.class)
                    .because("모든 도메인 예외는 DomainException을 상속해야 합니다")
                    .check(domainClasses);
        }

        @Test
        @DisplayName("예외 클래스는 final이어야 한다")
        void exceptionsShouldBeFinal() {
            classes()
                    .that().resideInAPackage("..exception..")
                    .and().areNotAssignableFrom(DomainException.class)
                    .should().haveModifier(com.tngtech.archunit.core.domain.JavaModifier.FINAL)
                    .because("예외 클래스는 상속을 방지하기 위해 final이어야 합니다")
                    .check(domainClasses);
        }
    }

    @Nested
    @DisplayName("Lombok 금지 규칙")
    class NoLombokRules {

        @Test
        @DisplayName("Domain 클래스는 Lombok 어노테이션을 사용하면 안 된다")
        void domainShouldNotUseLombok() {
            noClasses()
                    .that().resideInAPackage("com.ryuqq.setof.domain.core..")
                    .should().dependOnClassesThat().resideInAPackage("lombok..")
                    .because("Domain Layer에서 Lombok 사용은 금지되어 있습니다 (Zero-Tolerance)")
                    .check(domainClasses);
        }
    }

    @Nested
    @DisplayName("Aggregate 규칙")
    class AggregateRules {

        @Test
        @DisplayName("Aggregate 패키지의 클래스는 다른 레이어에 의존하면 안 된다")
        void aggregatesShouldNotDependOnOtherLayers() {
            noClasses()
                    .that().resideInAPackage("..aggregate..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "..application..",
                            "..storage..",
                            "..api.."
                    )
                    .because("Aggregate는 다른 레이어에 의존하면 안 됩니다")
                    .check(domainClasses);
        }
    }

    @Nested
    @DisplayName("Enum 규칙")
    class EnumRules {

        @Test
        @DisplayName("VO 패키지의 Enum은 Enum이어야 한다 (명시적 검증)")
        void voEnumsShouldBeEnums() {
            classes()
                    .that().resideInAPackage("..vo..")
                    .and().areEnums()
                    .should().beEnums()
                    .because("Enum은 Enum으로 선언되어야 합니다")
                    .check(domainClasses);
        }
    }

    @Nested
    @DisplayName("패키지 구조 규칙")
    class PackageStructureRules {

        @Test
        @DisplayName("Domain 클래스는 Spring에 의존하면 안 된다")
        void domainShouldNotDependOnSpring() {
            noClasses()
                    .that().resideInAPackage("com.ryuqq.setof.domain.core..")
                    .should().dependOnClassesThat().resideInAPackage("org.springframework..")
                    .because("Domain Layer는 프레임워크에 독립적이어야 합니다")
                    .check(domainClasses);
        }
    }
}
