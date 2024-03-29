package sample.instagram.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import sample.instagram.dto.ApiResponse;
import sample.instagram.dto.auth.AuthResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Configuration
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    private String returnUrl = "/";
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Object prevPageAttribute = request.getSession().getAttribute("prevPage");

        if (prevPageAttribute != null) {
            returnUrl = prevPageAttribute.toString();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(new ApiResponse<>(HttpStatus.OK.value(), "OK", new AuthResponse(returnUrl)));

        request.getSession(false).setMaxInactiveInterval(120*60);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print(jsonResponse);
        response.getWriter().flush();

    }
}
