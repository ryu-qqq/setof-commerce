package com.ryuqq.setof.adapter.out.persistence.architecture.entity;

import static com.ryuqq.setof.adapter.out.persistence.architecture.ArchUnitPackageConstants.*;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * JpaEntityArchTest - JPA Entity 아키텍처 규칙 검증.
 *
 * <p>PER-ENT-001: Entity는 @Entity, @Table 어노테이션 필수.
 *
 * <p>PER-ENT-002: JPA 관계 어노테이션 금지 (@OneToMany, @ManyToOne 등).
 *
 * <p>PER-ENT-003: ID 필드는 @GeneratedValue(strategy = IDENTITY).
 *
 * <p>PER-ENT-004: Lombok 사용 금지 - 수동 Getter/생성자.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("JpaEntity 아키텍처 규칙 검증")
class JpaEntityArchTest {

    private static JavaClasses allClasses;
    private static JavaClasses jpaEntityClasses;

    @BeforeAll
    static void setUp() {
        allClasses =
                new ClassFileImporter()
                        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                        .importPackages(BASE);

        jpaEntityClasses =
                allClasses.that(
                        DescribedPredicate.describe(
                                "JpaEntity 클래스 (QueryDSL Q* 클래스 제외)",
                                javaClass ->
                                        javaClass.getSimpleName().endsWith("JpaEntity")
                                                && !javaClass.getSimpleName().startsWith("Q")
                                                && !javaClass.isInterface()));
    }

    // ========================================================================
    // 1. 클래스 구조 규칙
    // ========================================================================

    @Nested
    @DisplayName("1. 클래스 구조 규칙")
    class ClassStructureRules {

        @Test
        @DisplayName("규칙 1-1: @Entity 어노테이션이 필수입니다 (PER-ENT-001)")
        void jpaEntity_MustHaveEntityAnnotation() {
            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("JpaEntity")
                            .and()
                            .resideInAPackage(ENTITY_ALL)
                            .should()
                            .beAnnotatedWith(Entity.class)
                            .allowEmptyShould(true)
                            .because("JPA Entity는 @Entity 어노테이션이 필수입니다 (PER-ENT-001)");

            rule.check(jpaEntityClasses);
        }

        @Test
        @DisplayName("규칙 1-2: @Table 어노테이션이 필수입니다 (PER-ENT-001)")
        void jpaEntity_MustHaveTableAnnotation() {
            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("JpaEntity")
                            .and()
                            .resideInAPackage(ENTITY_ALL)
                            .should()
                            .beAnnotatedWith(Table.class)
                            .allowEmptyShould(true)
                            .because("JPA Entity는 @Table 어노테이션이 필수입니다 (PER-ENT-001)");

            rule.check(jpaEntityClasses);
        }

        @Test
        @DisplayName("규칙 1-3: @Id 필드가 필수입니다 (PER-ENT-003)")
        void jpaEntity_MustHaveIdField() {
            ArchCondition<JavaClass> haveIdAnnotatedField =
                    new ArchCondition<>("@Id 어노테이션이 있는 필드가 존재") {
                        @Override
                        public void check(JavaClass javaClass, ConditionEvents events) {
                            boolean hasIdField =
                                    javaClass.getAllFields().stream()
                                            .anyMatch(field -> field.isAnnotatedWith(Id.class));

                            if (!hasIdField) {
                                events.add(
                                        SimpleConditionEvent.violated(
                                                javaClass, javaClass.getName() + "에 @Id 필드가 없습니다"));
                            }
                        }
                    };

            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("JpaEntity")
                            .and()
                            .resideInAPackage(ENTITY_ALL)
                            .should(haveIdAnnotatedField)
                            .allowEmptyShould(true)
                            .because("JPA Entity는 @Id 필드가 필수입니다 (PER-ENT-003)");

            rule.check(jpaEntityClasses);
        }

