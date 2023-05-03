package ec.edu.ups.proyectointegrador2fa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ec.edu.ups.proyectointegrador2fa.dto.UserRegisteredDTO;
import ec.edu.ups.proyectointegrador2fa.entity.User;
import ec.edu.ups.proyectointegrador2fa.service.DefaultUserService;
import ec.edu.ups.proyectointegrador2fa.service.TwilioVerifyService;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private DefaultUserService defaultUserService;
    
    @Autowired
    private TwilioVerifyService twilioVerifyService;

    @ModelAttribute("user")
    public UserRegisteredDTO userRegistrationDto() {
        return new UserRegisteredDTO();
    }

    @GetMapping
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") UserRegisteredDTO registrationDto) {
        User usuario = defaultUserService.save(registrationDto);
        twilioVerifyService.startVerification(registrationDto.getCellPhone(), "sms");
        return "redirect:/verifyAccount/" + usuario.getId();
    }
}
