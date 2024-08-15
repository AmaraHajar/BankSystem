package org.solareflare.project.BankSystemMangement.repositories;

import org.solareflare.project.BankSystemMangement.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    public Employee findByIdNumber(String idNumber);

}
