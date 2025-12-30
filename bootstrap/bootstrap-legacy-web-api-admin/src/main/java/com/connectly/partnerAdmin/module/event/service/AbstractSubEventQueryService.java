package com.connectly.partnerAdmin.module.event.service;


import com.connectly.partnerAdmin.module.event.dto.SubEvent;
import org.springframework.stereotype.Service;

@Service
public abstract class AbstractSubEventQueryService <T extends SubEvent> implements SubEventQueryService<T> {



}
