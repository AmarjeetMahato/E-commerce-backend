package com.springboot.service.User;


import com.springboot.Dtos.UpdateUserDto;
import com.springboot.entities.User;
import com.springboot.exception.InternalServerError;
import com.springboot.exception.ResourceNotFoundException;
import com.springboot.repository.AuthRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResourceAccessException;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

      private  final UserRepository userRepository;


    @Override
      public User getUserById(String  empId){
             try{
                 return  userRepository.findById(empId).orElseThrow(
                         ()-> new ResourceAccessException("User not found")
                 );
             }catch (InternalServerError e) {
                 throw new InternalServerError("Internal server error" + e);
             }
      }


    @Override
    public    User updateUser(String userId, UpdateUserDto userDtos) {

        if (userId == null || userId.isEmpty()) {
            throw new ResourceNotFoundException("Employee ID cannot be null or empty!");
        }
        // Fetch the existing employee from the database
        User existingEmployee = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        try {
            // Update fields only if provided in employeeDtos
            if (StringUtils.hasText(userDtos.getFirstname())) {
                existingEmployee.setFirstname(userDtos.getFirstname());
            }
            if (StringUtils.hasText(userDtos.getLastname())) {
                existingEmployee.setLastname(userDtos.getLastname());
            }
            if (StringUtils.hasText(userDtos.getEmail())) {
                existingEmployee.setEmail(userDtos.getEmail());
            }

            if (userDtos.getAge() != null) {  // Assuming 0 is not a valid age
                existingEmployee.setAge(userDtos.getAge());
            }
            if (userDtos.getNumber() != null) {  // Assuming 0 is not a valid phone number
                existingEmployee.setNumber(userDtos.getNumber());
            }
            // Save the updated employee
            return userRepository.save(existingEmployee);
        } catch (RuntimeException e) {
            throw new InternalServerError("Internal Server Error " + e);
        }
    }


    @Override
    public  void deleteUser(String userId){

          if (userId == null || userId.isEmpty()) {
              throw new ResourceNotFoundException("User ID cannot be null or empty!");
          }

          // Check if the employee exists in the database
          User employee = userRepository.findById(userId)
                  .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

          // Delete the employee
          userRepository.delete(employee);
      }
}
