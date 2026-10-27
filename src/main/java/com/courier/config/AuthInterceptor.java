package com.courier.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Intercepts all requests and redirects unauthenticated users to the login page.
 * Allows access to /login, /logout, and static resources.
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String path = request.getRequestURI();

        // Allow login/logout and static resources
        if (path.equals("/login") || path.equals("/logout") || path.startsWith("/css/")
                || path.startsWith("/js/") || path.startsWith("/images/") || path.startsWith("/favicon")) {
            return true;
        }

        HttpSession session = request.getSession(false);
        if (session != null && Boolean.TRUE.equals(session.getAttribute("loggedIn"))) {
            return true;
        }

        response.sendRedirect(request.getContextPath() + "/login");
        return false;
    }
}
