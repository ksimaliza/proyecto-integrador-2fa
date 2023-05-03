package ec.edu.ups.proyectointegrador2fa.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import ec.edu.ups.proyectointegrador2fa.dto.UserRegisteredDTO;
import ec.edu.ups.proyectointegrador2fa.entity.User;

public interface DefaultUserService extends UserDetailsService {

    User save(UserRegisteredDTO userRegisteredDTO);
    boolean generateOtpSendSMS(User user);
    User findUsuarioById(Integer id);
    User verifyAccountUser(User userVerification);

}
