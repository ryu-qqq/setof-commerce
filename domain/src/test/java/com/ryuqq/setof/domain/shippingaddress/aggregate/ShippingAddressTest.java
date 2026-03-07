package com.ryuqq.setof.domain.shippingaddress.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.common.vo.DeletionStatus;
import com.ryuqq.setof.domain.member.id.MemberId;
import com.ryuqq.setof.domain.member.vo.LegacyMemberId;
import com.ryuqq.setof.domain.shippingaddress.id.ShippingAddressId;
import com.ryuqq.setof.domain.shippingaddress.vo.Country;
import com.ryuqq.setof.domain.shippingaddress.vo.DeliveryRequest;
import com.ryuqq.setof.domain.shippingaddress.vo.ReceiverName;
import com.ryuqq.setof.domain.shippingaddress.vo.ShippingAddressName;
import com.setof.commerce.domain.shippingaddress.ShippingAddressFixtures;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("ShippingAddress Aggregate 단위 테스트")
class ShippingAddressTest {

    @Nested
    @DisplayName("forNew() - 신규 배송지 생성 (새 스키마)")
    class ForNewTest {

        @Test
        @DisplayName("MemberId로 신규 배송지를 생성한다")
        void createNewShippingAddressWithMemberId() {
            // given
            MemberId memberId = ShippingAddressFixtures.defaultMemberId();
            ReceiverName receiverName = ShippingAddressFixtures.defaultReceiverName();
            ShippingAddressName name = ShippingAddressFixtures.defaultShippingAddressName();
            Instant now = CommonVoFixtures.now();

            // when
            ShippingAddress address =
                    ShippingAddress.forNew(
                            memberId,
                            receiverName,
                            name,
                            ShippingAddressFixtures.defaultAddress(),
                            ShippingAddressFixtures.defaultCountry(),
                            ShippingAddressFixtures.defaultDeliveryRequest(),
                            CommonVoFixtures.defaultPhoneNumber(),
                            false,
                            now);

            // then
            assertThat(address).isNotNull();
            assertThat(address.isNew()).isTrue();
            assertThat(address.memberId()).isEqualTo(memberId);
            assertThat(address.legacyMemberId()).isNull();
            assertThat(address.receiverName()).isEqualTo(receiverName);
            assertThat(address.shippingAddressName()).isEqualTo(name);
            assertThat(address.country()).isEqualTo(Country.KR);
            assertThat(address.isDefault()).isFalse();
            assertThat(address.isDeleted()).isFalse();
            assertThat(address.createdAt()).isEqualTo(now);
            assertThat(address.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("기본 배송지로 신규 배송지를 생성한다")
        void createNewShippingAddressAsDefault() {
            // when
            ShippingAddress address = ShippingAddressFixtures.newDefaultShippingAddress();

            // then
            assertThat(address.isDefault()).isTrue();
            assertThat(address.isNew()).isTrue();
        }

        @Test
        @DisplayName("배송 요청사항 없이 신규 배송지를 생성한다")
        void createNewShippingAddressWithoutDeliveryRequest() {
            // when
            ShippingAddress address =
                    ShippingAddress.forNew(
                            ShippingAddressFixtures.defaultMemberId(),
                            ShippingAddressFixtures.defaultReceiverName(),
                            ShippingAddressFixtures.defaultShippingAddressName(),
                            ShippingAddressFixtures.defaultAddress(),
                            Country.KR,
                            DeliveryRequest.empty(),
                            CommonVoFixtures.defaultPhoneNumber(),
                            false,
                            CommonVoFixtures.now());

            // then
            assertThatCode(() -> address.deliveryRequestValue()).doesNotThrowAnyException();
            assertThat(address.deliveryRequestValue()).isNull();
        }
    }

    @Nested
    @DisplayName("forLegacy() - 레거시 시스템에서 신규 배송지 생성")
    class ForLegacyTest {

        @Test
        @DisplayName("LegacyMemberId로 레거시 배송지를 생성한다")
        void createLegacyShippingAddressWithLegacyMemberId() {
            // given
            LegacyMemberId legacyMemberId = ShippingAddressFixtures.defaultLegacyMemberId();
            Instant now = CommonVoFixtures.now();

            // when
            ShippingAddress address =
                    ShippingAddress.forLegacy(
                            legacyMemberId,
                            ShippingAddressFixtures.defaultReceiverName(),
                            ShippingAddressFixtures.defaultShippingAddressName(),
                            ShippingAddressFixtures.defaultAddress(),
                            ShippingAddressFixtures.defaultCountry(),
                            ShippingAddressFixtures.defaultDeliveryRequest(),
                            CommonVoFixtures.defaultPhoneNumber(),
                            false,
                            now);

            // then
            assertThat(address).isNotNull();
            assertThat(address.isNew()).isTrue();
            assertThat(address.legacyMemberId()).isEqualTo(legacyMemberId);
            assertThat(address.memberId()).isNull();
            assertThat(address.memberIdValue()).isNull();
            assertThat(address.isDeleted()).isFalse();
            assertThat(address.createdAt()).isEqualTo(now);
            assertThat(address.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("레거시 배송지는 생성 시 활성 상태이다")
        void legacyShippingAddressIsActiveOnCreation() {
            // when
            ShippingAddress address = ShippingAddressFixtures.newLegacyShippingAddress();

            // then
            assertThat(address.isDeleted()).isFalse();
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 레이어에서 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("영속성에서 활성 배송지를 복원한다")
        void reconstituteActiveShippingAddress() {
            // given
            ShippingAddressId id = ShippingAddressId.of(10L);
            MemberId memberId = ShippingAddressFixtures.defaultMemberId();
            Instant createdAt = CommonVoFixtures.yesterday();
            Instant updatedAt = CommonVoFixtures.yesterday();

            // when
            ShippingAddress address =
                    ShippingAddress.reconstitute(
                            id,
                            memberId,
                            null,
                            ShippingAddressFixtures.defaultReceiverName(),
                            ShippingAddressFixtures.defaultShippingAddressName(),
                            ShippingAddressFixtures.defaultAddress(),
                            Country.KR,
                            ShippingAddressFixtures.defaultDeliveryRequest(),
                            CommonVoFixtures.defaultPhoneNumber(),
                            false,
                            DeletionStatus.active(),
                            createdAt,
                            updatedAt);

            // then
            assertThat(address.isNew()).isFalse();
            assertThat(address.id()).isEqualTo(id);
            assertThat(address.idValue()).isEqualTo(10L);
            assertThat(address.memberId()).isEqualTo(memberId);
            assertThat(address.isDeleted()).isFalse();
            assertThat(address.createdAt()).isEqualTo(createdAt);
            assertThat(address.updatedAt()).isEqualTo(updatedAt);
        }

        @Test
        @DisplayName("영속성에서 삭제된 배송지를 복원한다")
        void reconstituteDeletedShippingAddress() {
            // given
            Instant deletedAt = CommonVoFixtures.yesterday();

            // when
            ShippingAddress address = ShippingAddressFixtures.deletedShippingAddress();

            // then
            assertThat(address.isDeleted()).isTrue();
            assertThat(address.deletionStatus().deletedAt()).isNotNull();
        }

        @Test
        @DisplayName("영속성에서 기본 배송지를 복원한다")
        void reconstituteDefaultShippingAddress() {
            // when
            ShippingAddress address = ShippingAddressFixtures.defaultShippingAddress();

            // then
            assertThat(address.isDefault()).isTrue();
            assertThat(address.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("레거시 배송지를 영속성에서 복원한다")
        void reconstituteLegacyShippingAddress() {
            // when
            ShippingAddress address = ShippingAddressFixtures.legacyShippingAddress(5L);

            // then
            assertThat(address.legacyMemberId())
                    .isEqualTo(ShippingAddressFixtures.defaultLegacyMemberId());
            assertThat(address.memberId()).isNull();
            assertThat(address.memberIdValue()).isNull();
            assertThat(address.legacyMemberIdValue()).isEqualTo(1001L);
        }
    }

    @Nested
    @DisplayName("update() - 배송지 정보 수정")
    class UpdateTest {

        @Test
        @DisplayName("배송지 정보를 수정한다")
        void updateShippingAddressInfo() {
            // given
            ShippingAddress address = ShippingAddressFixtures.activeShippingAddress();
            ShippingAddressUpdateData updateData = ShippingAddressFixtures.defaultUpdateData();

            // when
            address.update(updateData);

            // then
            assertThat(address.receiverName()).isEqualTo(updateData.receiverName());
            assertThat(address.shippingAddressName()).isEqualTo(updateData.shippingAddressName());
            assertThat(address.address()).isEqualTo(updateData.address());
            assertThat(address.country()).isEqualTo(updateData.country());
            assertThat(address.deliveryRequest()).isEqualTo(updateData.deliveryRequest());
            assertThat(address.phoneNumber()).isEqualTo(updateData.phoneNumber());
            assertThat(address.updatedAt()).isEqualTo(updateData.occurredAt());
        }

        @Test
        @DisplayName("수정 후 기존 필드 값은 변경된다")
        void updateChangesFieldValues() {
            // given
            ShippingAddress address = ShippingAddressFixtures.activeShippingAddress();
            String originalReceiverName = address.receiverNameValue();
            ShippingAddressUpdateData updateData = ShippingAddressFixtures.defaultUpdateData();

            // when
            address.update(updateData);

            // then
            assertThat(address.receiverNameValue()).isNotEqualTo(originalReceiverName);
            assertThat(address.receiverNameValue()).isEqualTo("김철수");
        }
    }

    @Nested
    @DisplayName("markAsDefault() - 기본 배송지 설정")
    class MarkAsDefaultTest {

        @Test
        @DisplayName("비기본 배송지를 기본 배송지로 설정한다")
        void markNonDefaultAddressAsDefault() {
            // given
            ShippingAddress address = ShippingAddressFixtures.activeShippingAddress();
            assertThat(address.isDefault()).isFalse();
            Instant now = CommonVoFixtures.now();

            // when
            address.markAsDefault(now);

            // then
            assertThat(address.isDefault()).isTrue();
            assertThat(address.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("이미 기본 배송지인 경우에도 markAsDefault()를 호출할 수 있다")
        void markAlreadyDefaultAddressAsDefault() {
            // given
            ShippingAddress address = ShippingAddressFixtures.defaultShippingAddress();
            assertThat(address.isDefault()).isTrue();

            // when & then
            assertThatCode(() -> address.markAsDefault(CommonVoFixtures.now()))
                    .doesNotThrowAnyException();
            assertThat(address.isDefault()).isTrue();
        }
    }

    @Nested
    @DisplayName("unmarkAsDefault() - 기본 배송지 해제")
    class UnmarkAsDefaultTest {

        @Test
        @DisplayName("기본 배송지를 해제한다")
        void unmarkDefaultAddress() {
            // given
            ShippingAddress address = ShippingAddressFixtures.defaultShippingAddress();
            assertThat(address.isDefault()).isTrue();
            Instant now = CommonVoFixtures.now();

            // when
            address.unmarkAsDefault(now);

            // then
            assertThat(address.isDefault()).isFalse();
            assertThat(address.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("기본 배송지가 아닌 경우에도 unmarkAsDefault()를 호출할 수 있다")
        void unmarkNonDefaultAddress() {
            // given
            ShippingAddress address = ShippingAddressFixtures.activeShippingAddress();
            assertThat(address.isDefault()).isFalse();

            // when & then
            assertThatCode(() -> address.unmarkAsDefault(CommonVoFixtures.now()))
                    .doesNotThrowAnyException();
            assertThat(address.isDefault()).isFalse();
        }
    }

    @Nested
    @DisplayName("delete() - 소프트 삭제")
    class DeleteTest {

        @Test
        @DisplayName("배송지를 소프트 삭제한다")
        void deleteShippingAddress() {
            // given
            ShippingAddress address = ShippingAddressFixtures.activeShippingAddress();
            assertThat(address.isDeleted()).isFalse();
            Instant now = CommonVoFixtures.now();

            // when
            address.delete(now);

            // then
            assertThat(address.isDeleted()).isTrue();
            assertThat(address.deletionStatus().deletedAt()).isEqualTo(now);
            assertThat(address.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("삭제된 배송지의 DeletionStatus는 삭제 상태이다")
        void deletedAddressHasDeletedStatus() {
            // given
            ShippingAddress address = ShippingAddressFixtures.activeShippingAddress();
            Instant now = CommonVoFixtures.now();

            // when
            address.delete(now);

            // then
            assertThat(address.deletionStatus().isDeleted()).isTrue();
            assertThat(address.deletionStatus().isActive()).isFalse();
        }
    }

    @Nested
    @DisplayName("isDeleted() / isDefault() - 상태 조회")
    class StateQueryTest {

        @Test
        @DisplayName("활성 배송지는 isDeleted()가 false를 반환한다")
        void activeAddressIsNotDeleted() {
            // given
            ShippingAddress address = ShippingAddressFixtures.activeShippingAddress();

            // then
            assertThat(address.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("삭제된 배송지는 isDeleted()가 true를 반환한다")
        void deletedAddressIsDeleted() {
            // given
            ShippingAddress address = ShippingAddressFixtures.deletedShippingAddress();

            // then
            assertThat(address.isDeleted()).isTrue();
        }

        @Test
        @DisplayName("기본 배송지는 isDefault()가 true를 반환한다")
        void defaultAddressIsDefault() {
            // given
            ShippingAddress address = ShippingAddressFixtures.defaultShippingAddress();

            // then
            assertThat(address.isDefault()).isTrue();
        }

        @Test
        @DisplayName("기본 배송지가 아니면 isDefault()가 false를 반환한다")
        void nonDefaultAddressIsNotDefault() {
            // given
            ShippingAddress address = ShippingAddressFixtures.activeShippingAddress();

            // then
            assertThat(address.isDefault()).isFalse();
        }
    }

    @Nested
    @DisplayName("Getter 메서드 테스트")
    class GetterTest {

        @Test
        @DisplayName("idValue()는 ID의 Long 값을 반환한다")
        void idValueReturnsLongValue() {
            // given
            ShippingAddress address = ShippingAddressFixtures.activeShippingAddress(42L);

            // then
            assertThat(address.idValue()).isEqualTo(42L);
        }

        @Test
        @DisplayName("receiverNameValue()는 수령인 이름 문자열을 반환한다")
        void receiverNameValueReturnsString() {
            // given
            ShippingAddress address = ShippingAddressFixtures.activeShippingAddress();

            // then
            assertThat(address.receiverNameValue()).isEqualTo("홍길동");
        }

        @Test
        @DisplayName("shippingAddressNameValue()는 배송지 별칭 문자열을 반환한다")
        void shippingAddressNameValueReturnsString() {
            // given
            ShippingAddress address = ShippingAddressFixtures.activeShippingAddress();

            // then
            assertThat(address.shippingAddressNameValue()).isEqualTo("집");
        }

        @Test
        @DisplayName("phoneNumberValue()는 전화번호 문자열을 반환한다")
        void phoneNumberValueReturnsString() {
            // given
            ShippingAddress address = ShippingAddressFixtures.activeShippingAddress();

            // then
            assertThat(address.phoneNumberValue()).isEqualTo("010-1234-5678");
        }

        @Test
        @DisplayName("deliveryRequestValue()는 배송 요청사항 문자열을 반환한다")
        void deliveryRequestValueReturnsString() {
            // given
            ShippingAddress address = ShippingAddressFixtures.activeShippingAddress();

            // then
            assertThat(address.deliveryRequestValue()).isEqualTo("문 앞에 놓아주세요");
        }

        @Test
        @DisplayName("memberIdValue()는 MemberId 값이 없으면 null을 반환한다")
        void memberIdValueReturnsNullWhenAbsent() {
            // given
            ShippingAddress address = ShippingAddressFixtures.legacyShippingAddress(1L);

            // then
            assertThat(address.memberIdValue()).isNull();
        }

        @Test
        @DisplayName("legacyMemberIdValue()는 LegacyMemberId 값이 없으면 null을 반환한다")
        void legacyMemberIdValueReturnsNullWhenAbsent() {
            // given
            ShippingAddress address = ShippingAddressFixtures.activeShippingAddress();

            // then
            assertThat(address.legacyMemberIdValue()).isNull();
        }
    }
}
