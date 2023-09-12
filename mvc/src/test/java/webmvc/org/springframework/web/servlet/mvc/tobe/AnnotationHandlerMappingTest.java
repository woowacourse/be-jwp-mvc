package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class AnnotationHandlerMappingTest {

	private AnnotationHandlerMapping handlerMapping;

	@BeforeEach
	void setUp() {
		handlerMapping = new AnnotationHandlerMapping("samples");
		handlerMapping.initialize();
	}

	@Test
	void get() throws Exception {
		final var request = mock(HttpServletRequest.class);
		final var response = mock(HttpServletResponse.class);

		when(request.getAttribute("id")).thenReturn("gugu");
		when(request.getRequestURI()).thenReturn("/get-test");
		when(request.getMethod()).thenReturn("GET");

		final var handlerExecution = (HandlerExecution)handlerMapping.getHandler(request);
		final var modelAndView = handlerExecution.handle(request, response);

		assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
	}

	@Test
	void post() throws Exception {
		final var request = mock(HttpServletRequest.class);
		final var response = mock(HttpServletResponse.class);

		when(request.getAttribute("id")).thenReturn("gugu");
		when(request.getRequestURI()).thenReturn("/post-test");
		when(request.getMethod()).thenReturn("POST");

		final var handlerExecution = (HandlerExecution)handlerMapping.getHandler(request);
		final var modelAndView = handlerExecution.handle(request, response);

		assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
	}

	@Test
	void put() throws Exception {
		final var request = mock(HttpServletRequest.class);
		final var response = mock(HttpServletResponse.class);

		when(request.getAttribute("id")).thenReturn("gugu");
		when(request.getRequestURI()).thenReturn("/update-test");
		when(request.getMethod()).thenReturn("PUT");

		final var handlerExecution = (HandlerExecution)handlerMapping.getHandler(request);
		final var modelAndView = handlerExecution.handle(request, response);

		assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
	}

	@Test
	void patch() throws Exception {
		final var request = mock(HttpServletRequest.class);
		final var response = mock(HttpServletResponse.class);

		when(request.getAttribute("id")).thenReturn("gugu");
		when(request.getRequestURI()).thenReturn("/update-test");
		when(request.getMethod()).thenReturn("PATCH");

		final var handlerExecution = (HandlerExecution)handlerMapping.getHandler(request);
		final var modelAndView = handlerExecution.handle(request, response);

		assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
	}

	@Test
	void getWithClassRequestMapping() throws Exception {
		final var request = mock(HttpServletRequest.class);
		final var response = mock(HttpServletResponse.class);

		when(request.getAttribute("id")).thenReturn("gugu");
		when(request.getRequestURI()).thenReturn("/test/get-test");
		when(request.getMethod()).thenReturn("GET");

		final var handlerExecution = (HandlerExecution)handlerMapping.getHandler(request);
		final var modelAndView = handlerExecution.handle(request, response);

		assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
	}

	@Test
	void postWithRequestMapping() throws Exception {
		final var request = mock(HttpServletRequest.class);
		final var response = mock(HttpServletResponse.class);

		when(request.getAttribute("id")).thenReturn("gugu");
		when(request.getRequestURI()).thenReturn("/test");
		when(request.getMethod()).thenReturn("POST");

		final var handlerExecution = (HandlerExecution)handlerMapping.getHandler(request);
		final var modelAndView = handlerExecution.handle(request, response);

		assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
	}

}
