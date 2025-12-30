package com.connectly.partnerAdmin.module.display.repository.component.banner;


import com.connectly.partnerAdmin.module.display.entity.component.item.BannerItem;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BannerItemJdbcRepositoryImpl implements BannerItemJdbcRepository{
    private final JdbcTemplate jdbcTemplate;
    @Override
    public void saveAll(List<BannerItem> bannerItems) {
        String sql = "INSERT INTO BANNER_ITEM (TITLE, IMAGE_URL, LINK_URL, DISPLAY_ORDER, DISPLAY_YN, BANNER_ID, DISPLAY_START_DATE, DISPLAY_END_DATE, WIDTH, HEIGHT, INSERT_OPERATOR, UPDATE_OPERATOR, INSERT_DATE, UPDATE_DATE) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();

        for (BannerItem bannerItem : bannerItems) {
            Object[] values = new Object[] {
                    bannerItem.getTitle(),
                    bannerItem.getImageUrl(),
                    bannerItem.getLinkUrl(),
                    bannerItem.getDisplayOrder(),
                    bannerItem.getDisplayYn().name(),
                    bannerItem.getBannerId(),
                    bannerItem.getDisplayPeriod().getDisplayStartDate(),
                    bannerItem.getDisplayPeriod().getDisplayEndDate(),
                    bannerItem.getImageSize().getWidth(),
                    bannerItem.getImageSize().getHeight(),
                    MDC.get("user") ==null ? "Anonymous" : MDC.get("user"),
                    MDC.get("user") ==null ? "Anonymous" : MDC.get("user"),
                    LocalDateTime.now(),
                    LocalDateTime.now()
            };
            batchArgs.add(values);
        }

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }


}
