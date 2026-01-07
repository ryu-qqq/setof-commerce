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
        String sql = "INSERT INTO banner_item (title, image_url, link_url, display_order, display_yn, banner_id, display_start_date, display_end_date, width, height, insert_operator, update_operator, insert_date, update_date) " +
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
