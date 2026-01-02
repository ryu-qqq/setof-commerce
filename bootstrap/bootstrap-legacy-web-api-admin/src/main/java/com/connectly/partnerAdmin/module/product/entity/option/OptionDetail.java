package com.connectly.partnerAdmin.module.product.entity.option;

import org.slf4j.MDC;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Table(name = "OPTION_DETAIL")
@Entity
public class OptionDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OPTION_DETAIL_ID")
    private long id;

    @Setter
    @Column(name = "OPTION_VALUE")
    private String optionValue;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "OPTION_GROUP_ID", referencedColumnName = "OPTION_GROUP_ID")
    private OptionGroup optionGroup;


    public OptionDetail(long id, String optionValue, OptionGroup optionGroup) {
        this.id = id;
        this.optionValue = optionValue;
        setOptionGroup(optionGroup);
        setDeleteYn(Yn.N);
        setInsertOperator(MDC.get("user") ==null ? "Anonymous" : MDC.get("user"));
        setUpdateOperator(MDC.get("user") ==null ? "Anonymous" : MDC.get("user"));
    }

    public OptionDetail(String optionValue, OptionGroup optionGroup) {
        this.optionValue = optionValue;
        setOptionGroup(optionGroup);
        setDeleteYn(Yn.N);
        setInsertOperator(MDC.get("user") ==null ? "Anonymous" : MDC.get("user"));
        setUpdateOperator(MDC.get("user") ==null ? "Anonymous" : MDC.get("user"));
    }

    public void setOptionGroup(OptionGroup optionGroup) {
        if (this.optionGroup != null) {
            this.optionGroup.getOptionDetails().remove(this);
        }
        this.optionGroup = optionGroup;
        if (optionGroup != null) {
            optionGroup.addOptionDetail(this);
        }
    }


    public void delete(){
        this.setDeleteYn(Yn.Y);
    }


    @Override
    public int hashCode() {
        return  (optionValue).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof OptionDetail p) {
            return this.hashCode()==p.hashCode();
        }
        return false;
    }

}

