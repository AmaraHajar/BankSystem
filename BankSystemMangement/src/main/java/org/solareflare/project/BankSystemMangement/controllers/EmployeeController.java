package org.solareflare.project.BankSystemMangement.controllers;


import org.solareflare.project.BankSystemMangement.beans.Customer;
import org.solareflare.project.BankSystemMangement.beans.Employee;
import org.solareflare.project.BankSystemMangement.bl.EmployeesBL;
import org.solareflare.project.BankSystemMangement.dto.UserRequest;
import org.solareflare.project.BankSystemMangement.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeesBL employeeBL;

    @PostMapping("/add/Employee")
    public ResponseEntity<Employee> addEmployee(@RequestBody UserRequest user) throws CustomerNotValidException, AddressNotValidException, IdNumberNotValidException, NameNotValidException, CustomerAlreadyExistException, EmailNotValidException, CustomerNotRegisteredException, EmployeeNotValidException {
        Employee employee = Employee.employeeBuilder()
                .email(user.getEmail())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
        return ResponseEntity.ok(employeeBL.addEmployee(employee));
    }


    @GetMapping("/{employeeIdNumber}")
    public ResponseEntity<Employee> getEmployeeByIdNumber(@PathVariable String employeeId) {
 try{
            return ResponseEntity.ok(employeeBL.getEmployeeByIdNumber(employeeId));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeBL.getEmployeeById(employeeId));
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<String> updateEmployee(@PathVariable Long employeeId, @RequestBody Employee employee) {
        employeeBL.updateEmployee(employee);
        return ResponseEntity.ok("Employee updated successfully");
    }

    @PostMapping("/suspend-account")
    public ResponseEntity<String> suspendAccount(@RequestParam Long accountId) {
        employeeBL.suspendAccount(accountId);
        return ResponseEntity.ok("Account suspended successfully");
    }

    @PostMapping("/restrict-account")
    public ResponseEntity<String> restrictAccount(@RequestParam Long accountId) {
        employeeBL.restrictAccount(accountId);
        return ResponseEntity.ok("Account restricted successfully");
    }

//    @PostMapping("/transfer")
//    public ResponseEntity<String> transferBetweenAccounts(@RequestParam Long fromAccountId, @RequestParam Long toAccountId, @RequestParam Long amount) {
//        try {
//            employeeBL.manageCustomerTransfer(fromAccountId, toAccountId, amount);
//            return ResponseEntity.ok("Transfer successful");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }

    @PostMapping("/transfer/external")
    public ResponseEntity<String> transferToExternal(@RequestParam Long fromAccountId, @RequestParam String externalBankAccountId, @RequestParam Double amount) {
        try {
            employeeBL.transferToExternalBank(fromAccountId, externalBankAccountId, amount);
            return ResponseEntity.ok("External transfer successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
