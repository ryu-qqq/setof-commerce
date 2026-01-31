package com.ryuqq.setof.integration.test.repository.sellerapplication;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.out.persistence.sellerapplication.SellerApplicationJpaEntityFixtures;
import com.ryuqq.setof.adapter.out.persistence.sellerapplication.entity.SellerApplicationJpaEntity;
import com.ryuqq.setof.adapter.out.persistence.sellerapplication.entity.SellerApplicationJpaEntity.AddressTypeJpaValue;
import com.ryuqq.setof.adapter.out.persistence.sellerapplication.entity.SellerApplicationJpaEntity.ApplicationStatusJpaValue;
import com.ryuqq.setof.adapter.out.persistence.sellerapplication.repository.SellerApplicationJpaRepository;
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
 * SellerApplication JPA Repository 통합 테스트.
 *
 * <p>JPA Repository의 기본 CRUD 동작을 검증합니다. QueryDslRepository는 E2E 테스트에서 간접적으로 검증됩니다.
 */
@Tag(TestTags.SELLER)
@DisplayName("셀러 입점 신청 JPA Repository 테스트")
class SellerApplicationRepositoryTest extends RepositoryTestBase {

    @Autowired private SellerApplicationJpaRepository jpaRepository;

    @Nested
    @DisplayName("save 테스트")
    class SaveTest {

        @Test
        @DisplayName("대기 상태 입점 신청 저장 성공")
        void shouldSavePendingApplication() {
            // given
            SellerApplicationJpaEntity entity = SellerApplicationJpaEntityFixtures.pendingEntity();

            // when
            SellerApplicationJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            assertThat(saved.getId()).isNotNull();

            Optional<SellerApplicationJpaEntity> found = jpaRepository.findById(saved.getId());
            assertThat(found).isPresent();
            assertThat(found.get().getSellerName()).isEqualTo("테스트셀러");
            assertThat(found.get().getStatus()).isEqualTo(ApplicationStatusJpaValue.PENDING);
        }

        @Test
        @DisplayName("승인 상태 입점 신청 저장 성공")
        void shouldSaveApprovedApplication() {
            // given
            SellerApplicationJpaEntity entity =
                    SellerApplicationJpaEntityFixtures.approvedEntity(100L);

            // when
            SellerApplicationJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            Optional<SellerApplicationJpaEntity> found = jpaRepository.findById(saved.getId());
            assertThat(found).isPresent();
            assertThat(found.get().getStatus()).isEqualTo(ApplicationStatusJpaValue.APPROVED);
            assertThat(found.get().getApprovedSellerId()).isEqualTo(100L);
            assertThat(found.get().getProcessedBy()).isNotNull();
        }

        @Test
        @DisplayName("거절 상태 입점 신청 저장 성공")
        void shouldSaveRejectedApplication() {
            // given
            SellerApplicationJpaEntity entity =
                    SellerApplicationJpaEntityFixtures.rejectedEntity("서류 불충분");

            // when
            SellerApplicationJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            Optional<SellerApplicationJpaEntity> found = jpaRepository.findById(saved.getId());
            assertThat(found).isPresent();
            assertThat(found.get().getStatus()).isEqualTo(ApplicationStatusJpaValue.REJECTED);
            assertThat(found.get().getRejectionReason()).isEqualTo("서류 불충분");
        }

        @Test
        @DisplayName("모든 필드가 정확히 저장되는지 검증")
        void shouldSaveAllFieldsCorrectly() {
            // given
            SellerApplicationJpaEntity entity = SellerApplicationJpaEntityFixtures.pendingEntity();

            // when
            SellerApplicationJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            SellerApplicationJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();

            assertThat(found.getSellerName())
                    .isEqualTo(SellerApplicationJpaEntityFixtures.DEFAULT_SELLER_NAME);
            assertThat(found.getDisplayName())
                    .isEqualTo(SellerApplicationJpaEntityFixtures.DEFAULT_DISPLAY_NAME);
            assertThat(found.getLogoUrl())
                    .isEqualTo(SellerApplicationJpaEntityFixtures.DEFAULT_LOGO_URL);
            assertThat(found.getDescription())
                    .isEqualTo(SellerApplicationJpaEntityFixtures.DEFAULT_DESCRIPTION);
            assertThat(found.getRegistrationNumber())
                    .isEqualTo(SellerApplicationJpaEntityFixtures.DEFAULT_REGISTRATION_NUMBER);
            assertThat(found.getCompanyName())
                    .isEqualTo(SellerApplicationJpaEntityFixtures.DEFAULT_COMPANY_NAME);
            assertThat(found.getRepresentative())
                    .isEqualTo(SellerApplicationJpaEntityFixtures.DEFAULT_REPRESENTATIVE);
            assertThat(found.getCsPhoneNumber())
                    .isEqualTo(SellerApplicationJpaEntityFixtures.DEFAULT_CS_PHONE);
            assertThat(found.getCsEmail())
                    .isEqualTo(SellerApplicationJpaEntityFixtures.DEFAULT_CS_EMAIL);
            assertThat(found.getAddressType()).isEqualTo(AddressTypeJpaValue.RETURN);
            assertThat(found.getAddressName())
                    .isEqualTo(SellerApplicationJpaEntityFixtures.DEFAULT_ADDRESS_NAME);
            assertThat(found.getAgreedAt()).isNotNull();
            assertThat(found.getAppliedAt()).isNotNull();
        }

