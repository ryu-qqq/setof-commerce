package com.connectly.partnerAdmin.module.notification.core;

import java.util.List;

public interface MessageConversion <T> {
    List<? extends MessageContext> convert(T t);


}
