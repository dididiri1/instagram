package sample.instagram.config.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Configuration
public class AuthSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String returnUrl = request.getSession().getAttribute("prevPage").toString();

        request.getSession(false).setMaxInactiveInterval(120*60);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print("{\"returnUrl\": \""+returnUrl+"\"}");
        response.getWriter().flush();

    }
}
