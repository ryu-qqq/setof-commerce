package com.ryuqq.setof.domain.shippingaddress.aggregate;

import com.ryuqq.setof.domain.shippingaddress.exception.ShippingAddressLimitExceededException;
import com.ryuqq.setof.domain.shippingaddress.exception.ShippingAddressNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 배송지 목록을 래핑하는 도메인 VO.
 *
 * <p>배송지 최대 개수 제한, 기본 배송지 관리 등의 비즈니스 규칙을 캡슐화합니다.
 *
 * @author ryu-qqq
 * @since 1.1.0
 */
public class ShippingAddressBook {

    private static final int MAX_SIZE = 5;

    private final List<ShippingAddress> addresses;

    private ShippingAddressBook(List<ShippingAddress> addresses) {
        this.addresses = addresses;
    }

    public static ShippingAddressBook of(List<ShippingAddress> addresses) {
        return new ShippingAddressBook(addresses);
    }

    public void validateCanAdd() {
        if (addresses.size() >= MAX_SIZE) {
            throw new ShippingAddressLimitExceededException();
        }
    }

    public ShippingAddress findById(Long shippingAddressId) {
        return addresses.stream()
                .filter(a -> a.idValue().equals(shippingAddressId))
                .findFirst()
                .orElseThrow(() -> new ShippingAddressNotFoundException(shippingAddressId));
    }

    /** 기존 기본 배송지를 해제하고, 변경된 주소 목록을 반환합니다. */
    public List<ShippingAddress> unmarkDefaults(Instant occurredAt) {
        List<ShippingAddress> changed = new ArrayList<>();
        for (ShippingAddress addr : addresses) {
            if (addr.isDefault()) {
                addr.unmarkAsDefault(occurredAt);
                changed.add(addr);
            }
        }
        return changed;
    }

    /** 삭제 후 다른 주소를 기본 배송지로 재지정하고, 변경된 주소를 반환합니다. */
    public Optional<ShippingAddress> reassignDefaultAfterRemove(
            Long removedId, Instant occurredAt) {
        return addresses.stream()
                .filter(a -> !a.idValue().equals(removedId))
                .findFirst()
                .map(
                        a -> {
                            a.markAsDefault(occurredAt);
                            return a;
                        });
    }

    public List<ShippingAddress> all() {
        return Collections.unmodifiableList(addresses);
    }

    public int size() {
        return addresses.size();
    }
}
