package ec.edu.ups.proyectointegrador2fa.controller;

import com.twilio.rest.verify.v2.service.VerificationCheck;
import ec.edu.ups.proyectointegrador2fa.dto.UserRegisteredDTO;
import ec.edu.ups.proyectointegrador2fa.entity.User;
import ec.edu.ups.proyectointegrador2fa.service.DefaultUserService;
import ec.edu.ups.proyectointegrador2fa.service.TwilioVerifyService;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/verifyAccount")
public class VerifyController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DefaultUserService defaultUserService;
    @Autowired
    private TwilioVerifyService twilioVerifyService;
    private User usuario;

    @GetMapping("/{id}")
    public String pageRegisterUser(@PathVariable Integer id, Model model) throws Exception {
        usuario = defaultUserService.findUsuarioById(id);
        UserRegisteredDTO usuarioDTO = new UserRegisteredDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setCellPhone(usuario.getCellPhone());
        model.addAttribute("usuario", usuarioDTO);
        return "verifyAccount";
    }

    @PostMapping("/validar/{id}")
    public String registrarCuentaDeUsuario(@PathVariable Integer id, @ModelAttribute("usuario") UserRegisteredDTO usuarioDTO, Model model) throws Exception {
        usuario = defaultUserService.findUsuarioById(id);
        VerificationCheck verificationCheck = twilioVerifyService.checkVerification(usuario.getCellPhone(), usuarioDTO.getOtp());
        if (verificationCheck.getValid() && "approved".equals(verificationCheck.getStatus())) {
            usuario.setVerify(true);
            defaultUserService.verifyAccountUser(usuario);
            return "redirect:/login";
        } else {
            return "redirect:/verifyAccount/" + id+"?error";
        }

    }

}
