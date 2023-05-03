package ec.edu.ups.proyectointegrador2fa.service.impl;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.springframework.stereotype.Service;
import ec.edu.ups.proyectointegrador2fa.service.TwilioVerifyService;
import org.springframework.beans.factory.annotation.Value;

@Service
public class TwilioVerifyServiceImpl implements TwilioVerifyService {

    @Value("${twilio.account.sid}")
    private String accountSid;
    
    @Value("${twilio.auth.token}")
    private String authToken;
    
    @Value("${twilio.verify.service.sid}")
    private String verifyServiceSid;
    
    @Override
    public void startVerification(String phoneNumber, String channel) throws ApiException {
        Twilio.init(accountSid, authToken);
        Verification verification = Verification.creator(
                verifyServiceSid,
                phoneNumber,
                channel)
                .create();
    }

    @Override
    public VerificationCheck checkVerification(String phoneNumber, String code) throws ApiException {
        Twilio.init(accountSid, authToken);
        VerificationCheck verificationCheck = VerificationCheck.creator(
                verifyServiceSid,
                code)
                .setTo(phoneNumber)
                .create();
        return verificationCheck;
    }

}
