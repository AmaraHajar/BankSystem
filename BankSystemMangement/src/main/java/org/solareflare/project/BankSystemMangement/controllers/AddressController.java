package org.solareflare.project.BankSystemMangement.controllers;




import org.solareflare.project.BankSystemMangement.beans.Address;
import org.solareflare.project.BankSystemMangement.bl.AddressBL;
import org.solareflare.project.BankSystemMangement.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    @Autowired
    private AddressBL addressBL;

    @PostMapping("/add/address")
    public Address addAddress(@RequestBody Address address) throws Exception {
        return this.addressBL.addAddress(address);
    }

    @GetMapping("/get/all")
    public List<Address> getAllAddresses() {
        return addressBL.getAllAddresses();
    }

    @GetMapping("/get/{id}")
    public Address getAddressById(@PathVariable Long id) throws NotFoundException {
            return this.addressBL.getAddressById(id);


    }

    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable Long id) {
        addressBL.deleteAddress(id);
    }
}
