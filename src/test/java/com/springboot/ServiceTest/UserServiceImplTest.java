package com.springboot.ServiceTest;


import com.springboot.Dtos.UpdateUserDto;
import com.springboot.entities.User;
import com.springboot.repository.AuthRepository.UserRepository;
import com.springboot.service.User.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    private  User user;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initializes mocks
        user = new User();
        user.setUserId("1");
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setEmail("john.doe@example.com");
        user.setAge(25);
        user.setNumber("1234567890");
    }
    @Test
    void  getUserByIdTest(){
        when(userRepository.findById(ArgumentMatchers.anyString()))
                .thenReturn(Optional.ofNullable(user));
          User result =   userService.getUserById("1");
        assertNotNull(result);
        assertEquals("1", result.getUserId());
        assertEquals("John", result.getFirstname());
        verify(userRepository, times(1)).findById("1");
    }

    @Test
    void getUserByEmailTest(){
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userRepository.findByEmail("john.doe@example.com");
        assertTrue(foundUser.isPresent());
        assertEquals("John", foundUser.get().getFirstname());
        assertEquals("john.doe@example.com",foundUser.get().getEmail());
        verify(userRepository, times(1)).findByEmail("john.doe@example.com");
    }


    @Test
    void updateUserTest() {
        // Arrange: Create an existing user
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        // Create an UpdateUserDto instead of User
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setFirstname("Jane");
        updateUserDto.setLastname("Doe");
        updateUserDto.setEmail("jane.doe@example.com");
        updateUserDto.setPassword("654321");
        updateUserDto.setAge(26);
        updateUserDto.setNumber("0987654321");

        User updatedUser = new User();
        updatedUser.setUserId("1");
        updatedUser.setFirstname(updateUserDto.getFirstname());
        updatedUser.setLastname(updateUserDto.getLastname());
        updatedUser.setEmail(updateUserDto.getEmail());
        updatedUser.setAge(updateUserDto.getAge());
        updatedUser.setNumber(updateUserDto.getNumber());

        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // Act: Pass UpdateUserDto instead of User
        User result = userService.updateUser("1", updateUserDto);

        // Assert: Verify the changes
        assertNotNull(result);
        assertEquals("Jane", result.getFirstname()); // ✅ Firstname updated
        assertEquals("Doe", result.getLastname());   // ✅ Lastname unchanged
        assertEquals("jane.doe@example.com", result.getEmail()); // ✅ Email updated

        // Verify interactions
        verify(userRepository, times(1)).findById("1");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void  deleteUserTest(){
        // Mock findById so the user exists
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        // Mock deleteById (optional, as it does not return anything)
        doNothing().when(userRepository).delete(user);

        // Act
        userService.deleteUser("1");

        // Assert: Verify interactions
        verify(userRepository, times(1)).findById("1");  // ✅ Check lookup was done
        verify(userRepository, times(1)).delete(user); // ✅ Check delete was called
    }

}
