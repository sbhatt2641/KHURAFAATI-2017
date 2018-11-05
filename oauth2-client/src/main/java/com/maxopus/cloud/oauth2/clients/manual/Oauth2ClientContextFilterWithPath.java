package com.maxopus.cloud.oauth2.clients.manual;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.web.util.UriComponentsBuilder;

class Oauth2ClientContextFilterWithPath extends OAuth2ClientContextFilter {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    protected void redirectUser(UserRedirectRequiredException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String redirectUri = e.getRedirectUri();
        final UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(redirectUri);
        final Map<String, String> requestParams = e.getRequestParams();
        for (Map.Entry<String, String> param : requestParams.entrySet()) {
        	String paramKey = param.getKey();
        	String paramValue = param.getValue();
        	if(paramKey.equals("redirect_uri")){
        		paramValue = paramValue.replace("/api", "");
        	}
            builder.queryParam(paramKey, paramValue);
        }

        if (e.getStateKey() != null) {
            builder.queryParam("state", e.getStateKey());
        }

        String url = /*getBaseUrl(request) +*/ builder.build().encode().toUriString();
        this.redirectStrategy.sendRedirect(request, response, url);
    }

    @Override
    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

    private String getBaseUrl(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        System.out.println("getBaseUrl: " + url);
        System.out.println("request.getRequestURI(): " + request.getRequestURI());
        System.out.println("request.getContextPath(): " + request.getContextPath());
        return url.substring(0, url.length() - request.getRequestURI().length() + request.getContextPath().length());
    }
}
