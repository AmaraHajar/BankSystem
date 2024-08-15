package org.solareflare.project.BankSystemMangement.repositories;



import org.solareflare.project.BankSystemMangement.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Custom finder methods
    public User findByIdNumber(String idNumber);

    public User findByFirstName(String name);

    public User findByLastName(String lastName);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    public User findByEmail(String email);

    public User findByFirstNameAndLastName(String name, String lastName);

}
