package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.mvc.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestMethod;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HandlerExecutionTest {

    @Test
    @DisplayName("정상적인 시그니처의 메서드인 경우 예외를 발생하지 않는다.")
    void test() throws NoSuchMethodException {
        Class<?> clazz = TestMethod.class;
        Method method = clazz.getDeclaredMethod("test", HttpServletRequest.class, HttpServletResponse.class);

        assertThatCode(() -> new HandlerExecution(clazz, method))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("질못된 갯수의 파라미터를 가진 메서드인 경우 예외를 발생한다.")
    void validateParameterSize() throws NoSuchMethodException {
        Class<?> clazz = TestMethod.class;
        Method method = clazz.getDeclaredMethod("wrongParameterSize", HttpServletRequest.class, HttpServletResponse.class, String.class);

        assertThatThrownBy(() -> new HandlerExecution(clazz, method))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid method parameter size:");
    }

    @Test
    @DisplayName("HttpServletRequest 타입의 파라미터를 가지지 않는 메서드인 경우 예외를 발생한다.")
    void validateRequestParameter() throws NoSuchMethodException {
        Class<?> clazz = TestMethod.class;
        Method method = clazz.getDeclaredMethod("wrongRequest", String.class, HttpServletResponse.class);

        assertThatThrownBy(() -> new HandlerExecution(clazz, method))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid method parameter type:");
    }

    @Test
    @DisplayName("HttpServletResponse 타입의 파라미터를 가지지 않는 메서드인 경우 예외를 발생한다.")
    void validateResponseParameter() throws NoSuchMethodException {
        Class<?> clazz = TestMethod.class;
        Method method = clazz.getDeclaredMethod("wrongResponse", HttpServletRequest.class, String.class);

        assertThatThrownBy(() -> new HandlerExecution(clazz, method))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid method parameter type:");
    }

    @Test
    @DisplayName("메서드의 리턴 타입이 ModelAndView 타입이 아닌 경우 예외를 발생한다.")
    void validateReturnType() throws NoSuchMethodException {
        Class<?> clazz = TestMethod.class;
        Method method = clazz.getDeclaredMethod("wrongReturnType", HttpServletRequest.class, HttpServletResponse.class);

        assertThatThrownBy(() -> new HandlerExecution(clazz, method))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid method return type:");
    }
}
