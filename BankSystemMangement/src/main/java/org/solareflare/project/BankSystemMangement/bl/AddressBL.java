package org.solareflare.project.BankSystemMangement.bl;

import org.solareflare.project.BankSystemMangement.beans.Address;
import org.solareflare.project.BankSystemMangement.dao.AddressDAO;
import org.solareflare.project.BankSystemMangement.exceptions.*;
import org.solareflare.project.BankSystemMangement.utils.Patterns;
import org.solareflare.project.BankSystemMangement.utils.Validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressBL {
    @Autowired
    private AddressDAO addressDAO;

    public List<Address> getAllAddresses() {
        return addressDAO.findAll();
    }

    public Address addAddress(Address address) throws AlreadyExistException, AddressNotValidException {
        if(address.getId() != null) {
            Optional<Address> existingAddress = this.addressDAO.findById(address.getId());
            if (existingAddress.isPresent()) {
                throw new AlreadyExistException(address);
            }
        }
        if(!Validation.isValidAddress(address))
            throw new AddressNotValidException();
        return this.saveAddress(address);
    }

    public Address saveAddress(Address address) {
        return addressDAO.save(address);
    }

    public Address getAddressById(Long id) throws CustomException, NotFoundException {
        Optional<Address> address = this.addressDAO.findById(id);
        if (address.isPresent()) {
            return address.get();
        }
        throw new NotFoundException();

    }

    public void deleteAddress(Long id) {
        addressDAO.deleteById(id);
    }
}
