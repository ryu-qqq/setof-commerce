package com.ryuqq.setof.domain.seller.aggregate;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.domain.common.CommonVoFixtures;
import com.ryuqq.setof.domain.seller.SellerFixtures;
import com.ryuqq.setof.domain.seller.vo.AddressType;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@DisplayName("SellerAddress Aggregate 테스트")
class SellerAddressTest {

    @Nested
    @DisplayName("forNew() - 신규 주소 생성")
    class ForNewTest {

        @Test
        @DisplayName("신규 출고지 주소를 생성한다")
        void createNewShippingAddress() {
            // when
            var address = SellerFixtures.newShippingAddress();

            // then
            assertThat(address.isNew()).isTrue();
            assertThat(address.addressType()).isEqualTo(AddressType.SHIPPING);
            assertThat(address.isShippingAddress()).isTrue();
            assertThat(address.isReturnAddress()).isFalse();
            assertThat(address.isDefaultAddress()).isTrue();
            assertThat(address.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("신규 반품지 주소를 생성한다")
        void createNewReturnAddress() {
            // when
            var address = SellerFixtures.newReturnAddress();

            // then
            assertThat(address.isNew()).isTrue();
            assertThat(address.addressType()).isEqualTo(AddressType.RETURN);
            assertThat(address.isShippingAddress()).isFalse();
            assertThat(address.isReturnAddress()).isTrue();
            assertThat(address.isDefaultAddress()).isFalse();
        }
    }

    @Nested
    @DisplayName("reconstitute() - 영속성 복원")
    class ReconstituteTest {

        @Test
        @DisplayName("출고지 주소를 복원한다")
        void reconstituteShippingAddress() {
            // when
            var address = SellerFixtures.activeShippingAddress();

            // then
            assertThat(address.isNew()).isFalse();
            assertThat(address.idValue()).isEqualTo(1L);
            assertThat(address.isShippingAddress()).isTrue();
            assertThat(address.isDeleted()).isFalse();
        }

        @Test
        @DisplayName("반품지 주소를 복원한다")
        void reconstituteReturnAddress() {
            // when
            var address = SellerFixtures.activeReturnAddress();

            // then
            assertThat(address.idValue()).isEqualTo(2L);
            assertThat(address.isReturnAddress()).isTrue();
        }

        @Test
        @DisplayName("삭제된 주소를 복원한다")
        void reconstituteDeletedAddress() {
            // when
            var address = SellerFixtures.deletedSellerAddress();

            // then
            assertThat(address.isDeleted()).isTrue();
            assertThat(address.deletedAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("update() - 주소 정보 수정")
    class UpdateTest {

        @Test
        @DisplayName("주소 정보를 수정한다")
        void updateSellerAddress() {
            // given
            var address = SellerFixtures.activeShippingAddress();
            var updateData = SellerFixtures.sellerAddressUpdateData();
            Instant now = CommonVoFixtures.now();

            // when
            address.update(updateData, now);

            // then
            assertThat(address.addressNameValue()).isEqualTo("수정된 창고");
            assertThat(address.addressZipCode()).isEqualTo("54321");
            assertThat(address.addressRoad()).isEqualTo("부산시 해운대구 센텀로 100");
            assertThat(address.contactInfoName()).isEqualTo("최담당");
            assertThat(address.contactInfoPhone()).isEqualTo("010-3333-4444");
            assertThat(address.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("markAsDefault() / unmarkDefault() - 기본 주소 설정")
    class DefaultAddressTest {

        @Test
        @DisplayName("주소를 기본 주소로 설정한다")
        void markAsDefault() {
            // given
            var address = SellerFixtures.activeReturnAddress();
            assertThat(address.isDefaultAddress()).isFalse();
            Instant now = CommonVoFixtures.now();

            // when
            address.markAsDefault(now);

            // then
            assertThat(address.isDefaultAddress()).isTrue();
            assertThat(address.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("기본 주소 설정을 해제한다")
        void unmarkDefault() {
            // given
            var address = SellerFixtures.activeShippingAddress();
            assertThat(address.isDefaultAddress()).isTrue();
            Instant now = CommonVoFixtures.now();

            // when
            address.unmarkDefault(now);

            // then
            assertThat(address.isDefaultAddress()).isFalse();
            assertThat(address.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("delete() / restore() - 삭제 및 복원")
    class DeletionTest {

        @Test
        @DisplayName("주소를 삭제(Soft Delete)한다")
        void deleteSellerAddress() {
            // given
            var address = SellerFixtures.activeShippingAddress();
            Instant now = CommonVoFixtures.now();

            // when
            address.delete(now);

            // then
            assertThat(address.isDeleted()).isTrue();
            assertThat(address.deletedAt()).isEqualTo(now);
            assertThat(address.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("삭제된 주소를 복원한다")
        void restoreSellerAddress() {
            // given
            var address = SellerFixtures.deletedSellerAddress();
            Instant now = CommonVoFixtures.now();

            // when
            address.restore(now);

            // then
            assertThat(address.isDeleted()).isFalse();
            assertThat(address.deletedAt()).isNull();
            assertThat(address.updatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("Getter 메서드 테스트")
    class GetterTest {

        @Test
        @DisplayName("ID 관련 getter들이 올바른 값을 반환한다")
        void returnsIdValues() {
            var address = SellerFixtures.activeShippingAddress();

            assertThat(address.id()).isNotNull();
            assertThat(address.idValue()).isEqualTo(1L);
            assertThat(address.sellerId()).isNotNull();
            assertThat(address.sellerIdValue()).isEqualTo(1L);
        }

        @Test
        @DisplayName("주소 관련 getter들이 올바른 값을 반환한다")
        void returnsAddressValues() {
            var address = SellerFixtures.activeShippingAddress();

            assertThat(address.addressName()).isNotNull();
            assertThat(address.addressNameValue()).isEqualTo("본사 창고");
            assertThat(address.address()).isNotNull();
            assertThat(address.addressZipCode()).isEqualTo("06141");
            assertThat(address.addressRoad()).isEqualTo("서울시 강남구 테헤란로 123");
            assertThat(address.addressDetail()).isEqualTo("테스트빌딩 5층");
        }

        @Test
        @DisplayName("담당자 연락처 관련 getter들이 올바른 값을 반환한다")
        void returnsContactInfoValues() {
            var address = SellerFixtures.activeShippingAddress();

            assertThat(address.contactInfo()).isNotNull();
            assertThat(address.contactInfoName()).isEqualTo("김담당");
            assertThat(address.contactInfoPhone()).isEqualTo("010-9876-5432");
        }

        @Test
        @DisplayName("시간 관련 getter들이 올바른 값을 반환한다")
        void returnsTimeValues() {
            var address = SellerFixtures.activeShippingAddress();

            assertThat(address.createdAt()).isNotNull();
            assertThat(address.updatedAt()).isNotNull();
        }

        @Test
        @DisplayName("deletionStatus()는 DeletionStatus를 반환한다")
        void returnsDeletionStatus() {
            var address = SellerFixtures.activeShippingAddress();
            assertThat(address.deletionStatus()).isNotNull();
            assertThat(address.deletionStatus().isDeleted()).isFalse();
        }
    }
}
