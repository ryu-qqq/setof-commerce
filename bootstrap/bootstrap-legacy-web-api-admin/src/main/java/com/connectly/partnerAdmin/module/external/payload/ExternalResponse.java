package com.connectly.partnerAdmin.module.external.payload;

public interface ExternalResponse <R>{

    R getData();
    String getResult();
    String getMessage();

}
