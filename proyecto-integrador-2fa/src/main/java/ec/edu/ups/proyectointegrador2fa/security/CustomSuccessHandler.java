package ec.edu.ups.proyectointegrador2fa.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import ec.edu.ups.proyectointegrador2fa.repository.UserRepository;
import ec.edu.ups.proyectointegrador2fa.entity.User;
import ec.edu.ups.proyectointegrador2fa.service.DefaultUserService;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private DefaultUserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String redirectUrl = null;
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepo.findByEmail(username);
        if (user != null && userService.generateOtpSendSMS(user) && user.isVerify()) {
            redirectUrl = "/login/otpVerification";
        } else if (user != null && !user.isVerify()) {
            redirectUrl = "/verifyAccount/" + user.getId();
        }

        new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

}
