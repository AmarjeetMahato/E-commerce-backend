package com.springboot.RepositoryTest;


import com.springboot.entities.User;
import com.springboot.repository.AuthRepository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    private User user;
    // Set up the user object once before each test
    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUserId("1");
        user.setFirstname("amar");
        user.setLastname("mahato");
        user.setEmail("amarmahato@gmail.com");
        user.setAge(30);
        user.setNumber("1234567890");
    }

    @Test
    public  void testSaveAndFindUserById(){
//        Save the user
         User savedUser = userRepository.save(user);
//         Retrieved the user by id
        User foundUser = userRepository.findById(savedUser.getUserId()).orElse(null);

        assertNotNull(foundUser);
        assertEquals(savedUser.getUserId(),foundUser.getUserId());
        assertEquals(savedUser.getEmail(), foundUser.getEmail());
        assertEquals(savedUser.getFirstname(), foundUser.getFirstname());

    }

    @Test
    public void  testUpdateUser(){
        this.userRepository.save(user);
        User founduser = this.userRepository.findById(user.getUserId()).orElse(null);
        assertNotNull(founduser);
        founduser.setFirstname("Raja");
        userRepository.save(founduser);
        User updatedUser = userRepository.findById(user.getUserId()).orElse(null);
        assertNotNull(updatedUser);
        assertEquals("Raja", updatedUser.getFirstname());
    }

    @Test
    public  void  testExitsByEmail(){
//        save the User
        userRepository.save(user);
//        Check if the user exists with this email
        boolean exists = userRepository.existsByEmail("amarmahato@gmail.com");
        // Assertions
        assertTrue(exists);
    }

    @Test
    public  void  testUserExistByUserId(){
//        save the user
        userRepository.save(user);
//       Check the user exists by userId
        System.out.println("UserId in test " + user.getUserId());
        boolean userExist = this.userRepository.existsById(user.getUserId());
        assertTrue(userExist);
    }

    @Test
    public  void  testDeleteUser(){
        userRepository.save(user);
        userRepository.delete(user);
        boolean existUser = this.userRepository.existsById(user.getUserId());
        assertFalse(existUser);
    }

    @Test
    public void  testUserNotFound(){
//        Save the User
        User foundUser = userRepository.findById("2").orElse(null);
         assertNull(foundUser);
    }


}
