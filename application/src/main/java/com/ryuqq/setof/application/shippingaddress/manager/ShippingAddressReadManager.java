package com.ryuqq.setof.application.shippingaddress.manager;

import com.ryuqq.setof.application.shippingaddress.assembler.ShippingAddressAssembler;
import com.ryuqq.setof.application.shippingaddress.dto.response.ShippingAddressResult;
import com.ryuqq.setof.application.shippingaddress.port.out.ShippingAddressQueryPort;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddress;
import com.ryuqq.setof.domain.shippingaddress.aggregate.ShippingAddressBook;
import com.ryuqq.setof.domain.shippingaddress.exception.ShippingAddressNotFoundException;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * ShippingAddressReadManager - 배송지 조회 Manager.
 *
 * <p>QueryPort에 위임하고 도메인 예외를 던집니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
@Component
public class ShippingAddressReadManager {

    private final ShippingAddressQueryPort queryPort;
    private final ShippingAddressAssembler assembler;

    public ShippingAddressReadManager(
            ShippingAddressQueryPort queryPort, ShippingAddressAssembler assembler) {
        this.queryPort = queryPort;
        this.assembler = assembler;
    }

    public ShippingAddress getById(Long userId, Long shippingAddressId) {
        return queryPort
                .findById(userId, shippingAddressId)
                .orElseThrow(() -> new ShippingAddressNotFoundException(shippingAddressId));
    }

    public List<ShippingAddress> getAllByUserId(Long userId) {
        return queryPort.findAllByUserId(userId);
    }

    public ShippingAddressBook getBookByUserId(Long userId) {
        return ShippingAddressBook.of(queryPort.findAllByUserId(userId));
    }

    public int countByUserId(Long userId) {
        return queryPort.countByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<ShippingAddressResult> fetchShippingAddresses(Long userId) {
        List<ShippingAddress> addresses = queryPort.findAllByUserId(userId);
        return assembler.toResults(addresses);
    }

    @Transactional(readOnly = true)
    public ShippingAddressResult fetchShippingAddress(Long userId, Long shippingAddressId) {
        ShippingAddress address =
                queryPort
                        .findById(userId, shippingAddressId)
                        .orElseThrow(() -> new ShippingAddressNotFoundException(shippingAddressId));
        return assembler.toResult(address);
    }
}
