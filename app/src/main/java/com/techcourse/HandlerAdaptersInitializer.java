package com.techcourse;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapters;
import com.interface21.webmvc.servlet.mvc.tobe.ManualHandlerAdapter;

public class HandlerAdaptersInitializer {

    public HandlerAdaptersInitializer() {
    }

    public HandlerAdapters initialize() {
        HandlerAdapters handlerAdapters = new HandlerAdapters();
        handlerAdapters.addHandlerAdapter(new ManualHandlerAdapter());
        handlerAdapters.addHandlerAdapter(new AnnotationHandlerAdapter());
        return handlerAdapters;
    }
}
