package com.ryuqq.setof.adapter.in.rest.admin.v1.discount.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ryuqq.setof.adapter.in.rest.admin.discount.DiscountPolicyApiFixtures;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.CreateDiscountFromExcelV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.CreateDiscountTargetV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.CreateDiscountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.UpdateDiscountStatusV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.command.UpdateDiscountV1ApiRequest;
import com.ryuqq.setof.adapter.in.rest.admin.v1.discount.dto.response.DiscountPolicyV1ApiResponse;
import com.ryuqq.setof.application.discount.dto.command.CreateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.ModifyDiscountTargetsCommand;
import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountPolicyCommand;
import com.ryuqq.setof.application.discount.dto.command.UpdateDiscountPolicyStatusCommand;
import com.ryuqq.setof.application.discount.dto.response.DiscountPolicyResult;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * DiscountPolicyCommandApiMapper 단위 테스트.
 *
 * <p>v1 레거시 Discount Command API Mapper의 변환 로직을 테스트합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Tag("unit")
@DisplayName("DiscountPolicyCommandApiMapper 단위 테스트")
class DiscountPolicyCommandApiMapperTest {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private DiscountPolicyCommandApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new DiscountPolicyCommandApiMapper();
    }

    @Nested
    @DisplayName("toCommand(CreateDiscountV1ApiRequest)")
    class ToCreateCommandTest {

        @Test
        @DisplayName("비율 할인 요청을 CreateDiscountPolicyCommand로 변환한다")
        void toCommand_RateDiscount_Success() {
            // given
            CreateDiscountV1ApiRequest request = DiscountPolicyApiFixtures.createRequest();

            // when
            CreateDiscountPolicyCommand command = mapper.toCommand(request);

            // then
            assertThat(command.name()).isEqualTo(DiscountPolicyApiFixtures.DEFAULT_POLICY_NAME);
            assertThat(command.description()).isEqualTo(DiscountPolicyApiFixtures.DEFAULT_MEMO);
            assertThat(command.discountMethod()).isEqualTo("RATE");
            assertThat(command.discountRate())
                    .isEqualTo(DiscountPolicyApiFixtures.DEFAULT_DISCOUNT_RATIO);
            assertThat(command.discountAmount()).isNull();
        }

        @Test
        @DisplayName("discountType RATE를 discountMethod RATE로 변환한다")
        void toCommand_RateType_ToRateMethod() {
            // given
            CreateDiscountV1ApiRequest request = DiscountPolicyApiFixtures.createRequest();

            // when
            CreateDiscountPolicyCommand command = mapper.toCommand(request);

            // then
            assertThat(command.discountMethod()).isEqualTo("RATE");
            assertThat(command.discountRate()).isNotNull();
            assertThat(command.discountAmount()).isNull();
        }

        @Test
        @DisplayName("discountType PRICE를 discountMethod FIXED_AMOUNT로 변환한다")
        void toCommand_PriceType_ToFixedAmountMethod() {
            // given
            CreateDiscountV1ApiRequest request = DiscountPolicyApiFixtures.createRequestPrice();

            // when
            CreateDiscountPolicyCommand command = mapper.toCommand(request);

            // then
            assertThat(command.discountMethod()).isEqualTo("FIXED_AMOUNT");
            assertThat(command.discountAmount()).isNotNull();
            assertThat(command.discountRate()).isNull();
        }

        @Test
        @DisplayName("issueType PRODUCT를 targetType PRODUCT로 변환한다")
        void toCommand_IssueTypeProduct_ToProduct() {
            // given
            CreateDiscountV1ApiRequest request = DiscountPolicyApiFixtures.createRequest();

            // when
            CreateDiscountPolicyCommand command = mapper.toCommand(request);

            // then
            assertThat(command.targets()).hasSize(1);
            assertThat(command.targets().get(0).targetType()).isEqualTo("PRODUCT");
        }

        @Test
        @DisplayName("publisherType ADMIN을 stackingGroup PLATFORM_INSTANT으로 변환한다")
        void toCommand_PublisherTypeAdmin_ToPlatformInstant() {
            // given
            CreateDiscountV1ApiRequest request = DiscountPolicyApiFixtures.createRequest();

            // when
            CreateDiscountPolicyCommand command = mapper.toCommand(request);

            // then
            assertThat(command.stackingGroup()).isEqualTo("PLATFORM_INSTANT");
        }

        @Test
        @DisplayName("publisherType SELLER를 stackingGroup SELLER_INSTANT으로 변환한다")
        void toCommand_PublisherTypeSeller_ToSellerInstant() {
            // given
            CreateDiscountV1ApiRequest request =
                    new CreateDiscountV1ApiRequest(
                            null, DiscountPolicyApiFixtures.discountDetailsSeller());

            // when
            CreateDiscountPolicyCommand command = mapper.toCommand(request);

            // then
            assertThat(command.stackingGroup()).isEqualTo("SELLER_INSTANT");
        }

        @Test
        @DisplayName("applicationType은 INSTANT로 고정 변환한다")
        void toCommand_ApplicationType_FixedToInstant() {
            // given
            CreateDiscountV1ApiRequest request = DiscountPolicyApiFixtures.createRequest();

            // when
            CreateDiscountPolicyCommand command = mapper.toCommand(request);

            // then
            assertThat(command.applicationType()).isEqualTo("INSTANT");
        }

        @Test
        @DisplayName("discountLimitYn Y를 discountCapped=true로 변환한다")
        void toCommand_DiscountLimitYnY_DiscountCappedTrue() {
            // given
            CreateDiscountV1ApiRequest request = DiscountPolicyApiFixtures.createRequest();

            // when
            CreateDiscountPolicyCommand command = mapper.toCommand(request);

            // then
            assertThat(command.discountCapped()).isTrue();
        }

        @Test
        @DisplayName("LocalDateTime을 KST 기준 Instant로 변환한다")
        void toCommand_LocalDateTimeToKstInstant() {
            // given
            CreateDiscountV1ApiRequest request = DiscountPolicyApiFixtures.createRequest();
            var expectedStart =
                    DiscountPolicyApiFixtures.DEFAULT_START_DATE.atZone(KST).toInstant();
            var expectedEnd = DiscountPolicyApiFixtures.DEFAULT_END_DATE.atZone(KST).toInstant();

            // when
            CreateDiscountPolicyCommand command = mapper.toCommand(request);

            // then
            assertThat(command.startAt()).isEqualTo(expectedStart);
            assertThat(command.endAt()).isEqualTo(expectedEnd);
        }

        @Test
        @DisplayName("maxDiscountPrice를 maxDiscountAmount(int)로 변환한다")
        void toCommand_MaxDiscountPrice_ToMaxDiscountAmount() {
            // given
            CreateDiscountV1ApiRequest request = DiscountPolicyApiFixtures.createRequest();

            // when
            CreateDiscountPolicyCommand command = mapper.toCommand(request);

            // then
            assertThat(command.maxDiscountAmount())
                    .isEqualTo((int) DiscountPolicyApiFixtures.DEFAULT_MAX_DISCOUNT_PRICE);
        }
    }

    @Nested
    @DisplayName("toCommand(long, UpdateDiscountV1ApiRequest)")
    class ToUpdateCommandTest {

        @Test
        @DisplayName("PathVariable ID와 수정 요청을 UpdateDiscountPolicyCommand로 변환한다")
        void toCommand_Update_Success() {
            // given
            long policyId = DiscountPolicyApiFixtures.DEFAULT_DISCOUNT_POLICY_ID;
            UpdateDiscountV1ApiRequest request = DiscountPolicyApiFixtures.updateRequest();

            // when
            UpdateDiscountPolicyCommand command = mapper.toCommand(policyId, request);

            // then
            assertThat(command.discountPolicyId()).isEqualTo(policyId);
            assertThat(command.name()).isEqualTo(DiscountPolicyApiFixtures.DEFAULT_POLICY_NAME);
            assertThat(command.description()).isEqualTo(DiscountPolicyApiFixtures.DEFAULT_MEMO);
        }

        @Test
        @DisplayName("수정 요청의 discountType RATE를 discountMethod RATE로 변환한다")
        void toCommand_Update_RateType_ToRateMethod() {
            // given
            long policyId = 1L;
            UpdateDiscountV1ApiRequest request = DiscountPolicyApiFixtures.updateRequest();

            // when
            UpdateDiscountPolicyCommand command = mapper.toCommand(policyId, request);

            // then
            assertThat(command.discountMethod()).isEqualTo("RATE");
            assertThat(command.discountRate()).isNotNull();
            assertThat(command.discountAmount()).isNull();
        }

        @Test
        @DisplayName("수정 요청의 LocalDateTime을 KST 기준 Instant로 변환한다")
        void toCommand_Update_LocalDateTimeToKstInstant() {
            // given
            long policyId = 1L;
            UpdateDiscountV1ApiRequest request = DiscountPolicyApiFixtures.updateRequest();
            var expectedStart =
                    DiscountPolicyApiFixtures.DEFAULT_START_DATE.atZone(KST).toInstant();
            var expectedEnd = DiscountPolicyApiFixtures.DEFAULT_END_DATE.atZone(KST).toInstant();

            // when
            UpdateDiscountPolicyCommand command = mapper.toCommand(policyId, request);

            // then
            assertThat(command.startAt()).isEqualTo(expectedStart);
            assertThat(command.endAt()).isEqualTo(expectedEnd);
        }

        @Test
        @DisplayName("다른 policyId도 올바르게 변환한다")
        void toCommand_Update_DifferentId() {
            // given
            long policyId = 99L;
            UpdateDiscountV1ApiRequest request = DiscountPolicyApiFixtures.updateRequest();

            // when
            UpdateDiscountPolicyCommand command = mapper.toCommand(policyId, request);

            // then
            assertThat(command.discountPolicyId()).isEqualTo(99L);
        }
    }

    @Nested
    @DisplayName("toCommand(UpdateDiscountStatusV1ApiRequest)")
    class ToUpdateStatusCommandTest {

        @Test
        @DisplayName("활성화 요청을 UpdateDiscountPolicyStatusCommand로 변환한다")
        void toCommand_Activate_Success() {
            // given
            UpdateDiscountStatusV1ApiRequest request =
                    DiscountPolicyApiFixtures.updateStatusRequest();

            // when
            UpdateDiscountPolicyStatusCommand command = mapper.toCommand(request);

            // then
            assertThat(command.policyIds()).isEqualTo(List.of(1L, 2L, 3L));
            assertThat(command.active()).isTrue();
        }

        @Test
        @DisplayName("비활성화 요청(activeYn=N)을 active=false로 변환한다")
        void toCommand_Deactivate_ActiveFalse() {
            // given
            UpdateDiscountStatusV1ApiRequest request =
                    DiscountPolicyApiFixtures.updateStatusRequestDeactivate();

            // when
            UpdateDiscountPolicyStatusCommand command = mapper.toCommand(request);

            // then
            assertThat(command.active()).isFalse();
        }

        @Test
        @DisplayName("활성화 요청(activeYn=Y)을 active=true로 변환한다")
        void toCommand_Activate_ActiveTrue() {
            // given
            UpdateDiscountStatusV1ApiRequest request =
                    DiscountPolicyApiFixtures.updateStatusRequestSingle(1L, "Y");

            // when
            UpdateDiscountPolicyStatusCommand command = mapper.toCommand(request);

            // then
            assertThat(command.active()).isTrue();
        }
    }

    @Nested
    @DisplayName("toCommand(long, CreateDiscountTargetV1ApiRequest)")
    class ToModifyTargetsCommandTest {

        @Test
        @DisplayName("PRODUCT issueType을 PRODUCT targetType으로 변환한다")
        void toCommand_ProductIssueType_ToProduct() {
            // given
            long policyId = 1L;
            CreateDiscountTargetV1ApiRequest request =
                    DiscountPolicyApiFixtures.createTargetRequest();

            // when
            ModifyDiscountTargetsCommand command = mapper.toCommand(policyId, request);

            // then
            assertThat(command.discountPolicyId()).isEqualTo(policyId);
            assertThat(command.targetType()).isEqualTo("PRODUCT");
            assertThat(command.targetIds()).isEqualTo(List.of(101L, 102L, 103L));
        }

        @Test
        @DisplayName("SELLER issueType을 그대로 SELLER로 변환한다")
        void toCommand_SellerIssueType_ToSeller() {
            // given
            long policyId = 2L;
            CreateDiscountTargetV1ApiRequest request =
                    DiscountPolicyApiFixtures.createTargetRequestSeller();

            // when
            ModifyDiscountTargetsCommand command = mapper.toCommand(policyId, request);

            // then
            assertThat(command.targetType()).isEqualTo("SELLER");
        }

        @Test
        @DisplayName("BRAND issueType을 그대로 BRAND로 변환한다")
        void toCommand_BrandIssueType_ToBrand() {
            // given
            long policyId = 3L;
            CreateDiscountTargetV1ApiRequest request =
                    DiscountPolicyApiFixtures.createTargetRequestBrand();

            // when
            ModifyDiscountTargetsCommand command = mapper.toCommand(policyId, request);

            // then
            assertThat(command.targetType()).isEqualTo("BRAND");
            assertThat(command.targetIds()).isEqualTo(List.of(301L, 302L));
        }
    }

    @Nested
    @DisplayName("toCommands(List<CreateDiscountFromExcelV1ApiRequest>)")
    class ToCommandsFromExcelTest {

        @Test
        @DisplayName("엑셀 요청 목록을 CreateDiscountPolicyCommand 목록으로 변환한다")
        void toCommands_Excel_Success() {
            // given
            List<CreateDiscountFromExcelV1ApiRequest> requests =
                    DiscountPolicyApiFixtures.excelRequests();

            // when
            List<CreateDiscountPolicyCommand> commands = mapper.toCommands(requests);

            // then
            assertThat(commands).hasSize(2);
        }

        @Test
        @DisplayName("엑셀 요청의 discountType RATE를 RATE로 변환한다")
        void toCommands_Excel_RateDiscount() {
            // given
            List<CreateDiscountFromExcelV1ApiRequest> requests =
                    List.of(DiscountPolicyApiFixtures.excelRequest());

            // when
            List<CreateDiscountPolicyCommand> commands = mapper.toCommands(requests);

            // then
            assertThat(commands.get(0).discountMethod()).isEqualTo("RATE");
            assertThat(commands.get(0).applicationType()).isEqualTo("INSTANT");
        }

        @Test
        @DisplayName("단건 엑셀 요청도 올바르게 변환한다")
        void toCommands_SingleExcel_Success() {
            // given
            List<CreateDiscountFromExcelV1ApiRequest> requests =
                    List.of(DiscountPolicyApiFixtures.excelRequest());

            // when
            List<CreateDiscountPolicyCommand> commands = mapper.toCommands(requests);

            // then
            assertThat(commands).hasSize(1);
            assertThat(commands.get(0).name())
                    .isEqualTo(DiscountPolicyApiFixtures.DEFAULT_POLICY_NAME);
        }
    }

    @Nested
    @DisplayName("toResponse(DiscountPolicyResult)")
    class ToResponseTest {

        @Test
        @DisplayName("RATE 방식 결과를 DiscountPolicyV1ApiResponse로 변환한다")
        void toResponse_Rate_Success() {
            // given
            DiscountPolicyResult result =
                    DiscountPolicyApiFixtures.discountPolicyResult(
                            DiscountPolicyApiFixtures.DEFAULT_DISCOUNT_POLICY_ID);

            // when
            DiscountPolicyV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.discountPolicyId())
                    .isEqualTo(DiscountPolicyApiFixtures.DEFAULT_DISCOUNT_POLICY_ID);
            assertThat(response.discountDetails().discountType()).isEqualTo("RATE");
            assertThat(response.discountDetails().discountRatio())
                    .isEqualTo(DiscountPolicyApiFixtures.DEFAULT_DISCOUNT_RATIO);
        }

        @Test
        @DisplayName("discountMethod FIXED_AMOUNT를 discountType PRICE로 역변환한다")
        void toResponse_FixedAmount_ToPriceType() {
            // given
            DiscountPolicyResult result =
                    DiscountPolicyApiFixtures.discountPolicyResultFixedAmount(2L);

            // when
            DiscountPolicyV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.discountDetails().discountType()).isEqualTo("PRICE");
            assertThat(response.discountDetails().discountRatio()).isEqualTo(5000.0);
        }

        @Test
        @DisplayName("discountCapped=true를 discountLimitYn Y로 역변환한다")
        void toResponse_DiscountCappedTrue_ToDiscountLimitYnY() {
            // given
            DiscountPolicyResult result = DiscountPolicyApiFixtures.discountPolicyResult(1L);

            // when
            DiscountPolicyV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.discountDetails().discountLimitYn()).isEqualTo("Y");
        }

        @Test
        @DisplayName("active=true를 activeYn Y로 역변환한다")
        void toResponse_ActiveTrue_ToActiveYnY() {
            // given
            DiscountPolicyResult result = DiscountPolicyApiFixtures.discountPolicyResult(1L);

            // when
            DiscountPolicyV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.discountDetails().activeYn()).isEqualTo("Y");
        }

        @Test
        @DisplayName("targets의 첫 번째 PRODUCT targetType을 issueType PRODUCT로 역변환한다")
        void toResponse_TargetProduct_ToIssueTypeProduct() {
            // given
            DiscountPolicyResult result = DiscountPolicyApiFixtures.discountPolicyResult(1L);

            // when
            DiscountPolicyV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.discountDetails().issueType()).isEqualTo("PRODUCT");
        }

        @Test
        @DisplayName("targets가 빈 목록이면 issueType을 null로 반환한다")
        void toResponse_EmptyTargets_IssueTypeNull() {
            // given
            DiscountPolicyResult result =
                    DiscountPolicyApiFixtures.discountPolicyResultEmptyTargets(1L);

            // when
            DiscountPolicyV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.discountDetails().issueType()).isNull();
        }

        @Test
        @DisplayName("insertOperator와 updateOperator는 system으로 고정 반환한다")
        void toResponse_Operators_FixedToSystem() {
            // given
            DiscountPolicyResult result = DiscountPolicyApiFixtures.discountPolicyResult(1L);

            // when
            DiscountPolicyV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.insertOperator()).isEqualTo("system");
            assertThat(response.updateOperator()).isEqualTo("system");
        }

        @Test
        @DisplayName("shareYn과 shareRatio는 고정값(N, 0.0)으로 반환한다")
        void toResponse_Share_FixedValues() {
            // given
            DiscountPolicyResult result = DiscountPolicyApiFixtures.discountPolicyResult(1L);

            // when
            DiscountPolicyV1ApiResponse response = mapper.toResponse(result);

            // then
            assertThat(response.discountDetails().shareYn()).isEqualTo("N");
            assertThat(response.discountDetails().shareRatio()).isEqualTo(0.0);
        }
    }
}
