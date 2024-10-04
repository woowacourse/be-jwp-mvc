package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class HandlerExecution {

    private static final int VALID_PARAMETER_LENGTH = 2;
    private static final int REQUEST_PARAMETER_INDEX = 0;
    private static final int RESPONSE_PARAMETER_INDEX = 1;

    private final Object handler;
    private final Method method;

    public HandlerExecution(Object handler, Method method) {
        validateParameters(method.getParameters());
        validateReturnType(method.getReturnType());

        this.handler = handler;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response)
            throws IllegalAccessException, InvocationTargetException {
        Object invokeResult = method.invoke(handler, request, response);

        return (ModelAndView) invokeResult;
    }

    private void validateParameters(Parameter[] parameters) {
        validateParametersLength(parameters);
        validateRequestParameter(parameters[REQUEST_PARAMETER_INDEX]);
        validateResponseParameter(parameters[RESPONSE_PARAMETER_INDEX]);
    }

    private void validateParametersLength(Parameter[] parameters) {
        if (parameters.length != VALID_PARAMETER_LENGTH) {
            throw new IllegalArgumentException("핸들러 메소드의 인자수가 잘못되었습니다.");
        }
    }

    private void validateRequestParameter(Parameter parameter) {
        if (parameter.getType() != HttpServletRequest.class) {
            throw new IllegalArgumentException("핸들러 메소드의 첫번째 인자가 HttpServletRequest 타입이 아닙니다.");
        }
    }

    private void validateResponseParameter(Parameter parameter) {
        if (parameter.getType() != HttpServletResponse.class) {
            throw new IllegalArgumentException("핸들러 메소드의 두번째 인자가 HttpServletResponse 타입이 아닙니다.");
        }
    }

    private void validateReturnType(Class<?> returnType) {
        if (returnType != ModelAndView.class) {
            throw new IllegalArgumentException("핸들러 메소드의 반환 타입이 ModelAndView 타입이 아닙니다.");
        }
    }
}
