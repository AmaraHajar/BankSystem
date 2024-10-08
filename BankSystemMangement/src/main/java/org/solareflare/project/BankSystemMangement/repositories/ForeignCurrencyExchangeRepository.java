package org.solareflare.project.BankSystemMangement.repositories;

import org.solareflare.project.BankSystemMangement.entities.ForeignCurrencyExchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ForeignCurrencyExchangeRepository extends JpaRepository<ForeignCurrencyExchange, Long> {

    @Query("SELECT f FROM ForeignCurrencyExchange f WHERE f.bank.id = :bankId")
    public List<ForeignCurrencyExchange> findExchangesByBankId(Long bankId);
    public Optional<ForeignCurrencyExchange> findByName(String name);


}
