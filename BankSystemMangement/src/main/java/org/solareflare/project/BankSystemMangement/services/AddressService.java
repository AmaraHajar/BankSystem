package org.solareflare.project.BankSystemMangement.services;

import org.solareflare.project.BankSystemMangement.beans.Address;
import org.solareflare.project.BankSystemMangement.dao.AddressDAO;
import org.solareflare.project.BankSystemMangement.exceptions.*;
import org.solareflare.project.BankSystemMangement.utils.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressDAO addressDAO;

    public List<Address> getAllAddresses() {
        try {
            return addressDAO.findAll();
        } catch (Exception e) {
            throw new CustomException(Address.class, "Failed to retrieve addresses");
        }
    }

    public Address addAddress(Address address)  {
        try {
            Optional<Address> existingAddress = this.addressDAO.findById(address.getId());
            if(existingAddress.isEmpty())
                return this.saveAddress(address);
        }catch (Exception e) {
            throw new CustomException(Address.class, "Failed to add address");
        }
        return address;
    }
    public Address saveAddress(Address address) {
        try {
            return addressDAO.save(address);
        } catch (Exception e) {
            throw new CustomException(Address.class, "Failed to save address");
        }
    }

    public Address getAddressById(Long id) throws NotFoundException {
        try {
            Optional<Address> address = addressDAO.findById(id);
            if(address.isPresent())
                return address.get();
        }catch (Exception e) {
            throw new CustomException(Address.class, "Failed to find the address with id: "+id);
        }
        return null;
    }

    public void deleteAddress(Long id) {
        try {
            Optional<Address> address = addressDAO.findById(id);
            if(address.isPresent())
                addressDAO.deleteById(id);
        }catch (Exception e) {
            throw new CustomException(Address.class, "Failed to delete address");
        }
    }
}
