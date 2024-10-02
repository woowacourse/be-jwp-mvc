package com.interface21.web.bind.annotation;

import java.util.Arrays;

public enum RequestMethod {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public static RequestMethod getRequestMethod(String method) {
        return Arrays.stream(values())
                .filter(value -> value.name().equals(method))
                .findFirst()
                .orElse(null);
    }
}
