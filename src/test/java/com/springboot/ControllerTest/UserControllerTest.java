package com.springboot.ControllerTest;

import com.springboot.Dtos.UpdateUserDto;
import com.springboot.Dtos.UserDtos;
import com.springboot.controller.UserController;
import com.springboot.entities.User;
import com.springboot.service.AuthService.RegisterServiceImpl;
import com.springboot.service.User.UserService;
import com.springboot.service.User.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status; // Correct import for status check
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath; // Correct import for json path check


@WebMvcTest(UserController.class)  // Specify the controller class to test
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Mock
    private RegisterServiceImpl userService;

    private UserDtos userDtos;
    private UpdateUserDto updateUserDto;
    private User user;

    @BeforeEach
    void setUp(){
        // Setup mock data for the tests
        userDtos = new UserDtos();
        userDtos.setFirstname("John");
        userDtos.setLastname("Doe");
        userDtos.setEmail("john.doe@example.com");

        updateUserDto = new UpdateUserDto();
        updateUserDto.setFirstname("Jane");
        updateUserDto.setLastname("Doe");

        user = new User();
        user.setUserId("1");
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setEmail("john.doe@example.com");
    }


    @Test
    void createUser() throws  Exception{
        // Mock the service method to return the created user
        when(userService.createUser(any(UserDtos.class))).thenReturn(user);

        mockMvc.perform(post("/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstname\":\"John\",\"lastname\":\"Doe\",\"email\":\"john.doe@example.com\",\"password\":\"password123\"}"))
                .andExpect(status().isCreated())  // Asserting that the status code is 201
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

}