        @Test
        @DisplayName("규칙 1-4: @GeneratedValue가 필수입니다 (PER-ENT-003)")
        void jpaEntity_MustHaveGeneratedValue() {
            ArchCondition<JavaClass> haveGeneratedValueAnnotatedField =
                    new ArchCondition<>("@GeneratedValue 어노테이션이 있는 필드가 존재") {
                        @Override
                        public void check(JavaClass javaClass, ConditionEvents events) {
                            boolean hasGeneratedValueField =
                                    javaClass.getAllFields().stream()
                                            .anyMatch(
                                                    field ->
                                                            field.isAnnotatedWith(
                                                                    GeneratedValue.class));

                            if (!hasGeneratedValueField) {
                                events.add(
                                        SimpleConditionEvent.violated(
                                                javaClass,
                                                javaClass.getName()
                                                        + "에 @GeneratedValue 필드가 없습니다"));
                            }
                        }
                    };

            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("JpaEntity")
                            .and()
                            .resideInAPackage(ENTITY_ALL)
                            .should(haveGeneratedValueAnnotatedField)
                            .allowEmptyShould(true)
                            .because("JPA Entity는 @GeneratedValue가 필수입니다 (PER-ENT-003)");

            rule.check(jpaEntityClasses);
        }
    }

    // ========================================================================
    // 2. 관계 매핑 금지 규칙
    // ========================================================================

    @Nested
    @DisplayName("2. 관계 매핑 금지 규칙")
    class RelationshipProhibitionRules {

        @Test
        @DisplayName("규칙 2-1: @OneToMany 사용이 금지됩니다 (PER-ENT-002)")
        void jpaEntity_MustNotUseOneToMany() {
            ArchCondition<JavaClass> notHaveOneToManyField =
                    new ArchCondition<>("@OneToMany 어노테이션이 있는 필드가 없어야 함") {
                        @Override
                        public void check(JavaClass javaClass, ConditionEvents events) {
                            for (JavaField field : javaClass.getAllFields()) {
                                if (field.isAnnotatedWith(OneToMany.class)) {
                                    events.add(
                                            SimpleConditionEvent.violated(
                                                    javaClass,
                                                    javaClass.getName()
                                                            + "의 "
                                                            + field.getName()
                                                            + " 필드에 @OneToMany 사용 금지"));
                                }
                            }
                        }
                    };

            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("JpaEntity")
                            .and()
                            .resideInAPackage(ENTITY_ALL)
                            .should(notHaveOneToManyField)
                            .allowEmptyShould(true)
                            .because("JPA Entity에서 @OneToMany 사용 금지 (PER-ENT-002)");

            rule.check(jpaEntityClasses);
        }

        @Test
        @DisplayName("규칙 2-2: @ManyToOne 사용이 금지됩니다 (PER-ENT-002)")
        void jpaEntity_MustNotUseManyToOne() {
            ArchCondition<JavaClass> notHaveManyToOneField =
                    new ArchCondition<>("@ManyToOne 어노테이션이 있는 필드가 없어야 함") {
                        @Override
                        public void check(JavaClass javaClass, ConditionEvents events) {
                            for (JavaField field : javaClass.getAllFields()) {
                                if (field.isAnnotatedWith(ManyToOne.class)) {
                                    events.add(
                                            SimpleConditionEvent.violated(
                                                    javaClass,
                                                    javaClass.getName()
                                                            + "의 "
                                                            + field.getName()
                                                            + " 필드에 @ManyToOne 사용 금지"));
                                }
                            }
                        }
                    };

            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("JpaEntity")
                            .and()
                            .resideInAPackage(ENTITY_ALL)
                            .should(notHaveManyToOneField)
                            .allowEmptyShould(true)
                            .because("JPA Entity에서 @ManyToOne 사용 금지 (PER-ENT-002)");

            rule.check(jpaEntityClasses);
        }

