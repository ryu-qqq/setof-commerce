package com.connectly.partnerAdmin.module.display.entity.content;

import com.connectly.partnerAdmin.module.common.entity.BaseEntity;
import com.connectly.partnerAdmin.module.common.enums.Yn;
import com.connectly.partnerAdmin.module.display.entity.embedded.DisplayPeriod;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;



@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "CONTENT")
@Entity
public class Content extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONTENT_ID")
    private long id;
    @Embedded
    private DisplayPeriod displayPeriod;
    private String title;
    private String memo;
    @Enumerated(EnumType.STRING)
    private Yn displayYn;
    private String imageUrl;


    @Builder
    public Content(long id, DisplayPeriod displayPeriod, String title, String memo, Yn displayYn, String imageUrl) {
        this.id = id;
        this.displayPeriod = displayPeriod;
        this.title = title;
        this.memo = memo;
        this.displayYn = displayYn;
        this.imageUrl = imageUrl;
    }

    public void updateDisplayYn(Yn displayYn){
        this.displayYn = displayYn;
    }
}

