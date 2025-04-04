package com.springboot.controller;

import com.springboot.Dtos.OtpLoginRequest;
import com.springboot.Dtos.OtpRequestDto;
import com.springboot.Dtos.UserDtos;
import com.springboot.response.AuthenticationResponse;
import com.springboot.service.AuthService.LoginService;
import com.springboot.service.AuthService.RegisterService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private  final LoginService loginService;
    private  final RegisterService registerService;

    @PostMapping("/create")
    @Operation(summary = "Create a new user", description = "Creates a user and sends a verification email.")
    public ResponseEntity<String> createEmployee(@Valid @RequestBody UserDtos employeeDtos){
        this.registerService.createUser(employeeDtos);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Verification email sent. Please check your inbox.");
    }

    @GetMapping("/activate-account")
    @Operation(summary = "Activate user account", description = "Activates the user account using the verification token.")
    public ResponseEntity<AuthenticationResponse> activateAccount(@RequestParam String token) throws MessagingException {
        AuthenticationResponse response = registerService.activateAccount(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-login-otp")
    @Operation(summary = "Send OTP for login", description = "Generates and sends an OTP to the user's registered email.")
    public ResponseEntity<String> sendLoginOtp(@RequestBody OtpRequestDto requestDto) throws MessagingException {
        loginService.generateAndSendOtp(requestDto.getEmail());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("OTP sent to your email.");
    }

    @PostMapping("/login-with-otp")
    @Operation(summary = "Login using OTP", description = "Authenticates a user by verifying the OTP sent to their email.")
    public ResponseEntity<AuthenticationResponse> loginWithOtp(
            @RequestBody OtpLoginRequest otpLoginRequest
    ) throws MessagingException {
        AuthenticationResponse response = loginService.authenticateWithOtp(otpLoginRequest.getEmail(), otpLoginRequest.getOtp());
        return ResponseEntity.ok(response);
    }

}
