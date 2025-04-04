package com.springboot.service.AuthService;


import com.springboot.SpringSecurityConfig.JwtService;
import com.springboot.entities.Token;
import com.springboot.exception.ResourceNotFoundException;
import com.springboot.repository.AuthRepository.TokenRepository;
import com.springboot.repository.AuthRepository.UserRepository;
import com.springboot.response.AuthenticationResponse;
import com.springboot.service.EmailService.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

import com.springboot.enums.EmailTemplateName;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private static final int MAX_OTP_ATTEMPTS = 5;
    private static final Duration OTP_EXPIRATION = Duration.ofMinutes(10);


    @Override
    public void generateAndSendOtp(String email) throws MessagingException {
        var user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getAccountLocked()) {
            throw new LockedException("Your account is locked due to multiple failed OTP attempts. Try again later.");
        }

        // ✅ Check rate limit (1 request per minute)
        Token lastToken = tokenRepository.findTopByUserOrderByOtpGeneratedAtDesc(user);  // Get most recent token
        if (lastToken != null && lastToken.getLastOtpRequestedAt() != null &&
                lastToken.getLastOtpRequestedAt().plusSeconds(60).isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Too many OTP requests. Try again in 1 minute.");
        }

        // ✅ Generate OTP and store expiry time
        String rawOtp = generateOtp(6);
        System.out.println("Raw OTP: " + rawOtp);
        String hashedOtp = passwordEncoder.encode(rawOtp);


        // Create Token entity with OTP details
        Token token = new Token();
        token.setToken(hashedOtp);  // Store OTP in the token (hashed)
        token.setOtpGeneratedAt(LocalDateTime.now());
        token.setOtpExpiresAt(LocalDateTime.now().plus(OTP_EXPIRATION));
        token.setLastOtpRequestedAt(LocalDateTime.now());

        // Associate the token with the user
        token.setUser(user);
        tokenRepository.save(token);
        // ✅ Send OTP email (send raw OTP, not hashed)
        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.OTP_LOGIN,
                "",  // Empty since no URL is needed
                rawOtp, // Send the plain OTP, not hashed
                "Your OTP for login"
        );
    }

    @Override
    public AuthenticationResponse authenticateWithOtp(String email, String otp) throws MessagingException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Fetch the most recent token
        Token token = tokenRepository.findTopByUserOrderByOtpGeneratedAtDesc(user);

        if (token == null) {
            throw new RuntimeException("No OTP found for user.");
        }

        if (user.getAccountLocked()) {
            throw new LockedException("Your account is locked due to multiple failed OTP attempts. Try again later.");
        }

        // ✅ Check if OTP is expired
        if (LocalDateTime.now().isAfter(token.getOtpExpiresAt())) {
            throw new RuntimeException("OTP has expired. Please request a new one.");
        }

        // Validate OTP using BCrypt match (hashed OTP in token)
        if (!passwordEncoder.matches(otp,  token.getToken())) {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);

            if (user.getFailedLoginAttempts() >= MAX_OTP_ATTEMPTS) {
                user.setAccountLocked(true);
                emailService.sendEmail(
                        user.getEmail(),
                        user.getFullName(),
                        EmailTemplateName.ACCOUNT_LOCKED,
                        "", // No URL needed
                        "Your account has been locked due to multiple failed OTP attempts.",
                        "Account Locked"
                );
            }

            userRepository.save(user);
            throw new BadCredentialsException("Invalid OTP.");
        }

        // Reset OTP attempts on successful login
        user.setFailedLoginAttempts(0);
        user.setAccountLocked(false); // Unlock the account if previously locked
        userRepository.save(user);

        // Generate JWT token
        var claims = new HashMap<String, Object>();
        claims.put("fullName", user.getFullName());
        var jwtToken = jwtService.generatedToken(claims, user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private String generateOtp(int length) {
        String characters = "0123456789";
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            otp.append(characters.charAt(randomIndex));
        }

        return otp.toString();
    }
}

