package ec.edu.ups.proyectointegrador2fa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ec.edu.ups.proyectointegrador2fa.repository.UserRepository;
import ec.edu.ups.proyectointegrador2fa.dto.UserLoginDTO;
import ec.edu.ups.proyectointegrador2fa.entity.User;
import ec.edu.ups.proyectointegrador2fa.service.DefaultUserService;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private DefaultUserService userService;

    @Autowired
    private UserRepository userRepo;

    @ModelAttribute("user")
    public UserLoginDTO userLoginDTO() {
        return new UserLoginDTO();
    }

    @GetMapping
    public String login() {
        return "login";
    }

    @PostMapping
    public void loginUser(@ModelAttribute("user") UserLoginDTO userLoginDTO) {
        userService.loadUserByUsername(userLoginDTO.getUsername());
    }

    @GetMapping("/otpVerification")
    public String otpSent(Model model, UserLoginDTO userLoginDTO) {
        model.addAttribute("otpValue", userLoginDTO);
        return "otp";

    }

    @PostMapping("/otpVerification")
    public String otpVerification(@ModelAttribute("otpValue") UserLoginDTO userLoginDTO) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails user = (UserDetails) securityContext.getAuthentication().getPrincipal();
        User users = userRepo.findByEmail(user.getUsername());
        if (users.getOtp().equals(userLoginDTO.getOtp())) {
            users.setActive(true);
            userRepo.save(users);
            return "redirect:/home";
        } else {
            return "redirect:/login/otpVerification?error";
        }
    }

}
