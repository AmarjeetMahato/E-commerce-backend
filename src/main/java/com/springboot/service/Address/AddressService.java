package com.springboot.service.Address;

import com.springboot.Dtos.AddressDtos;
import com.springboot.entities.Address;

import java.util.List;

public interface AddressService {

        Address createAddress(AddressDtos addressDto);
        Address getAddressById(String addressId);
        List<Address> getAllAddresses();
        Address getAddressByUserId(String userId);
        Address updateAddress(String addressId, AddressDtos addressDto);
        void deleteAddress(String addressId);

}
