package com.connectly.partnerAdmin.module.product.entity.option;

import java.util.LinkedHashSet;
import java.util.Set;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.product.enums.option.OptionName;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name = "option_group")
@Entity
public class OptionGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_group_id")
    private long id;

    @Setter
    @Column(name = "option_name")
    @Enumerated(EnumType.STRING)
    private OptionName optionName;

    @OneToMany(mappedBy = "optionGroup", orphanRemoval = true)
    private Set<OptionDetail> optionDetails = new LinkedHashSet<>();


    public OptionGroup(long id, OptionName optionName) {
        this.id = id;
        this.optionName = optionName;
        setDeleteYn(Yn.N);
    }

    public OptionGroup(OptionName optionName) {
        this.optionName = optionName;
    }

    public void addOptionDetail(OptionDetail optionDetail) {
            this.optionDetails.add(optionDetail);
        if (optionDetail.getOptionGroup() != this) {
            optionDetail.setOptionGroup(this);
        }
    }

    public void removeOptionDetail(OptionDetail optionDetail) {
        this.optionDetails.remove(optionDetail);
        if (optionDetail.getOptionGroup() == this) {
            optionDetail.setOptionGroup(null);
        }
    }

    @Override
    public int hashCode() {
        return  (optionName).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof OptionGroup p) {
            return this.hashCode()==p.hashCode();
        }
        return false;
    }

}