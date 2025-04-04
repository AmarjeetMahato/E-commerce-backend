package com.springboot.RepositoryTest;


import com.springboot.entities.Address;
import com.springboot.entities.User;
import com.springboot.repository.AddressRepository;
import com.springboot.repository.AuthRepository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;


    private Address address;
    private  User user;

    @BeforeEach
    void setUp(){
        user = new User();
        user.setUserId(UUID.randomUUID().toString()); // Ensure User has an ID
        user.setFirstname("John"); // Set necessary User fields
        user.setLastname("Doe");
        user.setEmail("john@example.com");
        user.setAge(24);
        user.setNumber("9978454150");

        user = userRepository.save(user);

        address = new Address();
        address.setAddressId(UUID.randomUUID().toString());
        address.setHouseNumber("101");
        address.setStreet("MG Road");
        address.setLandmark("Near Park");
        address.setCity("Bangalore");
        address.setState("Karnataka");
        address.setPincode("560001");
        address.setCountry("India");
        address.setContactNumber("9876543210");
        address.setAddressType("Home");
        address.setUser(user);
    }

    @Test
    void testSaveAddress() {
        Address savedAddress = addressRepository.save(address);
        assertNotNull(savedAddress.getAddressId());
        assertEquals("101", savedAddress.getHouseNumber());
    }

    @Test
    void testFindById() {
        Address savedAddress = addressRepository.save(address);
        Optional<Address> foundAddress = addressRepository.findById(savedAddress.getAddressId());
        assertTrue(foundAddress.isPresent());
        assertEquals("Bangalore", foundAddress.get().getCity());
    }

    @Test
    void testFindByPincode() {
        addressRepository.save(address);
        Optional<Address> foundAddress = addressRepository.findByPincode("560001");
        assertTrue(foundAddress.isPresent());
        assertEquals("Karnataka", foundAddress.get().getState());
    }



    @Test
    void testFindAllAddresses() {

        // Address 1
        Address address1 = new Address();
        address1.setAddressId(UUID.randomUUID().toString());
        address1.setHouseNumber("101");
        address1.setStreet("MG Road");
        address1.setLandmark("Near Park");
        address1.setCity("Bangalore");
        address1.setState("Karnataka");
        address1.setPincode("560001");
        address1.setCountry("India");
        address1.setContactNumber("9876543210");
        address1.setAddressType("Home");
        address1.setUser(user); // Set user

        // Address 2
        Address address2 = new Address();
        address2.setAddressId(UUID.randomUUID().toString());
        address2.setHouseNumber("202");
        address2.setStreet("Brigade Road");
        address2.setLandmark("Near Mall");
        address2.setCity("Mumbai");
        address2.setState("Maharashtra");
        address2.setPincode("400001");
        address2.setCountry("India");
        address2.setContactNumber("9123456789");
        address2.setAddressType("Work");
        address2.setUser(user); // Set user

        // Save Addresses
        addressRepository.save(address1);
        addressRepository.save(address2);

        // Fetch all addresses
        List<Address> addresses = addressRepository.findAll();
        assertEquals(2, addresses.size());
    }




    @Test
    void testDeleteAddress() {
        Address savedAddress = addressRepository.save(address);
        addressRepository.delete(savedAddress);
        Optional<Address> deletedAddress = addressRepository.findById(savedAddress.getAddressId());
        assertFalse(deletedAddress.isPresent());
    }
}
