package com.ryuqq.setof.adapter.in.rest.admin.common;

import com.ryuqq.setof.application.product.port.in.command.RegisterFullProductUseCase;
import com.ryuqq.setof.application.product.port.in.command.UpdateFullProductUseCase;
import com.ryuqq.setof.application.product.port.in.command.UpdateProductGroupStatusUseCase;
import com.ryuqq.setof.application.product.port.in.query.GetProductGroupUseCase;
import com.ryuqq.setof.application.product.port.in.query.GetProductGroupsUseCase;
import com.ryuqq.setof.application.productdescription.port.in.command.UpdateProductDescriptionUseCase;
import com.ryuqq.setof.application.productdescription.port.in.query.GetProductDescriptionUseCase;
import com.ryuqq.setof.application.productimage.port.in.command.DeleteProductImageUseCase;
import com.ryuqq.setof.application.productimage.port.in.command.UpdateProductImageUseCase;
import com.ryuqq.setof.application.productimage.port.in.query.GetProductImageUseCase;
import com.ryuqq.setof.application.productnotice.port.in.command.UpdateProductNoticeUseCase;
import com.ryuqq.setof.application.productnotice.port.in.query.GetProductNoticeUseCase;
import com.ryuqq.setof.application.productstock.port.in.command.SetStockUseCase;
import com.ryuqq.setof.application.productstock.port.in.query.GetProductStockUseCase;
import com.ryuqq.setof.application.refundpolicy.port.in.command.DeleteRefundPolicyUseCase;
import com.ryuqq.setof.application.refundpolicy.port.in.command.RegisterRefundPolicyUseCase;
import com.ryuqq.setof.application.refundpolicy.port.in.command.SetDefaultRefundPolicyUseCase;
import com.ryuqq.setof.application.refundpolicy.port.in.command.UpdateRefundPolicyUseCase;
import com.ryuqq.setof.application.refundpolicy.port.in.query.GetRefundPoliciesUseCase;
import com.ryuqq.setof.application.refundpolicy.port.in.query.GetRefundPolicyUseCase;
import com.ryuqq.setof.application.seller.port.in.command.DeleteSellerUseCase;
import com.ryuqq.setof.application.seller.port.in.command.RegisterSellerUseCase;
import com.ryuqq.setof.application.seller.port.in.command.UpdateApprovalStatusUseCase;
import com.ryuqq.setof.application.seller.port.in.command.UpdateSellerUseCase;
import com.ryuqq.setof.application.seller.port.in.query.GetSellerUseCase;
import com.ryuqq.setof.application.seller.port.in.query.GetSellersUseCase;
import com.ryuqq.setof.application.shippingpolicy.port.in.command.DeleteShippingPolicyUseCase;
import com.ryuqq.setof.application.shippingpolicy.port.in.command.RegisterShippingPolicyUseCase;
import com.ryuqq.setof.application.shippingpolicy.port.in.command.SetDefaultShippingPolicyUseCase;
import com.ryuqq.setof.application.shippingpolicy.port.in.command.UpdateShippingPolicyUseCase;
import com.ryuqq.setof.application.shippingpolicy.port.in.query.GetShippingPoliciesUseCase;
import com.ryuqq.setof.application.shippingpolicy.port.in.query.GetShippingPolicyUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import java.time.Clock;
import java.time.ZoneId;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

/**
 * Admin API 통합 테스트용 Mock Bean 설정
 *
 * <p>외부 어댑터를 Mock으로 대체합니다.
 *
 * <p><strong>테스트 인증 지원:</strong>
 *
 * <ul>
 *   <li>Admin API는 별도의 인증 메커니즘을 사용합니다 (Basic Auth, API Key 등)
 *   <li>{@code TEST_ADMIN_TOKEN_PREFIX + adminId} 형식의 토큰을 유효한 토큰으로 인식
 *   <li>예: "TEST_ADMIN_TOKEN_1" → adminId = "1"
 * </ul>
 *
 * @author Development Team
 * @since 1.0.0
 * @see ApiIntegrationTestSupport
 */
@TestConfiguration
public class TestMockBeanConfig {

    /** Admin 테스트 토큰 접두사 */
    public static final String TEST_ADMIN_TOKEN_PREFIX = "TEST_ADMIN_TOKEN_";

    /** 기본 테스트 관리자 ID */
    public static final Long DEFAULT_TEST_ADMIN_ID = 1L;

    @Bean
    public ClockHolder clockHolder() {
        return () -> Clock.system(ZoneId.of("Asia/Seoul"));
    }

    // ===== Seller UseCase Mocks =====

    @MockBean private RegisterSellerUseCase registerSellerUseCase;

    @MockBean private GetSellerUseCase getSellerUseCase;

    @MockBean private GetSellersUseCase getSellersUseCase;

    @MockBean private UpdateSellerUseCase updateSellerUseCase;

    @MockBean private UpdateApprovalStatusUseCase updateApprovalStatusUseCase;

    @MockBean private DeleteSellerUseCase deleteSellerUseCase;

    // ===== ShippingPolicy UseCase Mocks =====

    @MockBean private RegisterShippingPolicyUseCase registerShippingPolicyUseCase;

    @MockBean private GetShippingPolicyUseCase getShippingPolicyUseCase;

    @MockBean private GetShippingPoliciesUseCase getShippingPoliciesUseCase;

    @MockBean private UpdateShippingPolicyUseCase updateShippingPolicyUseCase;

    @MockBean private SetDefaultShippingPolicyUseCase setDefaultShippingPolicyUseCase;

    @MockBean private DeleteShippingPolicyUseCase deleteShippingPolicyUseCase;

    // ===== RefundPolicy UseCase Mocks =====

    @MockBean private RegisterRefundPolicyUseCase registerRefundPolicyUseCase;

    @MockBean private GetRefundPolicyUseCase getRefundPolicyUseCase;

    @MockBean private GetRefundPoliciesUseCase getRefundPoliciesUseCase;

    @MockBean private UpdateRefundPolicyUseCase updateRefundPolicyUseCase;

    @MockBean private SetDefaultRefundPolicyUseCase setDefaultRefundPolicyUseCase;

    @MockBean private DeleteRefundPolicyUseCase deleteRefundPolicyUseCase;

    // ===== ProductStock UseCase Mocks =====

    @MockBean private GetProductStockUseCase getProductStockUseCase;

    @MockBean private SetStockUseCase setStockUseCase;

    // ===== ProductGroup UseCase Mocks =====

    @MockBean private GetProductGroupUseCase getProductGroupUseCase;

    @MockBean private GetProductGroupsUseCase getProductGroupsUseCase;

    @MockBean private RegisterFullProductUseCase registerFullProductUseCase;

    @MockBean private UpdateFullProductUseCase updateFullProductUseCase;

    @MockBean private UpdateProductGroupStatusUseCase updateProductGroupStatusUseCase;

    // ===== ProductDescription UseCase Mocks =====

    @MockBean private GetProductDescriptionUseCase getProductDescriptionUseCase;

    @MockBean private UpdateProductDescriptionUseCase updateProductDescriptionUseCase;

    // ===== ProductImage UseCase Mocks =====

    @MockBean private GetProductImageUseCase getProductImageUseCase;

    @MockBean private UpdateProductImageUseCase updateProductImageUseCase;

    @MockBean private DeleteProductImageUseCase deleteProductImageUseCase;

    // ===== ProductNotice UseCase Mocks =====

    @MockBean private GetProductNoticeUseCase getProductNoticeUseCase;

    @MockBean private UpdateProductNoticeUseCase updateProductNoticeUseCase;
}
