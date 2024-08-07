package org.solareflare.project.BankSystemMangement.dao;



import org.solareflare.project.BankSystemMangement.beans.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDAO extends JpaRepository<User, Long> {

    // Custom finder methods
    public User findByIdNumber(String idNumber);

    public User findByFirstName(String name);

    public User findByLastName(String lastName);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    public User findByEmail(String email);

    public User findByFirstNameAndLastName(String name, String lastName);

}
