package com.setof.connectly.module.display.mapper.banner;

import com.setof.connectly.module.display.dto.banner.BannerItemDto;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class BannerMapperImpl implements BannerMapper {

    @Override
    public List<BannerItemDto> toBannerItems(List<BannerItemDto> bannerItems) {
        Map<Long, List<BannerItemDto>> bannerIdGroup =
                bannerItems.stream()
                        .collect(Collectors.groupingBy(BannerItemDto::getBannerId))
                        .entrySet()
                        .stream()
                        .sorted((e1, e2) -> e2.getKey().compareTo(e1.getKey()))
                        .collect(
                                Collectors.toMap(
                                        Map.Entry::getKey,
                                        Map.Entry::getValue,
                                        (e1, e2) -> e1, // 병합 함수, 이 경우 필요하지 않음
                                        LinkedHashMap::new // 순서를 유지하는 Map
                                        ));

        Optional<Map.Entry<Long, List<BannerItemDto>>> firstNonEmptyListEntry =
                bannerIdGroup.entrySet().stream()
                        .filter(entry -> !entry.getValue().isEmpty())
                        .findFirst();

        return firstNonEmptyListEntry.map(Map.Entry::getValue).orElse(new ArrayList<>());
    }
}
