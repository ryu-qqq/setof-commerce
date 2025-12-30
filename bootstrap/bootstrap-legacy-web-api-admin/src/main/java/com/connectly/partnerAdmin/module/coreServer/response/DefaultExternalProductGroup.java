package com.connectly.partnerAdmin.module.coreServer.response;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DefaultExternalProductGroup {

	private long siteId;
	private String siteName;
	private long sellerId;
	private String sellerName;
	private long productGroupId;
	private String externalProductGroupId;
	private long brandId;
	private String externalBrandId;
	private long categoryId;
	private String externalCategoryId;
	private String externalCategoryExtraId;
	private String description;
	private String productName;
	private BigDecimal regularPrice;
	private BigDecimal currentPrice;
	private String status;
	private boolean fixedPrice;
	private boolean soldOut;
	private boolean displayed;

}
