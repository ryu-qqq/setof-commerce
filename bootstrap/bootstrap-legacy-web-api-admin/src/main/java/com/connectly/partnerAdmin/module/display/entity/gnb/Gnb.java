package com.connectly.partnerAdmin.module.display.entity.gnb;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.entity.embedded.GnbDetails;
import jakarta.persistence.*;
import lombok.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "GNB")
@Entity
public class Gnb extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GNB_ID")
    private long id;

    @Embedded
    private GnbDetails gnbDetails;

    public void updateGnbDetails(GnbDetails gnbDetails) {
        this.gnbDetails = gnbDetails;
    }

    public void delete(){
        this.gnbDetails.setDisplayYn(Yn.N);
        setDeleteYn(Yn.Y);
    }
}
