package com.ryuqq.setof.integration.test.repository.selleradmin;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.selleradmin.SellerAdminJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.selleradmin.entity.SellerAdminJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.selleradmin.repository.SellerAdminJpaRepository;
import com.ryuqq.setof.domain.selleradmin.vo.SellerAdminStatus;
import com.ryuqq.setof.integration.test.common.base.RepositoryTestBase;
import com.ryuqq.setof.integration.test.common.tag.TestTags;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * SellerAdmin JPA Repository 통합 테스트.
 *
 * <p>JPA Repository의 기본 CRUD 동작을 검증합니다.
 */
@Tag(TestTags.SELLER_ADMIN)
@DisplayName("셀러 관리자 JPA Repository 테스트")
class SellerAdminRepositoryTest extends RepositoryTestBase {

    @Autowired private SellerAdminJpaRepository jpaRepository;

    @Nested
    @DisplayName("save 테스트")
    class SaveTest {

        @Test
        @DisplayName("활성 상태 셀러 관리자 저장 성공")
        void shouldSaveActiveEntity() {
            // given
            SellerAdminJpaEntity entity = SellerAdminJpaEntityFixtures.activeEntity();

            // when
            SellerAdminJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            assertThat(saved.getId()).isNotNull();

            Optional<SellerAdminJpaEntity> found = jpaRepository.findById(saved.getId());
            assertThat(found).isPresent();
            assertThat(found.get().getLoginId())
                    .isEqualTo(SellerAdminJpaEntityFixtures.DEFAULT_LOGIN_ID);
            assertThat(found.get().getStatus()).isEqualTo(SellerAdminStatus.ACTIVE);
        }

        @Test
        @DisplayName("모든 필드가 정확히 저장되는지 검증")
        void shouldSaveAllFieldsCorrectly() {
            // given
            SellerAdminJpaEntity entity = SellerAdminJpaEntityFixtures.activeEntity();

            // when
            SellerAdminJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            SellerAdminJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();

            assertThat(found.getSellerId())
                    .isEqualTo(SellerAdminJpaEntityFixtures.DEFAULT_SELLER_ID);
            assertThat(found.getAuthUserId())
                    .isEqualTo(SellerAdminJpaEntityFixtures.DEFAULT_AUTH_USER_ID);
            assertThat(found.getLoginId()).isEqualTo(SellerAdminJpaEntityFixtures.DEFAULT_LOGIN_ID);
            assertThat(found.getName()).isEqualTo(SellerAdminJpaEntityFixtures.DEFAULT_NAME);
            assertThat(found.getPhoneNumber())
                    .isEqualTo(SellerAdminJpaEntityFixtures.DEFAULT_PHONE_NUMBER);
            assertThat(found.getStatus()).isEqualTo(SellerAdminStatus.ACTIVE);
            assertThat(found.getCreatedAt()).isNotNull();
            assertThat(found.getUpdatedAt()).isNotNull();
            assertThat(found.getDeletedAt()).isNull();
        }

