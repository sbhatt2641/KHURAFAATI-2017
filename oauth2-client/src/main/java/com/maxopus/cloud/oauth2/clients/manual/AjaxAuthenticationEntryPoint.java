package com.maxopus.cloud.oauth2.clients.manual;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

public class AjaxAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	private boolean forceHttps = false;

	private boolean useForward = false;
	
    public AjaxAuthenticationEntryPoint(String loginFormUrl) {
		super(loginFormUrl);
	}

	@Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

    	String redirectUrl = null;

		if (useForward) {

			if (forceHttps && "http".equals(request.getScheme())) {
				// First redirect the current request to HTTPS.
				// When that request is received, the forward to the login page will be
				// used.
				redirectUrl = buildHttpsRedirectUrlForRequest(request);
			}

			if (redirectUrl == null) {
				String loginForm = determineUrlToUseForThisRequest(request, response,
						authException);

				/*if (logger.isDebugEnabled()) {
					logger.debug("Server side forward to: " + loginForm);
				}*/

				RequestDispatcher dispatcher = request.getRequestDispatcher(loginForm);

				dispatcher.forward(request, response);

				return;
			}
		}
		else {
			// redirect to login page. Use https if forceHttps true

			redirectUrl = buildRedirectUrlToLoginPage(request, response, authException);

		}

		redirectStrategy.sendRedirect(request, response, redirectUrl);
    }
}
