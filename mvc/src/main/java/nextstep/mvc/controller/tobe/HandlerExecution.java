package nextstep.mvc.controller.tobe;

import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

	private final Class<?> clazz;
	private final Method method;

	public HandlerExecution(Class<?> clazz, Method method) {
		this.clazz = clazz;
		this.method = method;
	}

	public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		final Object instance = clazz.getConstructor().newInstance();
		return (ModelAndView)method.invoke(instance, request,response);
	}
}
