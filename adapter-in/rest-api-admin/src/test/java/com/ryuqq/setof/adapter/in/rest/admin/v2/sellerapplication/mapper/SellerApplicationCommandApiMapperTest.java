package com.ryuqq.setof.adapter.in.rest.admin.v2.sellerapplication.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.sellerapplication.SellerApplicationApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v2.sellerapplication.dto.command.ApplySellerApplicationApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.sellerapplication.dto.command.ApproveSellerApplicationApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v2.sellerapplication.dto.command.RejectSellerApplicationApiRequest;
import com.ryuqq.setof.application.sellerapplication.dto.command.ApplySellerApplicationCommand;
import com.ryuqq.setof.application.sellerapplication.dto.command.ApproveSellerApplicationCommand;
import com.ryuqq.setof.application.sellerapplication.dto.command.RejectSellerApplicationCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * SellerApplicationCommandApiMapper 단위 테스트.
 *
 * <p>Command API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.0.0
 */
@Tag("unit")
@DisplayName("SellerApplicationCommandApiMapper 단위 테스트")
class SellerApplicationCommandApiMapperTest {

    private SellerApplicationCommandApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new SellerApplicationCommandApiMapper();
    }

    @Nested
    @DisplayName("toCommand(ApplySellerApplicationApiRequest)")
    class ToApplyCommandTest {

        @Test
        @DisplayName("입점 신청 요청을 Command로 변환한다")
        void toCommand_Apply_Success() {
            // given
            ApplySellerApplicationApiRequest request = SellerApplicationApiFixtures.applyRequest();

            // when
            ApplySellerApplicationCommand command = mapper.toCommand(request);

            // then
            assertThat(command.sellerInfo()).isNotNull();
            assertThat(command.sellerInfo().sellerName())
                    .isEqualTo(request.sellerInfo().sellerName());
            assertThat(command.businessInfo()).isNotNull();
            assertThat(command.businessInfo().registrationNumber())
                    .isEqualTo(request.businessInfo().registrationNumber());
            assertThat(command.csContact()).isNotNull();
            assertThat(command.csContact().phone()).isEqualTo(request.csContact().phone());
            assertThat(command.addressInfo()).isNotNull();
            assertThat(command.addressInfo().addressType())
                    .isEqualTo(request.returnAddress().addressType());
        }

        @Test
        @DisplayName("displayName이 null이면 sellerName으로 대체한다")
        void toCommand_Apply_NullDisplayName_UsesSellerName() {
            // given
            ApplySellerApplicationApiRequest.SellerInfo sellerInfo =
                    new ApplySellerApplicationApiRequest.SellerInfo(
                            "테스트셀러", null, "https://logo.url", "설명");

            ApplySellerApplicationApiRequest.AddressDetail address =
                    new ApplySellerApplicationApiRequest.AddressDetail("12345", "서울시", "상세주소");
            ApplySellerApplicationApiRequest.CsContactInfo csContact =
                    new ApplySellerApplicationApiRequest.CsContactInfo(
                            "02-1234-5678", "test@test.com", "010-1234-5678");
            ApplySellerApplicationApiRequest.BusinessInfo businessInfo =
                    new ApplySellerApplicationApiRequest.BusinessInfo(
                            "123-45-67890", "회사명", "대표자", null, address);
            ApplySellerApplicationApiRequest.ContactInfo contactInfo =
                    new ApplySellerApplicationApiRequest.ContactInfo("담당자", "010-0000-0000");
            ApplySellerApplicationApiRequest.AddressInfo returnAddress =
                    new ApplySellerApplicationApiRequest.AddressInfo(
                            "RETURN", "반품지", address, contactInfo);

            ApplySellerApplicationApiRequest request =
                    new ApplySellerApplicationApiRequest(
                            sellerInfo, businessInfo, csContact, returnAddress);

            // when
            ApplySellerApplicationCommand command = mapper.toCommand(request);

            // then
            assertThat(command.sellerInfo().displayName()).isEqualTo("테스트셀러");
        }
    }

    @Nested
    @DisplayName("toCommand(Long, ApproveSellerApplicationApiRequest)")
    class ToApproveCommandTest {

        @Test
        @DisplayName("승인 요청을 Command로 변환한다")
        void toCommand_Approve_Success() {
            // given
            Long applicationId = 1L;
            ApproveSellerApplicationApiRequest request =
                    SellerApplicationApiFixtures.approveRequest();

            // when
            ApproveSellerApplicationCommand command = mapper.toCommand(applicationId, request);

            // then
            assertThat(command.sellerApplicationId()).isEqualTo(applicationId);
            assertThat(command.processedBy()).isEqualTo(request.processedBy());
        }
    }

    @Nested
    @DisplayName("toCommand(Long, RejectSellerApplicationApiRequest)")
    class ToRejectCommandTest {

        @Test
        @DisplayName("거절 요청을 Command로 변환한다")
        void toCommand_Reject_Success() {
            // given
            Long applicationId = 1L;
            RejectSellerApplicationApiRequest request =
                    SellerApplicationApiFixtures.rejectRequest();

            // when
            RejectSellerApplicationCommand command = mapper.toCommand(applicationId, request);

            // then
            assertThat(command.sellerApplicationId()).isEqualTo(applicationId);
            assertThat(command.rejectionReason()).isEqualTo(request.rejectionReason());
            assertThat(command.processedBy()).isEqualTo(request.processedBy());
        }
    }
}
