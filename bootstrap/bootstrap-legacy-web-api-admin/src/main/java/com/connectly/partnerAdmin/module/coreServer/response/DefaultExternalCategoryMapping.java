package com.connectly.partnerAdmin.module.coreServer.response;

public record DefaultExternalCategoryMapping(
	long siteId,
	String externalCategoryId,
	String externalExtraCategoryId,
	String description,
	long categoryId
) {

}
