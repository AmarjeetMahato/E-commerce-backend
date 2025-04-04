package com.springboot.repository;

import com.springboot.entities.Address;
import com.springboot.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address,String> {

    Address findByHouseNumberAndUser_UserId(String houseNumber, String userId);

    Address findByUser(User user);

    Optional<Address> findByPincode(String number);
}
