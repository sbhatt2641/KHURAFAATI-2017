package com.maxopus.cloud.resourceserver;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
* Validate JWT before forwarding call to apis.* 
*
*/
@Component
public class PreJWTValidationFilter extends ZuulFilter {
	 
	private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Value("${zuul.PreJWTValidationFilter.jwt.key}")
	private String jwtKey;
   
	@Value("${zuul.PreJWTValidationFilter.jwt.prefix}")
	private String jwtPrefix;
   
	@Override
	public boolean shouldFilter() {
		return true;
	}

	/**
	 * Validates JWT passed in header and returns 401 if not valid.
	 * @return null
	 */
	@Override
	public Object run() {
		
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		Enumeration<String> headers = request.getHeaderNames();
		StringBuilder headerStr = new StringBuilder();
		while(headers.hasMoreElements()){
			headerStr.append(" " + headers.nextElement());
		}
       
		LOGGER.info("PreFilter: " + String.format("%s request to %s with header%s", request.getMethod(), request.getRequestURL().toString(), headerStr));
		try {
			String jwt = ctx.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
			LOGGER.info("{} {}", jwtKey, jwt);
			//Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(jwt.replace(jwtPrefix, ""));
			
			//JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
	        //converter.setSigningKey("123");
	        //converter.extractAccessToken(value, map)
	        //Jwt jwt1 = JwtHelper.decode(jwt);
		} catch (Exception e) {
			e.printStackTrace();
    	   LOGGER.info("Error while validating JWT " + e);
           //ctx.setSendZuulResponse(false);
           //ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
		}
		return null;
	}   
   
	/**
	 * @return the filter type
	 */
	@Override
	public String filterType() {
		return "pre";
	}

	/**
	 * @return the filter order
	 */
	@Override
	public int filterOrder() {
		return 1; // Put after Logging filter
	}

	/**
	 * Returns the value for the given config key
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unused")
	private String getDynamicStringPropertyValue(String key) {
		DynamicStringProperty property = DynamicPropertyFactory.getInstance().getStringProperty(key, null);
		return property.getValue();
	}
}
