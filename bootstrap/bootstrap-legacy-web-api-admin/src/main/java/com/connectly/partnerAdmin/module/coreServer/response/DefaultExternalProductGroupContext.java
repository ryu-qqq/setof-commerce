package com.connectly.partnerAdmin.module.coreServer.response;

import java.util.List;

public record DefaultExternalProductGroupContext(
	DefaultExternalProductGroup externalProductGroup,
	List<DefaultExternalProduct> externalProducts,
	DefaultExternalBrandMapping externalBrandMapping,
	DefaultExternalCategoryMapping externalCategoryMapping
) {


}