        @Test
        @DisplayName("여러 엔티티 일괄 저장 성공")
        void shouldSaveAllEntities() {
            // given
            List<SellerApplicationJpaEntity> entities =
                    List.of(
                            SellerApplicationJpaEntityFixtures.pendingEntityWithRegistrationNumber(
                                    "111-11-11111"),
                            SellerApplicationJpaEntityFixtures.pendingEntityWithRegistrationNumber(
                                    "222-22-22222"),
                            SellerApplicationJpaEntityFixtures.pendingEntityWithRegistrationNumber(
                                    "333-33-33333"));

            // when
            List<SellerApplicationJpaEntity> saved = jpaRepository.saveAll(entities);
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
        @DisplayName("ID로 입점 신청 조회 성공")
        void shouldFindById() {
            // given
            SellerApplicationJpaEntity entity =
                    jpaRepository.save(SellerApplicationJpaEntityFixtures.pendingEntity());
            flushAndClear();

            // when
            Optional<SellerApplicationJpaEntity> found = jpaRepository.findById(entity.getId());

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getSellerName()).isEqualTo("테스트셀러");
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회시 빈 Optional 반환")
        void shouldReturnEmptyWhenNotFound() {
            // when
            Optional<SellerApplicationJpaEntity> found = jpaRepository.findById(999999L);

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
            SellerApplicationJpaEntity entity =
                    jpaRepository.save(SellerApplicationJpaEntityFixtures.pendingEntity());
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
            boolean exists = jpaRepository.existsById(999999L);

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
            jpaRepository.save(SellerApplicationJpaEntityFixtures.pendingEntity());
            jpaRepository.save(SellerApplicationJpaEntityFixtures.approvedEntity(100L));
            jpaRepository.save(SellerApplicationJpaEntityFixtures.rejectedEntity());
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
            SellerApplicationJpaEntity entity =
                    jpaRepository.save(SellerApplicationJpaEntityFixtures.pendingEntity());
            Long id = entity.getId();
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
            jpaRepository.save(SellerApplicationJpaEntityFixtures.pendingEntity());
            jpaRepository.save(SellerApplicationJpaEntityFixtures.approvedEntity(100L));
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
        @DisplayName("주소 타입 SHIPPING 저장 및 조회")
        void shouldSaveAndRetrieveShippingAddressType() {
            // given
            SellerApplicationJpaEntity entity =
                    SellerApplicationJpaEntityFixtures.pendingEntityWithShippingAddress();

            // when
            SellerApplicationJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            SellerApplicationJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();
            assertThat(found.getAddressType()).isEqualTo(AddressTypeJpaValue.SHIPPING);
            assertThat(found.getAddressName()).isEqualTo("출고지");
        }

        @Test
        @DisplayName("긴 텍스트 필드 저장 가능 확인")
        void shouldSaveLongTextFields() {
            // given
            SellerApplicationJpaEntity entity =
                    SellerApplicationJpaEntityFixtures.pendingEntityWithName("테스트셀러", "테스트컴퍼니");

            // when
            SellerApplicationJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            SellerApplicationJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();
            assertThat(found).isNotNull();
        }

        @Test
        @DisplayName("null 허용 필드 확인")
        void shouldAllowNullableFields() {
            // given
            SellerApplicationJpaEntity entity = SellerApplicationJpaEntityFixtures.pendingEntity();

            // when
            SellerApplicationJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            SellerApplicationJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();
            assertThat(found.getProcessedAt()).isNull();
            assertThat(found.getProcessedBy()).isNull();
            assertThat(found.getRejectionReason()).isNull();
            assertThat(found.getApprovedSellerId()).isNull();
        }

        @Test
        @DisplayName("Audit 필드 자동 설정 확인")
        void shouldSetAuditFieldsAutomatically() {
            // given
            SellerApplicationJpaEntity entity = SellerApplicationJpaEntityFixtures.pendingEntity();

            // when
            SellerApplicationJpaEntity saved = jpaRepository.save(entity);
            flushAndClear();

            // then
            SellerApplicationJpaEntity found = jpaRepository.findById(saved.getId()).orElseThrow();
            assertThat(found.getCreatedAt()).isNotNull();
            assertThat(found.getUpdatedAt()).isNotNull();
        }
    }
}
