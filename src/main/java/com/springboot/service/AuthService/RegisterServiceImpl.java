package com.springboot.service.AuthService;

import com.springboot.Dtos.UserDtos;
import com.springboot.SpringSecurityConfig.JwtService;
import com.springboot.entities.Token;
import com.springboot.entities.User;
import com.springboot.enums.EmailTemplateName;
import com.springboot.exception.InternalServerError;
import com.springboot.exception.ResourceAlreadyExistsException;
import com.springboot.exception.ResourceNotFoundException;
import com.springboot.repository.AuthRepository.RoleRepository;
import com.springboot.repository.AuthRepository.TokenRepository;
import com.springboot.repository.AuthRepository.UserRepository;
import com.springboot.response.AuthenticationResponse;
import com.springboot.service.EmailService.EmailService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private  final UserRepository userRepository;
    private  final TokenRepository tokenRepository;
    private  final RoleRepository roleRepository;
    private  final  BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    @Value("${activation-url}")
    private String activationUrl;
    // Example of OTP expiration in minutes (e.g., 10 minutes)
    private static final Duration OTP_EXPIRATION = Duration.ofMinutes(10);


    @Override
    public void createUser(UserDtos userDtos) {

        var userRole =  roleRepository.findByName("USER").orElseThrow(
                () -> new ResourceNotFoundException("Role User was not initialized")
        );

        if(userRepository.existsByEmail(userDtos.getEmail())){
            throw new ResourceAlreadyExistsException("User with the email is already exists " + userDtos.getEmail());
        }
        try {
            var user = User.builder()
                    .firstname(userDtos.getFirstname())
                    .lastname(userDtos.getLastname())
                    .email(userDtos.getEmail())
                    .number(userDtos.getNumber())
                    .age(userDtos.getAge())
                    .accountLocked(false)
                    .enabled(false)
                    .roles(List.of(userRole))
                    .build();
            userRepository.save(user);
            sendVerificationEmail(user);
        } catch (InternalServerError | MessagingException e) {
            throw new InternalServerError("Internal server error" + e);
        }
    }

    private void sendVerificationEmail(User user) throws MessagingException {

        var newToken = generateAndSaveActivationToken(user);
        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
    }

    private String generateAndSaveActivationToken(User user) {
        //        Generated a token
        String generatedToken = generateActivationCode(6);
        // Create Token entity with OTP details
        Token token = new Token();
        token.setToken(generatedToken);  // Store OTP in the token (hashed)
        token.setOtpGeneratedAt(LocalDateTime.now());
        token.setOtpExpiresAt(LocalDateTime.now().plus(OTP_EXPIRATION));
        token.setLastOtpRequestedAt(LocalDateTime.now());
        token.setUser(user);
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String character = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i=0; i<length; i++){
            int randomIndex = secureRandom.nextInt(character.length()); //0..9
            codeBuilder.append(character.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }



    @Transactional
    public AuthenticationResponse activateAccount(String token) throws MessagingException {
        System.out.println("Received token: " + token);

        //  Fetch token from DB (hashed token is stored in the database)
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    System.err.println(" Invalid token provided: " + token);
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token");
                });

        //  Check if token is already used (validatedAt is not null)
        if (savedToken.getValidatedAt() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token already used.");
        }

        //  Check token expiration
        if (LocalDateTime.now().isAfter(savedToken.getOtpExpiresAt())) {
            System.err.println("⚠ Token expired. Resending new activation email...");
            sendVerificationEmail(savedToken.getUser());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Activation token expired. A new token has been sent.");
        }

        // Fetch associated user
        User user = userRepository.findById(savedToken.getUser().getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        //  Activate user account
        if (Boolean.TRUE.equals(user.getEnabled())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is already activated.");
        }
        user.setEnabled(true);
        userRepository.save(user);

        //  Mark token as used
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);

        //  Generate JWT
        var claims = new HashMap<String, Object>();
        claims.put("fullName", user.getFullName());
        String jwtToken = jwtService.generatedToken(claims, user);

        //  Delete token from DB after successful account activation
        tokenRepository.delete(savedToken);

        System.out.println(" Account activated successfully for user: " + user.getEmail());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

}
