package org.solareflare.project.BankSystemMangement.bl;





import org.solareflare.project.BankSystemMangement.beans.User;
import org.solareflare.project.BankSystemMangement.dao.UserDAO;
import org.solareflare.project.BankSystemMangement.exceptions.CustomException;
import org.solareflare.project.BankSystemMangement.utils.Patterns;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class UserBL {

    @Autowired
    private UserDAO userDAO;

    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                try{
                    return userDAO.findByEmail(username);
                }catch (UsernameNotFoundException e){
                    throw new UsernameNotFoundException("User not found");
                }
            }
        };
    }

    public User saveUser(User newUser) {
//        newUser.setPassword(passwordEncoder.encode(newUser.getPassword())); // Encode password
        if (newUser.getId() == null) {
            newUser.setCreatedAt(LocalDateTime.now());
        }
        newUser.setUpdatedAt(LocalDateTime.now());
        return userDAO.save(newUser);
    }

    public User addUser(User user) throws Exception {
        if (user.getId() != null) {
            Optional<User> existingUser = userDAO.findById(user.getId());
            if (existingUser.isPresent()) {
                throw new Exception("User with ID already exists: " + user.getId());
            }
        }
        return saveUser(user);
    }

    public User updateUser(User user) throws CustomException {
        Optional<User> existingUser = userDAO.findById(user.getId());
        if (existingUser.isEmpty()) {
            throw new CustomException(User.class, " not found with ID: " + user.getId());
        }
        return saveUser(user);
    }

    public User findUserByIdNumber(String idNumber) throws CustomException {
        User user = userDAO.findByIdNumber(idNumber);
        if (user == null) {
            throw new CustomException(User.class, " not found with ID Number: " + idNumber);
        }
        return user;
    }

    public List<User> getAllUsers() {
        System.out.println("show all users");
        return userDAO.findAll();
    }

    public User getUserById(Long id) throws CustomException {
        return userDAO.findById(id).orElseThrow(() ->
                new CustomException(User.class, " not found with ID: " + id));
    }

    public User findUserByEmail(String email) throws CustomException {
        User user = userDAO.findByEmail(email);
        if (user == null) {
            throw new CustomException(User.class, " not found with email: " + email);
        }
        return user;
    }

    public void deleteUser(Long id) throws CustomException {
        User user = getUserById(id);
        if (user == null) {
            throw new CustomException(User.class, " not found with ID: " + id);
        }
        userDAO.delete(user);
    }
}




