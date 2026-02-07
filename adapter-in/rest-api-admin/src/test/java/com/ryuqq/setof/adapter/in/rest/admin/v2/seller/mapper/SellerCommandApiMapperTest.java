package com.ryuqq.setof.adapter.in.rest.admin.v2.seller.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.seller.SellerApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command.RegisterSellerApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command.UpdateSellerApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.seller.dto.command.UpdateSellerFullApiRequest;
import com.ryuqq.setof.application.seller.dto.command.RegisterSellerCommand;
import com.ryuqq.setof.application.seller.dto.command.UpdateSellerCommand;
import com.ryuqq.setof.application.seller.dto.command.UpdateSellerFullCommand;
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

    @Nested
    @DisplayName("toCommand(Long, UpdateSellerFullApiRequest)")
    class ToUpdateFullCommandTest {

        @Test
        @DisplayName("전체 수정 요청을 Command로 변환한다")
        void toCommand_UpdateFull_Success() {
            // given
            Long sellerId = 1L;
            UpdateSellerFullApiRequest request = SellerApiFixtures.updateFullRequest();

            // when
            UpdateSellerFullCommand command = mapper.toCommand(sellerId, request);

            // then
            assertThat(command.sellerId()).isEqualTo(sellerId);
            assertThat(command.seller()).isNotNull();
            assertThat(command.seller().sellerName()).isEqualTo(request.seller().sellerName());
            assertThat(command.seller().displayName()).isEqualTo(request.seller().displayName());
            assertThat(command.seller().logoUrl()).isEqualTo(request.seller().logoUrl());
            assertThat(command.seller().description()).isEqualTo(request.seller().description());
        }

        @Test
        @DisplayName("사업자 정보를 Command로 변환한다")
        void toCommand_UpdateFull_BusinessInfo() {
            // given
            Long sellerId = 1L;
            UpdateSellerFullApiRequest request = SellerApiFixtures.updateFullRequest();

            // when
            UpdateSellerFullCommand command = mapper.toCommand(sellerId, request);

            // then
            assertThat(command.businessInfo()).isNotNull();
            assertThat(command.businessInfo().registrationNumber())
                    .isEqualTo(request.businessInfo().registrationNumber());
            assertThat(command.businessInfo().companyName())
                    .isEqualTo(request.businessInfo().companyName());
            assertThat(command.businessInfo().representative())
                    .isEqualTo(request.businessInfo().representative());
            assertThat(command.businessInfo().saleReportNumber())
                    .isEqualTo(request.businessInfo().saleReportNumber());
            assertThat(command.businessInfo().businessAddress()).isNotNull();
            assertThat(command.businessInfo().businessAddress().zipCode())
                    .isEqualTo(request.businessInfo().businessAddress().zipCode());
        }

        @Test
        @DisplayName("CS 정보를 Command로 변환한다")
        void toCommand_UpdateFull_CsInfo() {
            // given
            Long sellerId = 1L;
            UpdateSellerFullApiRequest request = SellerApiFixtures.updateFullRequest();

            // when
            UpdateSellerFullCommand command = mapper.toCommand(sellerId, request);

            // then
            assertThat(command.csInfo()).isNotNull();
            assertThat(command.csInfo().csContact()).isNotNull();
            assertThat(command.csInfo().csContact().phone()).isEqualTo(request.csInfo().phone());
            assertThat(command.csInfo().csContact().email()).isEqualTo(request.csInfo().email());
            assertThat(command.csInfo().operatingHours()).isNotNull();
            assertThat(command.csInfo().operatingHours().startTime())
                    .isEqualTo(request.csInfo().operatingStartTime());
            assertThat(command.csInfo().operatingHours().endTime())
                    .isEqualTo(request.csInfo().operatingEndTime());
            assertThat(command.csInfo().operatingDays())
                    .isEqualTo(request.csInfo().operatingDays());
            assertThat(command.csInfo().kakaoChannelUrl())
                    .isEqualTo(request.csInfo().kakaoChannelUrl());
        }

        @Test
        @DisplayName("주소 정보를 Command로 변환한다")
        void toCommand_UpdateFull_Address() {
            // given
            Long sellerId = 1L;
            UpdateSellerFullApiRequest request = SellerApiFixtures.updateFullRequest();

            // when
            UpdateSellerFullCommand command = mapper.toCommand(sellerId, request);

            // then
            assertThat(command.address()).isNotNull();
            assertThat(command.address().addressId()).isEqualTo(request.address().addressId());
            assertThat(command.address().addressName()).isEqualTo(request.address().addressName());
            assertThat(command.address().address()).isNotNull();
            assertThat(command.address().address().zipCode())
                    .isEqualTo(request.address().address().zipCode());
            assertThat(command.address().contactInfo()).isNotNull();
            assertThat(command.address().contactInfo().name())
                    .isEqualTo(request.address().contactInfo().name());
        }

        @Test
        @DisplayName("계약 정보를 Command로 변환한다")
        void toCommand_UpdateFull_ContractInfo() {
            // given
            Long sellerId = 1L;
            UpdateSellerFullApiRequest request = SellerApiFixtures.updateFullRequest();

            // when
            UpdateSellerFullCommand command = mapper.toCommand(sellerId, request);

            // then
            assertThat(command.contractInfo()).isNotNull();
            assertThat(command.contractInfo().commissionRate())
                    .isEqualTo(request.contractInfo().commissionRate());
            assertThat(command.contractInfo().contractStartDate())
                    .isEqualTo(request.contractInfo().contractStartDate());
            assertThat(command.contractInfo().contractEndDate())
                    .isEqualTo(request.contractInfo().contractEndDate());
            assertThat(command.contractInfo().specialTerms())
                    .isEqualTo(request.contractInfo().specialTerms());
        }

        @Test
        @DisplayName("정산 정보를 Command로 변환한다")
        void toCommand_UpdateFull_SettlementInfo() {
            // given
            Long sellerId = 1L;
            UpdateSellerFullApiRequest request = SellerApiFixtures.updateFullRequest();

            // when
            UpdateSellerFullCommand command = mapper.toCommand(sellerId, request);

            // then
            assertThat(command.settlementInfo()).isNotNull();
            assertThat(command.settlementInfo().settlementCycle())
                    .isEqualTo(request.settlementInfo().settlementCycle());
            assertThat(command.settlementInfo().settlementDay())
                    .isEqualTo(request.settlementInfo().settlementDay());
            assertThat(command.settlementInfo().bankAccount()).isNotNull();
            assertThat(command.settlementInfo().bankAccount().bankCode())
                    .isEqualTo(request.settlementInfo().bankAccount().bankCode());
            assertThat(command.settlementInfo().bankAccount().bankName())
                    .isEqualTo(request.settlementInfo().bankAccount().bankName());
            assertThat(command.settlementInfo().bankAccount().accountNumber())
                    .isEqualTo(request.settlementInfo().bankAccount().accountNumber());
            assertThat(command.settlementInfo().bankAccount().accountHolderName())
                    .isEqualTo(request.settlementInfo().bankAccount().accountHolderName());
        }

        @Test
        @DisplayName("CS 운영시간이 null이면 operatingHours가 null이다")
        void toCommand_UpdateFull_NullOperatingHours() {
            // given
            Long sellerId = 1L;
            UpdateSellerFullApiRequest request =
                    new UpdateSellerFullApiRequest(
                            SellerApiFixtures.updateFullSellerInfoRequest(),
                            SellerApiFixtures.updateFullBusinessInfoRequest(),
                            new UpdateSellerFullApiRequest.CsInfoRequest(
                                    "02-1234-5678",
                                    "cs@example.com",
                                    "010-1234-5678",
                                    null,
                                    null,
                                    "MON,TUE,WED,THU,FRI",
                                    null),
                            SellerApiFixtures.updateFullAddressInfoRequest(),
                            SellerApiFixtures.updateFullContractInfoRequest(),
                            SellerApiFixtures.updateFullSettlementInfoRequest());

            // when
            UpdateSellerFullCommand command = mapper.toCommand(sellerId, request);

            // then
            assertThat(command.csInfo().operatingHours()).isNull();
        }
    }
}
