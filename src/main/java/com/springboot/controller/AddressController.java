package com.springboot.controller;

import com.springboot.Dtos.AddressDtos;
import com.springboot.entities.Address;
import com.springboot.repository.AddressRepository;
import com.springboot.service.Address.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/address")
@AllArgsConstructor
@Slf4j
@Tag(name = "Address Controller",description = "APIs for managing addresses")
public class AddressController {

    private  final AddressService addressService;

    @PostMapping("/create")
    @Operation(summary = "Create a new address", description = "Creates a new address and returns the created address details.")
    public ResponseEntity<Address> createAddress(@Valid @RequestBody AddressDtos address){
         System.out.println("In address controller " + address.getUserId());
          Address  createAddress = this.addressService.createAddress(address);
         return ResponseEntity.status(HttpStatus.CREATED).body(createAddress);
    }

    @GetMapping("/{addressId}/get-address")
    @Operation(summary = "Get address by ID", description = "Retrieves an address by its unique address ID.")
    public ResponseEntity<Address> getAddress(@PathVariable String addressId){
         Address address = this.addressService.getAddressById(addressId);
         return ResponseEntity.status(HttpStatus.OK).body(address);
    }

    @GetMapping("/{userId}/get-address-by-userId")
    @Operation(summary = "Get address by User ID", description = "Fetches an address associated with a specific user.")
    public ResponseEntity<?> getAddressByUserId(@PathVariable String userId){
           Address getAddress = this.addressService.getAddressByUserId(userId);
           return  ResponseEntity.status(HttpStatus.OK).body(getAddress);
    }


    @GetMapping("/get-all-address")
    @Operation(summary = "Get all addresses", description = "Retrieves all addresses stored in the system.")
    public ResponseEntity<List<Address>> getAllAddress(){
        List<Address> getAllAddress = this.addressService.getAllAddresses();
        return ResponseEntity.status(HttpStatus.OK).body(getAllAddress);
    }

    @DeleteMapping("/{addressId}/delete-address")
    @Operation(summary = "Delete an address", description = "Deletes an address based on the provided address ID.")
    public ResponseEntity<?> deleteAddress(@PathVariable String addressId){
        this.addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }
}
