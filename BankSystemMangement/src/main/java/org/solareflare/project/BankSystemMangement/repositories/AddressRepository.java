package org.solareflare.project.BankSystemMangement.repositories;

import org.solareflare.project.BankSystemMangement.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
