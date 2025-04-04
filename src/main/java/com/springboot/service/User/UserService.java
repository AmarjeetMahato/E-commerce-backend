package com.springboot.service.User;

import com.springboot.Dtos.UpdateUserDto;
import com.springboot.Dtos.UserDtos;
import com.springboot.entities.User;
import com.springboot.response.AuthenticationResponse;
import jakarta.mail.MessagingException;
import org.springframework.validation.BindingResult;


public interface UserService {

    User getUserById(String  empId);
    User updateUser(String empId, UpdateUserDto employeeDtos);
    void deleteUser(String empId);

}
