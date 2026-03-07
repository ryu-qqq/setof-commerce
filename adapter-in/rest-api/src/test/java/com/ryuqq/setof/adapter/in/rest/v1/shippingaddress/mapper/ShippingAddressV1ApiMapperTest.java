package com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.ShippingAddressApiFixtures;
import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.dto.request.RegisterShippingAddressV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.dto.request.UpdateShippingAddressV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v1.shippingaddress.dto.response.ShippingAddressV1ApiResponse;
import com.ryuqq.setof.application.shippingaddress.dto.command.DeleteShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.command.RegisterShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.command.UpdateShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResult;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * ShippingAddressV1ApiMapper 단위 테스트.
 *
 * <p>배송지 V1 API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("ShippingAddressV1ApiMapper 단위 테스트")
class ShippingAddressV1ApiMapperTest {

    private ShippingAddressV1ApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ShippingAddressV1ApiMapper();
    }

    @Nested
    @DisplayName("toResponse 메서드 테스트")
    class ToResponseTest {

        @Test
        @DisplayName("ShippingAddressResult를 ShippingAddressV1ApiResponse로 변환한다")
        void toResponse_Success() {
            // given
            ShippingAddressResult result = ShippingAddressApiFixtures.shippingAddressResult(1L);

            // when
            ShippingAddressV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.shippingAddressId()).isEqualTo(1L);
            assertThat(response.defaultYn()).isEqualTo("N");
        }

        @Test
        @DisplayName("shippingDetails 필드가 올바르게 매핑된다")
        void toResponse_ShippingDetailsMapping() {
            // given
            ShippingAddressResult result = ShippingAddressApiFixtures.shippingAddressResult(1L);

            // when
            ShippingAddressV1ApiResponse response = mapper.toResponse(result);

            // then
            ShippingAddressV1ApiResponse.ShippingDetailsResponse details =
                    response.shippingDetails();
            assertThat(details.receiverName()).isEqualTo("홍길동");
            assertThat(details.shippingAddressName()).isEqualTo("집");
            assertThat(details.addressLine1()).isEqualTo("서울특별시 강남구 테헤란로 123");
            assertThat(details.addressLine2()).isEqualTo("456호");
            assertThat(details.zipCode()).isEqualTo("06234");
            assertThat(details.country()).isEqualTo("KR");
            assertThat(details.deliveryRequest()).isEqualTo("문 앞에 놓아주세요");
            assertThat(details.phoneNumber()).isEqualTo("01012345678");
        }

        @Test
        @DisplayName("기본 배송지인 경우 defaultYn이 Y로 변환된다")
        void toResponse_DefaultAddress() {
            // given
            ShippingAddressResult result =
                    ShippingAddressApiFixtures.shippingAddressResultAsDefault(2L);

            // when
            ShippingAddressV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.shippingAddressId()).isEqualTo(2L);
            assertThat(response.defaultYn()).isEqualTo("Y");
        }
    }

    @Nested
    @DisplayName("toResponseList 메서드 테스트")
    class ToResponseListTest {

        @Test
        @DisplayName("ShippingAddressResult 목록을 ShippingAddressV1ApiResponse 목록으로 변환한다")
        void toResponseList_Success() {
            // given
            List<ShippingAddressResult> results =
                    ShippingAddressApiFixtures.shippingAddressResultList();

            // when
            List<ShippingAddressV1ApiResponse> responses = mapper.toResponseList(results);

            // then
            assertThat(responses).hasSize(2);
            assertThat(responses.get(0).shippingAddressId()).isEqualTo(1L);
            assertThat(responses.get(0).defaultYn()).isEqualTo("Y");
            assertThat(responses.get(1).shippingAddressId()).isEqualTo(2L);
            assertThat(responses.get(1).defaultYn()).isEqualTo("N");
        }

        @Test
        @DisplayName("빈 목록을 빈 응답 목록으로 변환한다")
        void toResponseList_Empty() {
            // given
            List<ShippingAddressResult> results = List.of();

            // when
            List<ShippingAddressV1ApiResponse> responses = mapper.toResponseList(results);

            // then
            assertThat(responses).isEmpty();
        }
    }

    @Nested
    @DisplayName("toRegisterCommand 메서드 테스트")
    class ToRegisterCommandTest {

        @Test
        @DisplayName("userId와 RegisterRequest를 RegisterShippingAddressCommand로 변환한다")
        void toRegisterCommand_Success() {
            // given
            Long userId = 10L;
            RegisterShippingAddressV1ApiRequest request =
                    ShippingAddressApiFixtures.registerRequest();

            // when
            RegisterShippingAddressCommand command = mapper.toRegisterCommand(userId, request);

            // then
            assertThat(command.userId()).isEqualTo(userId);
            assertThat(command.receiverName()).isEqualTo("홍길동");
            assertThat(command.shippingAddressName()).isEqualTo("집");
            assertThat(command.addressLine1()).isEqualTo("서울특별시 강남구 테헤란로 123");
            assertThat(command.addressLine2()).isEqualTo("456호");
            assertThat(command.zipCode()).isEqualTo("06234");
            assertThat(command.country()).isEqualTo("KR");
            assertThat(command.deliveryRequest()).isEqualTo("문 앞에 놓아주세요");
            assertThat(command.phoneNumber()).isEqualTo("01012345678");
            assertThat(command.defaultAddress()).isFalse();
        }

        @Test
        @DisplayName("기본 배송지 요청 시 defaultAddress가 true로 변환된다")
        void toRegisterCommand_DefaultAddress() {
            // given
            Long userId = 10L;
            RegisterShippingAddressV1ApiRequest request =
                    ShippingAddressApiFixtures.registerRequestAsDefault();

            // when
            RegisterShippingAddressCommand command = mapper.toRegisterCommand(userId, request);

            // then
            assertThat(command.userId()).isEqualTo(userId);
            assertThat(command.defaultAddress()).isTrue();
        }
    }

    @Nested
    @DisplayName("toUpdateCommand 메서드 테스트")
    class ToUpdateCommandTest {

        @Test
        @DisplayName("userId, shippingAddressId, UpdateRequest를 UpdateShippingAddressCommand로 변환한다")
        void toUpdateCommand_Success() {
            // given
            Long userId = 10L;
            Long shippingAddressId = 5L;
            UpdateShippingAddressV1ApiRequest request = ShippingAddressApiFixtures.updateRequest();

            // when
            UpdateShippingAddressCommand command =
                    mapper.toUpdateCommand(userId, shippingAddressId, request);

            // then
            assertThat(command.userId()).isEqualTo(userId);
            assertThat(command.shippingAddressId()).isEqualTo(shippingAddressId);
            assertThat(command.receiverName()).isEqualTo("김철수");
            assertThat(command.shippingAddressName()).isEqualTo("회사");
            assertThat(command.addressLine1()).isEqualTo("서울특별시 서초구 서초대로 456");
            assertThat(command.addressLine2()).isEqualTo("7층");
            assertThat(command.zipCode()).isEqualTo("06500");
            assertThat(command.country()).isEqualTo("KR");
            assertThat(command.deliveryRequest()).isEqualTo("부재 시 경비실에 맡겨주세요");
            assertThat(command.phoneNumber()).isEqualTo("01098765432");
            assertThat(command.defaultAddress()).isFalse();
        }
    }

    @Nested
    @DisplayName("toDeleteCommand 메서드 테스트")
    class ToDeleteCommandTest {

        @Test
        @DisplayName("userId와 shippingAddressId를 DeleteShippingAddressCommand로 변환한다")
        void toDeleteCommand_Success() {
            // given
            Long userId = 10L;
            Long shippingAddressId = 5L;

            // when
            DeleteShippingAddressCommand command =
                    mapper.toDeleteCommand(userId, shippingAddressId);

            // then
            assertThat(command.userId()).isEqualTo(userId);
            assertThat(command.shippingAddressId()).isEqualTo(shippingAddressId);
        }
    }
}
