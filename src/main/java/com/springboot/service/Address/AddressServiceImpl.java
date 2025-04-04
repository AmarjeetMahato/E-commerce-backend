package com.springboot.service.Address;


import com.springboot.Dtos.AddressDtos;
import com.springboot.entities.Address;
import com.springboot.entities.User;
import com.springboot.exception.InternalServerError;
import com.springboot.exception.ResourceNotFoundException;
import com.springboot.repository.AddressRepository;
import com.springboot.repository.AuthRepository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService{
    private  final UserRepository userRepository;
    private  final AddressRepository addressRepository;

    @Transactional
    public Address createAddress(AddressDtos addressDto) {

        Address existingAddress = this.addressRepository.findByHouseNumberAndUser_UserId(
                addressDto.getHouseNumber(),
                addressDto.getUserId()
        );

        if (existingAddress != null) {
            // If address exists, return the existing one
            System.out.println("Existing address found: " + existingAddress);
            return existingAddress;
        }

        User user = this.userRepository.findById(addressDto.getUserId()).orElseThrow(
                ()-> new ResourceNotFoundException("User  not found with id " + addressDto.getUserId())
        );

        try {
            Address address = Address.builder()
                    .houseNumber(addressDto.getHouseNumber())
                    .street(addressDto.getStreet())
                    .landmark(addressDto.getLandmark())
                    .city(addressDto.getCity())
                    .country(addressDto.getCountry())
                    .state(addressDto.getState())
                    .pincode(addressDto.getPincode())
                    .contactNumber(addressDto.getContactNumber())
                    .addressType(addressDto.getAddressType())
                    .user(user)
                    .build();
            return  addressRepository.save(address);
        } catch (RuntimeException e) {
            throw new InternalServerError("Error  Occurred " + e.getMessage());
        }
    }

    @Override
    public Address getAddressById(String addressId) {
           if(addressId == null || addressId.isEmpty()){
               throw new ResourceNotFoundException("AddressId can not be null !!");
           }
        try {
            return addressRepository.findById(addressId).orElseThrow(
                    () -> new ResourceNotFoundException("Address not found")
            );
        } catch (ResourceNotFoundException e) {
            throw new InternalServerError("An Error was occurred " + e.getMessage());
        }
    }

    @Override
    public Address getAddressByUserId(String userId){
        if(userId == null || userId.isEmpty()){
            throw new ResourceNotFoundException("UserId can not be null !!");
        }
        try {
            User user =  userRepository.findById(userId).orElseThrow(
                    () -> new ResourceNotFoundException("User not found")
            );
            Address userAddress = this.addressRepository.findByUser(user);
            System.out.println("userAddress " + userAddress);
            if (userAddress == null) {
                throw new ResourceNotFoundException("No Address found  user with userId: " + userId);
            }
            return  userAddress;
        } catch (ResourceNotFoundException e) {
            throw new InternalServerError("An Error was occurred " + e.getMessage());
        }
    }

    @Override
    public List<Address> getAllAddresses() {

        try {
            List<Address> getAllAddress = this.addressRepository.findAll();
            if(getAllAddress.isEmpty()){
                throw new ResourceNotFoundException("Address list is Empty !!");
            }
            return  getAllAddress;
        } catch (ResourceNotFoundException e) {
            throw new InternalServerError("An error occurred while creating the order: " + e.getMessage());
        }
    }

    @Override
    public Address updateAddress(String addressId, AddressDtos addressDto) {
        return null;
    }

    @Override
    public void deleteAddress(String addressId) {
        if(addressId == null || addressId.isEmpty()){
            throw new ResourceNotFoundException("AddressId can not be null !!");
        }

        Address address = this.addressRepository.findById(addressId).orElseThrow(
                ()-> new ResourceNotFoundException("Address not found")
        );
        this.addressRepository.delete(address);
    }
}
