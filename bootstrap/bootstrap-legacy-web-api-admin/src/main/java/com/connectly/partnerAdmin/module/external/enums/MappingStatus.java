package com.connectly.partnerAdmin.module.external.enums;

public enum MappingStatus {

    PENDING,
    ACTIVE,
    UPDATE,
    DE_ACTIVE,

    ;

    public boolean isActive(){
        return this.equals(ACTIVE);
    }

    public boolean isUpdate(){
        return this.equals(UPDATE);
    }

    public boolean isPending(){
        return this.equals(PENDING);
    }

}
