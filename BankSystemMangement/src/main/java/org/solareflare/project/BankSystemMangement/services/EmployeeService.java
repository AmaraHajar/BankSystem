package org.solareflare.project.BankSystemMangement.services;

import org.solareflare.project.BankSystemMangement.entities.*;
import org.solareflare.project.BankSystemMangement.repositories.AccountRepository;
import org.solareflare.project.BankSystemMangement.exceptions.*;
import org.solareflare.project.BankSystemMangement.enums.StatusInSystem;
import org.solareflare.project.BankSystemMangement.repositories.EmployeeRepository;
import org.solareflare.project.BankSystemMangement.utils.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    public Employee addEmployee(Employee employee) throws EmployeeNotValidException, IdNumberNotValidException, NameNotValidException, EmailNotValidException {
        Employee emp = employeeRepository.findByIdNumber(employee.getIdNumber());
        try {
            if(emp != null)
                throw new AlreadyExistException(emp);
            return saveEmployee(employee);
        } catch (Exception e) {
            throw new EmployeeNotValidException();//"Failed to add employee: " + e.getMessage()
        }
    }

    public Employee updateEmployee(Employee employee) throws NotFoundException {
        Employee emp;
        try {
            emp = employeeRepository.findByIdNumber(employee.getIdNumber());
        } catch (Exception e) {
            throw new NotFoundException("NOt FOUND ", e);
        }
        return saveEmployee(emp);
    }

    public Employee getEmployeeById(Long id) throws EmployeeNotFoundException {
        Optional<Employee> employee = this.employeeRepository.findById(id);
        if (employee.isPresent()) {
            return employee.get();
        } else {
            throw new EmployeeNotFoundException("Employee with ID " + id + " not found.");
        }
    }

    public Employee getEmployeeByIdNumber(String employeeId) throws EmployeeNotFoundException {
        Employee employee = employeeRepository.findByIdNumber(employeeId);
        if (employee != null) {
            return employee;
        } else {
            throw new EmployeeNotFoundException("Employee with ID number " + employeeId + " not found.");
        }
    }

    public List<Employee> getAllEmployees() throws CustomException {
        try {
            return this.employeeRepository.findAll();
        } catch (Exception e) {
            throw new CustomException(Employee.class, "Failed to retrieve all employees");
        }
    }

    public Employee saveEmployee(Employee employee) throws CustomException {
        try {
            validateEmployee(employee);
            return saveEmployee(employee);
        } catch (Exception e) {
            throw new CustomException(Employee.class, "Failed to save employee");
        }
    }

    public void deleteEmployee(Long id) {
        try {
            if (employeeRepository.existsById(id)) {
                employeeRepository.deleteById(id);
            } else {
                throw new CustomException(Employee.class,"Employee with ID " + id + " not found.");
            }
        } catch (Exception e) {
            throw new CustomException(Employee.class, "Failed to delete employee: " + e.getMessage());
        }
    }

    public void suspendAccount(Long accountId) throws AccountNotFoundException {
        Account account = accountRepository.findAccountById(accountId);
        if (account == null) {
            throw new AccountNotFoundException("Account with ID " + accountId + " not found.");
        }
        account.setStatus(StatusInSystem.IRREGULAR);
        accountRepository.save(account);
    }

    public void restrictAccount(Long accountId) throws AccountNotFoundException {
        Account account = accountRepository.findAccountById(accountId);
        if (account == null) {
            throw new AccountNotFoundException("Account with ID " + accountId + " not found.");
        }
        account.setStatus(StatusInSystem.RESTRICTED);
        accountRepository.save(account);
    }

    public void manageCustomerDeposit(Long employeeId, Long customerId, Long accountId, Double amount) throws Exception {
        try {
            accountService.deposit(accountId, amount);
        } catch (Exception e) {
            throw new CustomException(Employee.class, "Failed to manage customer deposit: " + e.getMessage());
        }
    }

    public void manageCustomerWithdrawal(Long employeeId, Long customerId, Long accountId, Double amount) throws Exception {
        try {
            accountService.withdraw(accountId, amount);
        } catch (Exception e) {
            throw new CustomException(Employee.class, "Failed to manage customer withdrawal: " + e.getMessage());
        }
    }

    public void manageCustomerTransfer(Long employeeId, Long customerId, Long fromAccountId, Long toAccountId, Double amount) throws Exception {
        try {
            accountService.transfer(fromAccountId, toAccountId, amount);
        } catch (Exception e) {
            throw new CustomException(Employee.class, "Failed to manage customer transfer: " + e.getMessage());
        }
    }

    public void transferToExternalBank(Long fromAccountId, String externalBankAccountId, Double amount) throws Exception {
        try {
            accountService.transferToExternalBank(fromAccountId, externalBankAccountId, amount);
        } catch (Exception e) {
            throw new CustomException(Employee.class, "Failed to transfer to external bank: " + e.getMessage());
        }
    }

    private Employee validateEmployee(Employee emp) throws InvalidNameException, InvalidEmailException, InvalidIdException {
       Validation.validateUserDetails(emp.getIdNumber(), emp.getFirstName(), emp.getLastName()
                   ,emp.getEmail());
       return emp;
    }
}
