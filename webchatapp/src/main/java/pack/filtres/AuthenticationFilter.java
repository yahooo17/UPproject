package pack.filtres;

import javax.servlet.http.HttpServlet;
import pack.servlets.Authentication;
import pack.utils.StaticKeyStorage;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * Created by ASUS on 15.05.2016.
 */
@WebFilter(value = "/*")
public class AuthenticationFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String uidParam = servletRequest.getParameter(Authentication.COOKIE_USER_ID);
        if (uidParam == null && servletRequest instanceof HttpServletRequest) {
            Cookie[] cookies = ((HttpServletRequest) servletRequest).getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(Authentication.COOKIE_USER_ID)) {
                    uidParam = cookie.getValue();
                }
            }
        }
        boolean authenticated = uidParam != null && !uidParam.equals("") && checkAuthenticated(uidParam);
        if (authenticated) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            servletResponse.getOutputStream().println("403, Forbidden");
        }
    }

    @Override
    public void destroy() {

    }
    private boolean checkAuthenticated(String uid) {
        return StaticKeyStorage.getUserByUid(uid) != null;
    }
}
