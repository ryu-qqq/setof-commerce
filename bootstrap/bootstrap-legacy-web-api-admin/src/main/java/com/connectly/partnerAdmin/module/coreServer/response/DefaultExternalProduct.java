package com.connectly.partnerAdmin.module.coreServer.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DefaultExternalProduct{

	private Long id;
	private String externalProductGroupId;
	private String externalProductId;
	private long productId;
	private String optionValue;
	private int quantity;
	private BigDecimal additionalPrice;
	private boolean soldOut;
	private boolean displayed;
	private boolean deleted;


}
