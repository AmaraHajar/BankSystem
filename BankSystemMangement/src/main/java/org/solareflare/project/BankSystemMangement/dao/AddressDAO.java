package org.solareflare.project.BankSystemMangement.dao;

import org.solareflare.project.BankSystemMangement.beans.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressDAO extends JpaRepository<Address, Long> {
}
