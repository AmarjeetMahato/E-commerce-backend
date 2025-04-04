package com.springboot.controller;

import com.springboot.Dtos.UpdateUserDto;
import com.springboot.entities.User;
import com.springboot.service.User.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@Tag(name = "User Controller", description = "Create,Update,Delete,Authentication,OtpAccess")
public class UserController {

     private final UserService userService;



    @GetMapping("/getUser/{userId}")
    @Operation(summary = "Get user details", description = "Fetches user details by user ID.")
    public ResponseEntity<?> getEmployee(@PathVariable String userId){
        User employee =  userService.getUserById(userId);
        return  ResponseEntity.status(HttpStatus.OK).body(employee);
    }

    @PutMapping("/update/{userId}")
    @Operation(summary = "Update user details", description = "Updates the user's details.")
    public ResponseEntity<?> updateEmployee(@Valid
            @PathVariable String userId,
            @RequestBody UpdateUserDto updateUserDto
            ){
         User updateUser = this.userService.updateUser(userId,updateUserDto);
         return  ResponseEntity.status(HttpStatus.OK).body(updateUser);
    }

    @DeleteMapping("/delete/{userId}")
    @Operation(summary = "Delete user", description = "Deletes a user by user ID.")
    public ResponseEntity<?> deleteEmployee(@PathVariable String userId){
        // Call service method to delete employee
        this.userService.deleteUser(userId);
        // Return success response
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // 204 No Content
    }
}