        @Test
        @DisplayName("규칙 2-3: @OneToOne 사용이 금지됩니다 (PER-ENT-002)")
        void jpaEntity_MustNotUseOneToOne() {
            ArchCondition<JavaClass> notHaveOneToOneField =
                    new ArchCondition<>("@OneToOne 어노테이션이 있는 필드가 없어야 함") {
                        @Override
                        public void check(JavaClass javaClass, ConditionEvents events) {
                            for (JavaField field : javaClass.getAllFields()) {
                                if (field.isAnnotatedWith(OneToOne.class)) {
                                    events.add(
                                            SimpleConditionEvent.violated(
                                                    javaClass,
                                                    javaClass.getName()
                                                            + "의 "
                                                            + field.getName()
                                                            + " 필드에 @OneToOne 사용 금지"));
                                }
                            }
                        }
                    };

            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("JpaEntity")
                            .and()
                            .resideInAPackage(ENTITY_ALL)
                            .should(notHaveOneToOneField)
                            .allowEmptyShould(true)
                            .because("JPA Entity에서 @OneToOne 사용 금지 (PER-ENT-002)");

            rule.check(jpaEntityClasses);
        }

        @Test
        @DisplayName("규칙 2-4: @ManyToMany 사용이 금지됩니다 (PER-ENT-002)")
        void jpaEntity_MustNotUseManyToMany() {
            ArchCondition<JavaClass> notHaveManyToManyField =
                    new ArchCondition<>("@ManyToMany 어노테이션이 있는 필드가 없어야 함") {
                        @Override
                        public void check(JavaClass javaClass, ConditionEvents events) {
                            for (JavaField field : javaClass.getAllFields()) {
                                if (field.isAnnotatedWith(ManyToMany.class)) {
                                    events.add(
                                            SimpleConditionEvent.violated(
                                                    javaClass,
                                                    javaClass.getName()
                                                            + "의 "
                                                            + field.getName()
                                                            + " 필드에 @ManyToMany 사용 금지"));
                                }
                            }
                        }
                    };

            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("JpaEntity")
                            .and()
                            .resideInAPackage(ENTITY_ALL)
                            .should(notHaveManyToManyField)
                            .allowEmptyShould(true)
                            .because("JPA Entity에서 @ManyToMany 사용 금지 (PER-ENT-002)");

            rule.check(jpaEntityClasses);
        }
    }

    // ========================================================================
    // 3. Lombok 금지 규칙
    // ========================================================================

    @Nested
    @DisplayName("3. Lombok 금지 규칙")
    class LombokProhibitionRules {

        @Test
        @DisplayName("규칙 3-1: Lombok 의존성이 금지됩니다 (PER-ENT-004)")
        void jpaEntity_MustNotUseLombok() {
            ArchRule rule =
                    noClasses()
                            .that()
                            .haveSimpleNameEndingWith("JpaEntity")
                            .and()
                            .resideInAPackage(ENTITY_ALL)
                            .should()
                            .dependOnClassesThat()
                            .resideInAPackage("lombok..")
                            .allowEmptyShould(true)
                            .because("JPA Entity에서 Lombok 사용 금지 (PER-ENT-004)");

            rule.check(jpaEntityClasses);
        }
    }

    // ========================================================================
    // 4. 네이밍 규칙
    // ========================================================================

    @Nested
    @DisplayName("4. 네이밍 규칙")
    class NamingRules {

        @Test
        @DisplayName("규칙 4-1: Entity는 entity 패키지에 위치해야 합니다")
        void jpaEntity_MustResideInEntityPackage() {
            ArchRule rule =
                    classes()
                            .that()
                            .haveSimpleNameEndingWith("JpaEntity")
                            .should()
                            .resideInAPackage(ENTITY_ALL)
                            .allowEmptyShould(true)
                            .because("JPA Entity는 entity 패키지에 위치해야 합니다");

            rule.check(jpaEntityClasses);
        }
    }
}
