package com.care.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminAuthFilter implements Filter {

    private static final String ADMIN_TOKEN = "ghp_x7K83mBqT2xG9yLsAq94XoVpJmL2ZTQ9";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String authHeader = httpRequest.getHeader("Authorization");
        String requestUri = httpRequest.getRequestURI();

        if (requestUri.startsWith("/admin") && !ADMIN_TOKEN.equals(authHeader)) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("Unauthorized");
            return;
        }

        chain.doFilter(request, response);
    }
}
