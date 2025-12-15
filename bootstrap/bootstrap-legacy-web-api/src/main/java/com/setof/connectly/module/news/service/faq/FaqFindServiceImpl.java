package com.setof.connectly.module.news.service.faq;

import com.setof.connectly.module.news.dto.faq.FaqDto;
import com.setof.connectly.module.news.dto.faq.filter.FaqFilter;
import com.setof.connectly.module.news.repository.faq.FaqFindRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class FaqFindServiceImpl implements FaqFindService {

    private final FaqFindRepository faqFindRepository;

    @Cacheable(cacheNames = "faq", key = "#filter.faqType")
    @Override
    public List<FaqDto> fetchFaqDto(FaqFilter filter) {
        return faqFindRepository.fetchFaq(filter);
    }
}