        @Test
        @DisplayName("여러 엔티티 일괄 저장 성공")
        void shouldSaveAllEntities() {
            // given
            List<SellerAdminJpaEntity> entities =
                    List.of(
                            SellerAdminJpaEntityFixtures.newActiveEntityWithLoginId(
                                    "01956f4a-0001-7d8e-9f0a-1b2c3d4e5f60", "admin1@test.com"),
                            SellerAdminJpaEntityFixtures.newActiveEntityWithLoginId(
                                    "01956f4a-0002-7d8e-9f0a-1b2c3d4e5f60", "admin2@test.com"),
                            SellerAdminJpaEntityFixtures.newActiveEntityWithLoginId(
                                    "01956f4a-0003-7d8e-9f0a-1b2c3d4e5f60", "admin3@test.com"));

            // when
            List<SellerAdminJpaEntity> saved = jpaRepository.saveAll(entities);
            flushAndClear();

            // then
            assertThat(saved).hasSize(3);
            assertThat(jpaRepository.count()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("findById 테스트")
    class FindByIdTest {

        @Test
        @DisplayName("ID로 셀러 관리자 조회 성공")
        void shouldFindById() {
            // given
            SellerAdminJpaEntity entity =
                    jpaRepository.save(SellerAdminJpaEntityFixtures.activeEntity());
            flushAndClear();

            // when
            Optional<SellerAdminJpaEntity> found = jpaRepository.findById(entity.getId());

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getLoginId())
                    .isEqualTo(SellerAdminJpaEntityFixtures.DEFAULT_LOGIN_ID);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회시 빈 Optional 반환")
        void shouldReturnEmptyWhenNotFound() {
            // when
            Optional<SellerAdminJpaEntity> found =
                    jpaRepository.findById("non-existing-id-12345678");

            // then
            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("existsById 테스트")
    class ExistsByIdTest {

        @Test
        @DisplayName("존재하는 ID는 true 반환")
        void shouldReturnTrueWhenExists() {
            // given
            SellerAdminJpaEntity entity =
                    jpaRepository.save(SellerAdminJpaEntityFixtures.activeEntity());
            flushAndClear();

            // when
            boolean exists = jpaRepository.existsById(entity.getId());

            // then
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("존재하지 않는 ID는 false 반환")
        void shouldReturnFalseWhenNotExists() {
            // when
            boolean exists = jpaRepository.existsById("non-existing-id-12345678");

            // then
            assertThat(exists).isFalse();
        }
    }

    @Nested
    @DisplayName("count 테스트")
    class CountTest {

        @Test
        @DisplayName("전체 개수 카운트")
        void shouldCountAll() {
            // given
            jpaRepository.save(
                    SellerAdminJpaEntityFixtures.newActiveEntityWithLoginId(
                            "01956f4a-0001-7d8e-9f0a-1b2c3d4e5f60", "admin1@test.com"));
            jpaRepository.save(
                    SellerAdminJpaEntityFixtures.newActiveEntityWithLoginId(
                            "01956f4a-0002-7d8e-9f0a-1b2c3d4e5f60", "admin2@test.com"));
            jpaRepository.save(
                    SellerAdminJpaEntityFixtures.newActiveEntityWithLoginId(
                            "01956f4a-0003-7d8e-9f0a-1b2c3d4e5f60", "admin3@test.com"));
            flushAndClear();

            // when
            long count = jpaRepository.count();

            // then
            assertThat(count).isEqualTo(3);
        }

        @Test
        @DisplayName("빈 테이블 카운트")
        void shouldReturnZeroWhenEmpty() {
            // when
            long count = jpaRepository.count();

            // then
            assertThat(count).isZero();
        }
    }

    @Nested
    @DisplayName("delete 테스트")
    class DeleteTest {

        @Test
        @DisplayName("엔티티 삭제 성공")
        void shouldDeleteEntity() {
            // given
            SellerAdminJpaEntity entity =
                    jpaRepository.save(SellerAdminJpaEntityFixtures.activeEntity());
            String id = entity.getId();
            flushAndClear();

            // when
            jpaRepository.deleteById(id);
            flushAndClear();

            // then
            assertThat(jpaRepository.existsById(id)).isFalse();
        }

        @Test
        @DisplayName("전체 삭제 성공")
        void shouldDeleteAll() {
            // given
            jpaRepository.save(
                    SellerAdminJpaEntityFixtures.newActiveEntityWithLoginId(
                            "01956f4a-0001-7d8e-9f0a-1b2c3d4e5f60", "admin1@test.com"));
            jpaRepository.save(
                    SellerAdminJpaEntityFixtures.newActiveEntityWithLoginId(
                            "01956f4a-0002-7d8e-9f0a-1b2c3d4e5f60", "admin2@test.com"));
            flushAndClear();

            // when
            jpaRepository.deleteAll();
            flushAndClear();

            // then
            assertThat(jpaRepository.count()).isZero();
        }
    }

    @Nested
    @DisplayName("엔티티 필드 검증 테스트")
    class EntityFieldValidationTest {

        @Test
        @DisplayName("핸드폰 번호 없이 저장 가능 확인")
        void shouldSaveWithoutPhoneNumber() {
            // given
            SellerAdminJpaEntity entity = SellerAdminJpaEntityFixtures.entityWithoutPhoneNumber();

            // when
            SellerAdminJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            SellerAdminJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();
            assertThat(found.getPhoneNumber()).isNull();
        }

        @Test
        @DisplayName("인증 사용자 ID 없이 저장 가능 확인 (승인 전)")
        void shouldSaveWithoutAuthUserId() {
            // given
            SellerAdminJpaEntity entity = SellerAdminJpaEntityFixtures.entityWithoutAuthUserId();

            // when
            SellerAdminJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            SellerAdminJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();
            assertThat(found.getAuthUserId()).isNull();
        }

        @Test
        @DisplayName("승인 대기 상태 저장 및 조회")
        void shouldSaveAndRetrievePendingApprovalEntity() {
            // given
            SellerAdminJpaEntity entity =
                    SellerAdminJpaEntityFixtures.pendingApprovalEntity(
                            "01956f4a-9999-7d8e-9f0a-1b2c3d4e5f60", "pending-unique@test.com");

            // when
            SellerAdminJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            SellerAdminJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();
            assertThat(found.getStatus()).isEqualTo(SellerAdminStatus.PENDING_APPROVAL);
            assertThat(found.getAuthUserId()).isNull();
        }

        @Test
        @DisplayName("거절 상태 저장 및 조회")
        void shouldSaveAndRetrieveRejectedEntity() {
            // given
            SellerAdminJpaEntity entity = SellerAdminJpaEntityFixtures.rejectedEntity();

            // when
            SellerAdminJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            SellerAdminJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();
            assertThat(found.getStatus()).isEqualTo(SellerAdminStatus.REJECTED);
        }

        @Test
        @DisplayName("정지 상태 저장 및 조회")
        void shouldSaveAndRetrieveSuspendedEntity() {
            // given
            SellerAdminJpaEntity entity = SellerAdminJpaEntityFixtures.suspendedEntity();

            // when
            SellerAdminJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            SellerAdminJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();
            assertThat(found.getStatus()).isEqualTo(SellerAdminStatus.SUSPENDED);
        }

        @Test
        @DisplayName("삭제 상태 저장 및 조회")
        void shouldSaveAndRetrieveDeletedEntity() {
            // given
            SellerAdminJpaEntity entity =
                    SellerAdminJpaEntityFixtures.deletedEntity(
                            "01956f4a-8888-7d8e-9f0a-1b2c3d4e5f60", "deleted-unique@test.com");

            // when
            SellerAdminJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            SellerAdminJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();
            assertThat(found.getDeletedAt()).isNotNull();
        }

        @Test
        @DisplayName("Audit 필드 자동 설정 확인")
        void shouldSetAuditFieldsAutomatically() {
            // given
            SellerAdminJpaEntity entity = SellerAdminJpaEntityFixtures.activeEntity();

            // when
            SellerAdminJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            SellerAdminJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();
            assertThat(found.getCreatedAt()).isNotNull();
            assertThat(found.getUpdatedAt()).isNotNull();
        }
    }
}
