package com.connectly.partnerAdmin.module.notification.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlimTalkTemplateCode {


    ORDER_ACCEPT(TemplateGroup.ORDER, MessageTarget.SELLER),
    ORDER_COMPLETE(TemplateGroup.ORDER, MessageTarget.CUSTOMER),
    CANCEL_ORDER_S(TemplateGroup.ORDER, MessageTarget.SELLER),
    CANCEL_REQUEST(TemplateGroup.ORDER, MessageTarget.CUSTOMER),

    RETURN_REQUEST(TemplateGroup.ORDER, MessageTarget.CUSTOMER),
    RETURN_REQUEST_S(TemplateGroup.ORDER, MessageTarget.SELLER),

    DELIVERY_START(TemplateGroup.ORDER, MessageTarget.CUSTOMER),

    RETURN_ACCEPT(TemplateGroup.ORDER, MessageTarget.CUSTOMER),
    RETURN_REJECT(TemplateGroup.ORDER, MessageTarget.CUSTOMER),
    CANCEL_SALE(TemplateGroup.ORDER, MessageTarget.CUSTOMER),
    CANCEL_COMPLETE(TemplateGroup.ORDER, MessageTarget.CUSTOMER),

    CS_PRODUCT_S(TemplateGroup.QNA, MessageTarget.CUSTOMER),
    CS_ORDER_S(TemplateGroup.QNA, MessageTarget.CUSTOMER),
    CS_PRODUCT(TemplateGroup.QNA, MessageTarget.SELLER),
    CS_ORDER(TemplateGroup.QNA, MessageTarget.SELLER),

    MEMBER_JOIN(TemplateGroup.MEMBER, MessageTarget.CUSTOMER),

    CANCEL_NOTIFY(TemplateGroup.ORDER, MessageTarget.CUSTOMER),
    CANCEL_ORDER_AUTO(TemplateGroup.ORDER, MessageTarget.SELLER),
    CANCEL_VCOMPLETE(TemplateGroup.ORDER, MessageTarget.CUSTOMER),

    MILEAGE_SOON_EXPIRE(TemplateGroup.MEMBER, MessageTarget.CUSTOMER),

    ;


    private final TemplateGroup templateGroup;
    private final MessageTarget messageTarget;





}
