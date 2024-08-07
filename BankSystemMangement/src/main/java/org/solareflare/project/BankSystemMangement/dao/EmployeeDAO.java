package org.solareflare.project.BankSystemMangement.dao;

import org.solareflare.project.BankSystemMangement.beans.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeDAO extends JpaRepository<Employee, Long> {

    public Employee findByIdNumber(String idNumber);

}
