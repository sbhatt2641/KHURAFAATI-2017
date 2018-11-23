package com.maxopus.cloud.resourceserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.io.CharStreams;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class PostFilter extends ZuulFilter {
	private static Logger LOGGER = LoggerFactory.getLogger(PostFilter.class);

	  @Override
	  public String filterType() {
	    return "post";
	  }

	  @Override
	  public int filterOrder() {
	    return 1;
	  }

	  @Override
	  public boolean shouldFilter() {
	    return true;
	  }

	  @Override
	  public Object run() {
	    RequestContext ctx = RequestContext.getCurrentContext();
	    HttpServletResponse response = ctx.getResponse();
	    
	    try (
    		final InputStream responseDataStream = ctx.getResponseDataStream()) {
	    	final String responseData = CharStreams.toString(new InputStreamReader(responseDataStream, "UTF-8"));
    	    ctx.setResponseBody(responseData);
    	} catch (IOException e) {
    		LOGGER.warn("Error reading body",e);
    	}
	    
	    LOGGER.info("PostFilter: " + String.format("response's status is %s and content is %s", response.getStatus(), ctx.getResponseBody()));
	    return null;
	  }	 
}
