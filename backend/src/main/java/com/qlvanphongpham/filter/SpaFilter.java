package com.qlvanphongpham.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * SPA Routing Filter — chuyển tiếp tất cả deep-link routes về index.html
 * để React Router xử lý client-side routing.
 */
public class SpaFilter implements Filter {

    private static final String[] STATIC_EXTENSIONS = {
        ".js", ".css", ".html", ".ico", ".png", ".jpg", ".jpeg",
        ".gif", ".svg", ".woff", ".woff2", ".ttf", ".eot", ".map", ".json"
    };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String relativePath = path.substring(contextPath.length());

        if (relativePath.startsWith("/api/")) {
            chain.doFilter(request, response);
            return;
        }
        if (isStaticFile(relativePath)) {
            chain.doFilter(request, response);
            return;
        }
        request.getRequestDispatcher("/index.html").forward(request, response);
    }

    private boolean isStaticFile(String path) {
        String lower = path.toLowerCase();
        for (String ext : STATIC_EXTENSIONS) {
            if (lower.endsWith(ext)) return true;
        }
        return false;
    }

    @Override public void init(FilterConfig cfg) {}
    @Override public void destroy() {}
}
