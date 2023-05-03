package ec.edu.ups.proyectointegrador2fa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ec.edu.ups.proyectointegrador2fa.repository.UserRepository;
import ec.edu.ups.proyectointegrador2fa.entity.User;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String viewHome(Model model) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails user = (UserDetails) securityContext.getAuthentication().getPrincipal();
        User users = userRepository.findByEmail(user.getUsername());
        if (users.isActive()) {
            model.addAttribute("userDetails", users.getName());
            return "home";
        } else {
            return "redirect:/login/otpVerification?error";
        }
    }

}
