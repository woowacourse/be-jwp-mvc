package com.interface21.webmvc.servlet.mvc.handleradaptor;

import com.interface21.bean.container.BeanContainer;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import java.util.List;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapters() {
        BeanContainer beanContainer = BeanContainer.getInstance();
        this.handlerAdapters = beanContainer.getSubTypeBeansOf(HandlerAdapter.class);
    }

    public HandlerAdapter findHandlerAdaptor(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdaptor -> handlerAdaptor.supports(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하는 handlerAdaptor 가 없습니다."));
    }
}
