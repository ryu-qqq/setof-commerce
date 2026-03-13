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
 * @since 1.1.0
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
        @DisplayName("등록 요청을 RegisterSellerCommand로 변환한다")
        void toCommand_Register_Success() {
            // given
            RegisterSellerApiRequest request = SellerApiFixtures.registerRequest();

            // when
            RegisterSellerCommand command = mapper.toCommand(request);

            // then
            assertThat(command.seller().sellerName()).isEqualTo(request.sellerInfo().sellerName());
            assertThat(command.seller().displayName())
                    .isEqualTo(request.sellerInfo().displayName());
            assertThat(command.seller().logoUrl()).isEqualTo(request.sellerInfo().logoUrl());
            assertThat(command.seller().description())
                    .isEqualTo(request.sellerInfo().description());
        }

        @Test
        @DisplayName("사업자 정보를 RegisterSellerCommand.SellerBusinessInfoCommand로 변환한다")
        void toCommand_Register_BusinessInfo_Success() {
            // given
            RegisterSellerApiRequest request = SellerApiFixtures.registerRequest();

            // when
            RegisterSellerCommand command = mapper.toCommand(request);

            // then
            assertThat(command.businessInfo().registrationNumber())
                    .isEqualTo(request.businessInfo().registrationNumber());
            assertThat(command.businessInfo().companyName())
                    .isEqualTo(request.businessInfo().companyName());
            assertThat(command.businessInfo().representative())
                    .isEqualTo(request.businessInfo().representative());
            assertThat(command.businessInfo().saleReportNumber())
                    .isEqualTo(request.businessInfo().saleReportNumber());
        }

        @Test
        @DisplayName("주소 정보를 RegisterSellerCommand.AddressCommand로 변환한다")
        void toCommand_Register_Address_Success() {
            // given
            RegisterSellerApiRequest request = SellerApiFixtures.registerRequest();

            // when
            RegisterSellerCommand command = mapper.toCommand(request);

            // then
            assertThat(command.businessInfo().businessAddress().zipCode())
                    .isEqualTo(request.businessInfo().businessAddress().zipCode());
            assertThat(command.businessInfo().businessAddress().line1())
                    .isEqualTo(request.businessInfo().businessAddress().line1());
            assertThat(command.businessInfo().businessAddress().line2())
                    .isEqualTo(request.businessInfo().businessAddress().line2());
        }

        @Test
        @DisplayName("CS 연락처를 RegisterSellerCommand.CsContactCommand로 변환한다")
        void toCommand_Register_CsContact_Success() {
            // given
            RegisterSellerApiRequest request = SellerApiFixtures.registerRequest();

            // when
            RegisterSellerCommand command = mapper.toCommand(request);

            // then
            assertThat(command.businessInfo().csContact().phone())
                    .isEqualTo(request.businessInfo().csContact().phone());
            assertThat(command.businessInfo().csContact().email())
                    .isEqualTo(request.businessInfo().csContact().email());
            assertThat(command.businessInfo().csContact().mobile())
                    .isEqualTo(request.businessInfo().csContact().mobile());
        }
    }

    @Nested
    @DisplayName("toCommand(Long, UpdateSellerApiRequest)")
    class ToUpdateCommandTest {

        @Test
        @DisplayName("수정 요청을 UpdateSellerCommand로 변환한다")
        void toCommand_Update_Success() {
            // given
            Long sellerId = SellerApiFixtures.DEFAULT_SELLER_ID;
            UpdateSellerApiRequest request = SellerApiFixtures.updateRequest();

            // when
            UpdateSellerCommand command = mapper.toCommand(sellerId, request);

            // then
            assertThat(command.sellerId()).isEqualTo(sellerId);
            assertThat(command.sellerName()).isEqualTo(request.sellerName());
            assertThat(command.displayName()).isEqualTo(request.displayName());
            assertThat(command.logoUrl()).isEqualTo(request.logoUrl());
            assertThat(command.description()).isEqualTo(request.description());
        }

        @Test
        @DisplayName("CS 정보를 포함한 수정 요청을 UpdateSellerCommand.CsInfoCommand로 변환한다")
        void toCommand_Update_WithCsInfo_Success() {
            // given
            Long sellerId = SellerApiFixtures.DEFAULT_SELLER_ID;
            UpdateSellerApiRequest request = SellerApiFixtures.updateRequest();

            // when
            UpdateSellerCommand command = mapper.toCommand(sellerId, request);

            // then
            assertThat(command.csInfo()).isNotNull();
            assertThat(command.csInfo().phone()).isEqualTo(request.csInfo().phone());
            assertThat(command.csInfo().email()).isEqualTo(request.csInfo().email());
            assertThat(command.csInfo().mobile()).isEqualTo(request.csInfo().mobile());
        }

        @Test
        @DisplayName("사업자 정보를 포함한 수정 요청을 UpdateSellerCommand.BusinessInfoCommand로 변환한다")
        void toCommand_Update_WithBusinessInfo_Success() {
            // given
            Long sellerId = SellerApiFixtures.DEFAULT_SELLER_ID;
            UpdateSellerApiRequest request = SellerApiFixtures.updateRequest();

            // when
            UpdateSellerCommand command = mapper.toCommand(sellerId, request);

            // then
            assertThat(command.businessInfo()).isNotNull();
            assertThat(command.businessInfo().registrationNumber())
                    .isEqualTo(request.businessInfo().registrationNumber());
            assertThat(command.businessInfo().companyName())
                    .isEqualTo(request.businessInfo().companyName());
            assertThat(command.businessInfo().businessAddress()).isNotNull();
            assertThat(command.businessInfo().businessAddress().zipCode())
                    .isEqualTo(request.businessInfo().businessAddress().zipCode());
        }

        @Test
        @DisplayName("선택 항목이 null인 수정 요청을 변환하면 csInfo와 businessInfo가 null이다")
        void toCommand_Update_WithNullOptionalFields_Success() {
            // given
            Long sellerId = SellerApiFixtures.DEFAULT_SELLER_ID;
            UpdateSellerApiRequest request = SellerApiFixtures.updateRequestWithoutOptional();

            // when
            UpdateSellerCommand command = mapper.toCommand(sellerId, request);

            // then
            assertThat(command.sellerId()).isEqualTo(sellerId);
            assertThat(command.csInfo()).isNull();
            assertThat(command.businessInfo()).isNull();
        }
    }
}
