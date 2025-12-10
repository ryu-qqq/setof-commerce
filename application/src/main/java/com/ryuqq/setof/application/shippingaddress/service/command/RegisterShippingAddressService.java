package com.ryuqq.setof.application.shippingaddress.service.command;

import com.ryuqq.setof.application.shippingaddress.assembler.ShippingAddressAssembler;
import com.ryuqq.setof.application.shippingaddress.dto.command.RegisterShippingAddressCommand;
import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResponse;
import com.ryuqq.setof.application.shippingaddress.factory.command.ShippingAddressCommandFactory;
import com.ryuqq.setof.application.shippingaddress.manager.command.ShippingAddressPersistenceManager;
import com.ryuqq.setof.application.shippingaddress.manager.query.ShippingAddressReadManager;
import com.ryuqq.setof.application.shippingaddress.port.in.command.RegisterShippingAddressUseCase;
import com.ryuqq.setof.domain.common.util.ClockHolder;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.exception.ShippingAddressLimitExceededException;
import com.ryuqq.setof.domain.shippingaddress.vo.ShippingAddressId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Register ShippingAddress Service
 *
 * <p>배송지 등록 UseCase 구현체
 *
 * <p>비즈니스 규칙:
 * <ul>
 *   <li>회원당 최대 5개까지만 등록 가능
 *   <li>기본 배송지로 등록 시 기존 기본 배송지 해제
 *   <li>첫 번째 배송지는 자동으로 기본 배송지로 설정
 * </ul>
 *
 * @author development-team
 * @since 1.0.0
 */
@Service
public class RegisterShippingAddressService implements RegisterShippingAddressUseCase {

    private final ShippingAddressReadManager shippingAddressReadManager;
    private final ShippingAddressPersistenceManager shippingAddressPersistenceManager;
    private final ShippingAddressCommandFactory shippingAddressCommandFactory;
    private final ShippingAddressAssembler shippingAddressAssembler;
    private final ClockHolder clockHolder;

    public RegisterShippingAddressService(
            ShippingAddressReadManager shippingAddressReadManager,
            ShippingAddressPersistenceManager shippingAddressPersistenceManager,
            ShippingAddressCommandFactory shippingAddressCommandFactory,
            ShippingAddressAssembler shippingAddressAssembler,
            ClockHolder clockHolder) {
        this.shippingAddressReadManager = shippingAddressReadManager;
        this.shippingAddressPersistenceManager = shippingAddressPersistenceManager;
        this.shippingAddressCommandFactory = shippingAddressCommandFactory;
        this.shippingAddressAssembler = shippingAddressAssembler;
        this.clockHolder = clockHolder;
    }

    @Override
    @Transactional
    public ShippingAddressResponse execute(RegisterShippingAddressCommand command) {
        validateAddressLimit(command);

        boolean shouldBeDefault = determineDefaultStatus(command);

        if (shouldBeDefault) {
            unsetCurrentDefaultIfExists(command);
        }

        ShippingAddress shippingAddress = createShippingAddress(command, shouldBeDefault);
        ShippingAddressId savedId = shippingAddressPersistenceManager.persist(shippingAddress);

        ShippingAddress savedAddress = shippingAddressReadManager.findById(savedId.value());
        return shippingAddressAssembler.toResponse(savedAddress);
    }

    private void validateAddressLimit(RegisterShippingAddressCommand command) {
        long currentCount = shippingAddressReadManager.countByMemberId(command.memberId());
        if (currentCount >= ShippingAddress.MAX_ADDRESS_COUNT) {
            throw new ShippingAddressLimitExceededException(
                    command.memberId(), ShippingAddress.MAX_ADDRESS_COUNT);
        }
    }

    private boolean determineDefaultStatus(RegisterShippingAddressCommand command) {
        long currentCount = shippingAddressReadManager.countByMemberId(command.memberId());
        return currentCount == 0 || command.isDefault();
    }

    private void unsetCurrentDefaultIfExists(RegisterShippingAddressCommand command) {
        shippingAddressReadManager
                .findDefaultByMemberId(command.memberId())
                .ifPresent(
                        currentDefault -> {
                            currentDefault.unsetDefault(clockHolder.getClock());
                            shippingAddressPersistenceManager.persist(currentDefault);
                        });
    }

    private ShippingAddress createShippingAddress(
            RegisterShippingAddressCommand command, boolean isDefault) {
        RegisterShippingAddressCommand adjustedCommand =
                RegisterShippingAddressCommand.of(
                        command.memberId(),
                        command.addressName(),
                        command.receiverName(),
                        command.receiverPhone(),
                        command.roadAddress(),
                        command.jibunAddress(),
                        command.detailAddress(),
                        command.zipCode(),
                        command.deliveryRequest(),
                        isDefault);
        return shippingAddressCommandFactory.create(adjustedCommand);
    }
}
