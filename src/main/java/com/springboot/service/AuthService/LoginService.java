package com.springboot.service.AuthService;

import com.springboot.response.AuthenticationResponse;
import jakarta.mail.MessagingException;

public interface LoginService {

    void generateAndSendOtp(String email) throws MessagingException;
    AuthenticationResponse authenticateWithOtp(String email, String otp) throws MessagingException ;
}
