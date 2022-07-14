package com.packt.rest.auth;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String token = request.getHeader("Authorization");
        String actualToken = "secrete staff";
        if(actualToken.equals(token)){
            filterChain.doFilter(request,response);
        }
        else {
            response.getWriter().println("Sorry,authentication required");
            response.setStatus(401);
        }
    }

    @Override
    public void destroy() {

    }
}
