package ch.heigvd.amt.mvcProject.ui.web.filter;

import ch.heigvd.amt.mvcProject.application.authentication.CurrentUserDTO;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "AuthorizationFilter", urlPatterns = "/*")
public class AuthorizationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        if (isPublicRessource(request.getRequestURI())) {
            chain.doFilter(req, resp);
            return;
        }

        CurrentUserDTO currentUserDTO = (CurrentUserDTO) request.getSession().getAttribute("currentUser");

        if (currentUserDTO == null) {
            String targetUrl = request.getRequestURI();

            if (request.getQueryString() != null) {
                targetUrl += "?" + request.getQueryString();
            }

            request.getSession().setAttribute("targetUrl", targetUrl);

            ((HttpServletResponse) resp).sendRedirect("/login");

            return;
        }

        chain.doFilter(req, resp);
    }

    /**
     * @param URI
     * @return true if the URI given is a public ressource
     */
    private boolean isPublicRessource(String URI) {
        if (URI.startsWith("/assets"))
            return true;
        if (URI.startsWith("/login"))
            return true;
        if (URI.startsWith("/logout"))
            return true;
        if (URI.startsWith("/register"))
            return true;
        if (URI.startsWith("/browsing"))
            return true;
        if (URI.startsWith("/arquillian-managed/"))
            return true;
        if (URI.equals("/")) // Home page
            return true;
        return false;
    }
}
