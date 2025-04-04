package com.springboot.repository.AuthRepository;

import com.springboot.entities.Token;
import com.springboot.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token,String> {
    Optional<Token> findByToken(String token);

    Token findTopByUserOrderByOtpGeneratedAtDesc(User user);
}
