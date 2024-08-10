package org.solareflare.project.BankSystemMangement.controllers;

import org.solareflare.project.BankSystemMangement.beans.Address;
import org.solareflare.project.BankSystemMangement.exceptions.NotFoundException;
import org.solareflare.project.BankSystemMangement.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/add/address")
    public Address addAddress(@RequestBody Address address) throws Exception {
        return this.addressService.addAddress(address);
    }

    @GetMapping("/get/all")
    public List<Address> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @GetMapping("/get/{id}")
    public Address getAddressById(@PathVariable Long id) throws NotFoundException {
            return this.addressService.getAddressById(id);


    }

    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
    }
}
