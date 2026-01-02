package com.ryuqq.setof.adapter.in.rest.v2.checkout.mapper;

import com.ryuqq.setof.adapter.in.rest.v2.checkout.dto.command.CompleteCheckoutV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.checkout.dto.command.CreateCheckoutItemV2ApiRequest;
import com.ryuqq.setof.adapter.in.rest.v2.checkout.dto.command.CreateCheckoutV2ApiRequest;
import com.ryuqq.setof.application.checkout.dto.command.CompleteCheckoutCommand;
import com.ryuqq.setof.application.checkout.dto.command.CreateCheckoutCommand;
import com.ryuqq.setof.application.checkout.dto.command.CreateCheckoutItemCommand;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Checkout V2 API Mapper
 *
 * <p>체크아웃 관련 API DTO ↔ Application Command 변환
 *
 * <p>컨벤션 규칙:
 *
 * <ul>
 *   <li>@Component로 DI (Static 금지)
 *   <li>비즈니스 로직 금지 - 순수 변환만
 * </ul>
 *
 * @author development-team
 * @since 2.0.0
 */
@Component
public class CheckoutV2ApiMapper {

    /**
     * 체크아웃 생성 요청 → 생성 커맨드 변환
     *
     * @param memberId 회원 ID (UUIDv7)
     * @param request API 요청
     * @return CreateCheckoutCommand
     */
    public CreateCheckoutCommand toCreateCommand(
            String memberId, CreateCheckoutV2ApiRequest request) {
        List<CreateCheckoutItemCommand> itemCommands =
                request.items().stream().map(this::toItemCommand).toList();

        return new CreateCheckoutCommand(
                request.idempotencyKey(),
                memberId,
                itemCommands,
                request.pgProvider(),
                request.paymentMethod(),
                request.receiverName(),
                request.receiverPhone(),
                request.zipCode(),
                request.address(),
                request.addressDetail(),
                request.deliveryRequest());
    }

    /**
     * 체크아웃 아이템 요청 → 아이템 커맨드 변환
     *
     * @param request 아이템 API 요청
     * @return CreateCheckoutItemCommand
     */
    private CreateCheckoutItemCommand toItemCommand(CreateCheckoutItemV2ApiRequest request) {
        return new CreateCheckoutItemCommand(
                request.productStockId(),
                request.productId(),
                request.sellerId(),
                request.quantity(),
                request.unitPrice(),
                request.productName(),
                request.productImage(),
                request.optionName(),
                request.brandName(),
                request.sellerName());
    }

    /**
     * 체크아웃 완료 요청 → 완료 커맨드 변환
     *
     * @param request API 요청
     * @return CompleteCheckoutCommand
     */
    public CompleteCheckoutCommand toCompleteCommand(CompleteCheckoutV2ApiRequest request) {
        return new CompleteCheckoutCommand(
                request.paymentId(), request.pgTransactionId(), request.approvedAmount());
    }
}
