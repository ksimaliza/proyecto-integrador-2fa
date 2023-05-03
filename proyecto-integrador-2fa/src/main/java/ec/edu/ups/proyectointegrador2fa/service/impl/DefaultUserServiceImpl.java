package ec.edu.ups.proyectointegrador2fa.service.impl;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ec.edu.ups.proyectointegrador2fa.repository.RoleRepository;
import ec.edu.ups.proyectointegrador2fa.repository.UserRepository;
import ec.edu.ups.proyectointegrador2fa.dto.UserRegisteredDTO;
import ec.edu.ups.proyectointegrador2fa.entity.Role;
import ec.edu.ups.proyectointegrador2fa.entity.User;
import ec.edu.ups.proyectointegrador2fa.service.DefaultUserService;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Service
public class DefaultUserServiceImpl implements DefaultUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
    }

    @Override
    public User save(UserRegisteredDTO userRegisteredDTO) {
        Role role = roleRepository.findByRole("USER");
        User user = new User();
        user.setEmail(userRegisteredDTO.getEmail());
        user.setName(userRegisteredDTO.getName());
        user.setPassword(passwordEncoder.encode(userRegisteredDTO.getPassword()));
        user.setCellPhone(userRegisteredDTO.getCellPhone());
        user.setRole(role);
        user.setVerify(userRegisteredDTO.isVerify());
        return userRepository.save(user);
    }

    @Override
    public boolean generateOtpSendSMS(User user) {
        boolean validar = false;
        try {
            String randomPIN = random(6);
            user.setOtp(randomPIN);
            userRepository.save(user);
            Twilio.init("ACe879ea744a81c9a29d7373b8873caaa5", "0edd1c5cf36b5bb80a8bfbf94c0a757d");
            Message.creator(new PhoneNumber(user.getCellPhone()), new PhoneNumber("+16205079164"), "UPS Código Generado 2FA:" + randomPIN).create();
            validar = true;
            return validar;
        } catch (Exception e) {
            return validar;
        }
    }
    
    public static String random(int size) {

        StringBuilder generatedToken = new StringBuilder();
        try {
            SecureRandom number = SecureRandom.getInstance("SHA1PRNG");
            // Generate 20 integers 0..20
            for (int i = 0; i < size; i++) {
                generatedToken.append(number.nextInt(9));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return generatedToken.toString();
    }

    @Override
    public User findUsuarioById(Integer id) {
        return userRepository.findUsuarioById(id);
    }

    @Override
    public User verifyAccountUser(User userVerification) {
        return userRepository.save(userVerification);
    }

}
