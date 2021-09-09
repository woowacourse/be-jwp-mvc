package com.techcourse.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.mockito.MockedStatic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    @Test
    @DisplayName("유저가 로그인되어 있다면 index.jsp 반환")
    void loggedInTest() {

        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        LoginController controller = new LoginController();

        try (MockedStatic<UserSession> session = mockStatic(UserSession.class)) {
            session.when(() -> UserSession.isLoggedIn(any())).thenReturn(true);

            // when
            final ModelAndView modelAndView = controller.execute(request, response);

            // then
            assertThat(modelAndView).usingRecursiveComparison().isEqualTo(new ModelAndView(new JspView("redirect:/index.jsp")));
        }
    }

    @Test
    @DisplayName("회원가입된 유저가 로그인 요청한다면 index.jsp 반환")
    void logInTest() {

        // given
        try (MockedStatic<UserSession> session = mockStatic(UserSession.class)) {
            session.when(() -> UserSession.isLoggedIn(any())).thenReturn(false);

            final HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getParameter("account")).thenReturn("gugu");
            when(request.getParameter("password")).thenReturn("password");
            when(request.getSession()).thenReturn(mock(HttpSession.class));

            final HttpServletResponse response = mock(HttpServletResponse.class);

            // when
            LoginController controller = new LoginController();
            final ModelAndView modelAndView = controller.execute(request, response);

            // then
            assertThat(modelAndView).usingRecursiveComparison().isEqualTo(new ModelAndView(new JspView("redirect:/index.jsp")));
        }
    }

    @Test
    @DisplayName("account와 일치하는 유저가 없다면 로그인 실패 테스트")
    void logInFailTest() {

        // given
        try (MockedStatic<UserSession> session = mockStatic(UserSession.class)) {
            session.when(() -> UserSession.isLoggedIn(any())).thenReturn(false);

            final HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getParameter("account")).thenReturn("seed");

            final HttpServletResponse response = mock(HttpServletResponse.class);

            // when
            LoginController controller = new LoginController();
            final ModelAndView modelAndView = controller.execute(request, response);

            // then
            assertThat(modelAndView).usingRecursiveComparison().isEqualTo(new ModelAndView(new JspView("redirect:/401.jsp")));
        }
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않는다면 로그인 실패 테스트")
    void wrongPasswordTest() {

        // given
        try (MockedStatic<UserSession> session = mockStatic(UserSession.class)) {
            session.when(() -> UserSession.isLoggedIn(any())).thenReturn(false);

            final HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getParameter("account")).thenReturn("gugu");
            when(request.getParameter("password")).thenReturn("wrong pass word");

            final HttpServletResponse response = mock(HttpServletResponse.class);

            // when
            LoginController controller = new LoginController();
            final ModelAndView modelAndView = controller.execute(request, response);

            // then
            assertThat(modelAndView).usingRecursiveComparison().isEqualTo(new ModelAndView(new JspView("redirect:/401.jsp")));
        }
    }
}
