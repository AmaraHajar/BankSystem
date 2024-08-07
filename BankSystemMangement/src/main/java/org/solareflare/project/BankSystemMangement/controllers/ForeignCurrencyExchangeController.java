package org.solareflare.project.BankSystemMangement.controllers;

import lombok.RequiredArgsConstructor;
import org.solareflare.project.BankSystemMangement.beans.ForeignCurrencyExchange;
import org.solareflare.project.BankSystemMangement.bl.ForeignCurrencyExchangeBL;
import org.solareflare.project.BankSystemMangement.dto.ForexDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/rates")
public class ForeignCurrencyExchangeController {

    @Autowired
    private ForeignCurrencyExchangeBL foreignCurrencyExchangeBL;

    @GetMapping("/getAll")
    public ResponseEntity<Map<String, Double>> getExchangeRates() throws Exception {
        Map<String, Double> rates = foreignCurrencyExchangeBL.getDailyRates();
        return ResponseEntity.ok(rates);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ForeignCurrencyExchange> getForexById(@PathVariable Long id){
        try{
            return ResponseEntity.ok(foreignCurrencyExchangeBL.getForexById(id));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<ForeignCurrencyExchange> getForexByName(String name){
        return ResponseEntity.ok(foreignCurrencyExchangeBL.getForexByName(name));
    }


    @PostMapping("/add")
    public ForeignCurrencyExchange AddForexRate(@RequestBody ForexDetails forex) throws Exception {
        System.out.println("Name is  "+ forex.getName()+" rate is "+forex.getRate());
        return foreignCurrencyExchangeBL.addForex(forex.getName(), forex.getRate());
    }

    @PutMapping("/update")
    public ForeignCurrencyExchange updateForexRate(@RequestBody ForexDetails forex) throws Exception {
        System.out.println("Name is  "+ forex.getName()+ " rate is "+forex.getRate());
        return foreignCurrencyExchangeBL.updateForex(forex.getName(), forex.getRate());
    }

    // Delete a Forex by ID
    @DeleteMapping("/delete/{id}")
    public void deleteForexById(@PathVariable Long id) {
        try {
            foreignCurrencyExchangeBL.deleteForexById(id);
        } catch (Exception e) {
            // Handle exception, e.g., log it or send a specific response
            throw new RuntimeException("Error deleting Foreign Currency Exchange with ID: " + id, e);
        }
    }


    // Delete a Forex by Name
    @DeleteMapping("/delete")
    public void deleteForexByName(@RequestParam String name) {
        try {
            foreignCurrencyExchangeBL.deleteForexByName(name);
        } catch (Exception e) {
            // Handle exception, e.g., log it or send a specific response
            throw new RuntimeException("Error deleting Foreign Currency Exchange with name: " + name, e);
        }
    }

    @DeleteMapping("/deleteAll")
    public void deleteAllForex() {
        try {
            foreignCurrencyExchangeBL.deleteAllForex();
        } catch (Exception e) {
            throw new RuntimeException("Error deleting all Foreign Currency Exchanges", e);
        }
    }
}
