package org.solareflare.project.BankSystemMangement.controllers;

import org.solareflare.project.BankSystemMangement.entities.Employee;
import org.solareflare.project.BankSystemMangement.services.EmployeeService;
import org.solareflare.project.BankSystemMangement.dto.UserRequest;
import org.solareflare.project.BankSystemMangement.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;


@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/add/Employee")
    public ResponseEntity<Employee> addEmployee(@RequestBody UserRequest user) throws CustomerNotValidException, AddressNotValidException, IdNumberNotValidException, NameNotValidException, CustomerAlreadyExistException, EmailNotValidException, CustomerNotRegisteredException, EmployeeNotValidException {
        Employee employee = Employee.employeeBuilder()
                .email(user.getEmail())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
        return ResponseEntity.ok(employeeService.addEmployee(employee));
    }


    @GetMapping("/{employeeIdNumber}")
    public ResponseEntity<Employee> getEmployeeByIdNumber(@PathVariable String employeeId) {
 try{
            return ResponseEntity.ok(employeeService.getEmployeeByIdNumber(employeeId));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long employeeId) throws EmployeeNotFoundException {
        return ResponseEntity.ok(employeeService.getEmployeeById(employeeId));
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<String> updateEmployee(@PathVariable Long employeeId, @RequestBody Employee employee) throws EmployeeNotValidException, NotFoundException {
        employeeService.updateEmployee(employee);
        return ResponseEntity.ok("Employee updated successfully");
    }

    @PostMapping("/suspend-account")
    public ResponseEntity<String> suspendAccount(@RequestParam Long accountId) throws AccountNotFoundException {
        employeeService.suspendAccount(accountId);
        return ResponseEntity.ok("Account suspended successfully");
    }

    @PostMapping("/restrict-account")
    public ResponseEntity<String> restrictAccount(@RequestParam Long accountId) throws AccountNotFoundException {
        employeeService.restrictAccount(accountId);
        return ResponseEntity.ok("Account restricted successfully");
    }

    @PostMapping("/transfer/external")
    public ResponseEntity<String> transferToExternal(@RequestParam Long fromAccountId, @RequestParam String externalBankAccountId, @RequestParam Double amount) {
        try {
            employeeService.transferToExternalBank(fromAccountId, externalBankAccountId, amount);
            return ResponseEntity.ok("External transfer successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
