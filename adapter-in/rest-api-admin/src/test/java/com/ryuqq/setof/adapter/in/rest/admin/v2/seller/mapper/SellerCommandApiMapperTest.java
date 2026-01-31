package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.seller.SellerApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command.RegisterSellerApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command.UpdateSellerApiRequest;
import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand;
import com.ryuqq.setof.application.seller.dto.command.UpdateSellerCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * SellerCommandApiMapper 단위 테스트.
 *
 * <p>Command API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("SellerCommandApiMapper 단위 테스트")
class SellerCommandApiMapperTest {

    private SellerCommandApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new SellerCommandApiMapper();
    }

    @Nested
    @DisplayName("toCommand(RegisterSellerApiRequest)")
    class ToRegisterCommandTest {

        @Test
        @DisplayName("등록 요청을 Command로 변환한다")
        void toCommand_Register_Success() {
            // given
            RegisterSellerApiRequest request = SellerApiFixtures.registerRequest();

            // when
            RegisterSellerCommand command = mapper.toCommand(request);

            // then
            assertThat(command.seller()).isNotNull();
            assertThat(command.seller().sellerName()).isEqualTo(request.seller().sellerName());
            assertThat(command.seller().displayName()).isEqualTo(request.seller().displayName());
            assertThat(command.seller().logoUrl()).isEqualTo(request.seller().logoUrl());
            assertThat(command.seller().description()).isEqualTo(request.seller().description());
        }

        @Test
        @DisplayName("사업자 정보를 Command로 변환한다")
        void toCommand_Register_BusinessInfo() {
            // given
            RegisterSellerApiRequest request = SellerApiFixtures.registerRequest();

            // when
            RegisterSellerCommand command = mapper.toCommand(request);

            // then
            assertThat(command.businessInfo()).isNotNull();
            assertThat(command.businessInfo().registrationNumber())
                    .isEqualTo(request.businessInfo().registrationNumber());
            assertThat(command.businessInfo().companyName())
                    .isEqualTo(request.businessInfo().companyName());
            assertThat(command.businessInfo().representative())
                    .isEqualTo(request.businessInfo().representative());
        }

        @Test
        @DisplayName("주소 정보를 Command로 변환한다")
        void toCommand_Register_Address() {
            // given
            RegisterSellerApiRequest request = SellerApiFixtures.registerRequest();

            // when
            RegisterSellerCommand command = mapper.toCommand(request);

            // then
            assertThat(command.address()).isNotNull();
            assertThat(command.address().addressType()).isEqualTo(request.address().addressType());
            assertThat(command.address().addressName()).isEqualTo(request.address().addressName());
            assertThat(command.address().address()).isNotNull();
        }
    }

    @Nested
    @DisplayName("toCommand(Long, UpdateSellerApiRequest)")
    class ToUpdateCommandTest {

        @Test
        @DisplayName("수정 요청을 Command로 변환한다")
        void toCommand_Update_Success() {
            // given
            Long sellerId = 1L;
            UpdateSellerApiRequest request = SellerApiFixtures.updateRequest();

            // when
            UpdateSellerCommand command = mapper.toCommand(sellerId, request);

            // then
            assertThat(command.sellerId()).isEqualTo(sellerId);
            assertThat(command.sellerName()).isEqualTo(request.sellerName());
            assertThat(command.displayName()).isEqualTo(request.displayName());
            assertThat(command.logoUrl()).isEqualTo(request.logoUrl());
            assertThat(command.description()).isEqualTo(request.description());
            // address, csInfo, businessInfo는 필수값이므로 null이 아님
            assertThat(command.address()).isNotNull();
            assertThat(command.csInfo()).isNotNull();
            assertThat(command.businessInfo()).isNotNull();
        }

        @Test
        @DisplayName("주소/CS/사업자 정보가 포함된 수정 요청을 변환한다")
        void toCommand_Update_WithAddressCsBusinessInfo() {
            // given
            Long sellerId = 1L;
            UpdateSellerApiRequest request = SellerApiFixtures.updateRequestWithAddress();

            // when
            UpdateSellerCommand command = mapper.toCommand(sellerId, request);

            // then
            assertThat(command.sellerId()).isEqualTo(sellerId);
            assertThat(command.address()).isNotNull();
            assertThat(command.address().zipCode()).isEqualTo(request.address().zipCode());
            assertThat(command.csInfo()).isNotNull();
            assertThat(command.csInfo().phone()).isEqualTo(request.csInfo().phone());
            assertThat(command.businessInfo()).isNotNull();
            assertThat(command.businessInfo().registrationNumber())
                    .isEqualTo(request.businessInfo().registrationNumber());
        }
    }
}
