package shiro.utilities;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CJsessionIdAvoiderFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		boolean allowFilterChain = redirectToAvoidJsessionId((HttpServletRequest) req, (HttpServletResponse) res);
		// if its redirected, then no need to continue processing the request
		if (allowFilterChain) {
			chain.doFilter(req, res);
		}
	}

	public static boolean redirectToAvoidJsessionId(HttpServletRequest req, HttpServletResponse res) {
		String requestURI = req.getRequestURI();
		if (requestURI.indexOf(";JSESSIONID=") > 0) {
			try {
				res.sendRedirect("/");
				return false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
	

}
