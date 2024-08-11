package org.solareflare.project.BankSystemMangement.services;

import org.solareflare.project.BankSystemMangement.beans.User;
import org.solareflare.project.BankSystemMangement.dao.UserDAO;
import org.solareflare.project.BankSystemMangement.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    // Refactor UserDetailsService to handle exceptions properly
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                UserDetails user = userDAO.findByEmail(username);
                if (user == null) {
                    throw new UsernameNotFoundException("User with email " + username + " not found");
                }
                return user;
            }
        };
    }

    // Method to save or update user
    public User saveUser(User newUser) {
        if (newUser.getId() == null) {
            newUser.setCreatedAt(LocalDateTime.now());
        }
        newUser.setUpdatedAt(LocalDateTime.now());
        return userDAO.save(newUser);
    }

    // Add user with proper exception handling
    public User addUser(User user) throws CustomException {
        if (user.getId() != null && userDAO.existsById(user.getId())) {
            throw new CustomException(User.class, "User with ID already exists: " + user.getId());
        }
        return saveUser(user);
    }

    // Update user with proper exception handling
    public User updateUser(User user) throws CustomException {
        if (!userDAO.existsById(user.getId())) {
            throw new CustomException(User.class, "User not found with ID: " + user.getId());
        }
        return saveUser(user);
    }

    // Find user by ID number with proper exception handling
    public User findUserByIdNumber(String idNumber) throws CustomException {
        User user = userDAO.findByIdNumber(idNumber);
        if (user == null) {
            throw new CustomException(User.class, "User not found with ID Number: " + idNumber);
        }
        return user;
    }

    // Get all users
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    // Get user by ID with proper exception handling
    public User getUserById(Long id) throws CustomException {
        return userDAO.findById(id)
                .orElseThrow(() -> new CustomException(User.class, "User not found with ID: " + id));
    }

    // Find user by email with proper exception handling
    public User findUserByEmail(String email) throws CustomException {
        User user = userDAO.findByEmail(email);
        if (user == null) {
            throw new CustomException(User.class, "User not found with email: " + email);
        }
        return user;
    }

    // Delete user with proper exception handling
    public void deleteUser(Long id) throws CustomException {
        User user = getUserById(id);
        if (user == null) {
            throw new CustomException(User.class, "User not found with ID: " + id);
        }
        userDAO.delete(user);
    }
}