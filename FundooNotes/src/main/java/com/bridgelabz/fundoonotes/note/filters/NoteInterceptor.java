package com.bridgelabz.fundoonotes.note.filters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.bridgelabz.fundoonotes.user.filters.LoggerInterceptor;

public class NoteInterceptor extends HandlerInterceptorAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);

	 public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
             Object handler) throws Exception {
		 
		 request.getHeader("token");
		// tokenProvider.parseJWT(token);
		 
				return true;

}
	
}
