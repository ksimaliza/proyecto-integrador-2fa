package ec.edu.ups.proyectointegrador2fa.service;

import com.twilio.exception.ApiException;
import com.twilio.rest.verify.v2.service.VerificationCheck;

/**
 *
 * @author ksimaliza
 */
public interface TwilioVerifyService {

    void startVerification(String phoneNumber, String channel) throws ApiException;
    VerificationCheck checkVerification(String phoneNumber, String code) throws ApiException;

}
