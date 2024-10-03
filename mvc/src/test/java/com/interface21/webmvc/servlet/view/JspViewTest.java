package com.interface21.webmvc.servlet.view;

import java.util.Map;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JspViewTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }

    @Test
    void renderJsp() throws Exception {
        // Given
        String viewName = "/user.jsp";
        JspView view = new JspView(viewName);

        // When
        view.render(Map.of("id", "123"), request, response);

        // Then
        verify(request).setAttribute("id", "123");
        verify(request).getRequestDispatcher("/user.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void redirect() throws Exception {
        // Given
        String viewName = "redirect:/";
        JspView view = new JspView(viewName);

        // When
        view.render(Map.of(), request, response);

        // Then
        verify(response).sendRedirect("/");
    }
}
