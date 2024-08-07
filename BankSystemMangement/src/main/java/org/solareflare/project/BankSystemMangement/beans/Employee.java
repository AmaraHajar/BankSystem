package org.solareflare.project.BankSystemMangement.beans;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import org.solareflare.project.BankSystemMangement.utils.Position;


@Entity
@DiscriminatorValue("EMPLOYEE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Employee extends User {

    @Column(name = "emp_id")
    private String employeeId;

    private Position position;

    private String department;


    @Builder(builderMethodName = "employeeBuilder")
    public Employee(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
    }
}