package webmvc.org.springframework.web.servlet.view;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.View;

public class JspView implements View {

	public static final String REDIRECT_PREFIX = "redirect:";
	private static final Logger log = LoggerFactory.getLogger(JspView.class);
	private final String viewName;

	public JspView(final String viewName) {
		this.viewName = viewName;
	}

	@Override
	public void render(final Map<String, ?> model, final HttpServletRequest request,
		final HttpServletResponse response) throws Exception {
		// todo

		model.keySet().forEach(key -> {
			log.debug("attribute name : {}, value : {}", key, model.get(key));
			request.setAttribute(key, model.get(key));
		});

		// todo
	}

	@Override
	public String getName() {
		return viewName;
	}
}
