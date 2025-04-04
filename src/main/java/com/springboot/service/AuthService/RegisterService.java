package com.springboot.service.AuthService;

import com.springboot.Dtos.UserDtos;
import com.springboot.response.AuthenticationResponse;
import jakarta.mail.MessagingException;

public interface RegisterService {
    void createUser(UserDtos userDtos);
    AuthenticationResponse activateAccount(String token) throws MessagingException;
}
