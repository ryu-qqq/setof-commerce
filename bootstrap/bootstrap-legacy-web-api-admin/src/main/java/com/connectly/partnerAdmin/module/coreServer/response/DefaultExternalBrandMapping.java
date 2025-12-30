package com.connectly.partnerAdmin.module.coreServer.response;


public record DefaultExternalBrandMapping(
	long siteId,
	long sellerId,
	String externalBrandId,
	long brandId
){

}
