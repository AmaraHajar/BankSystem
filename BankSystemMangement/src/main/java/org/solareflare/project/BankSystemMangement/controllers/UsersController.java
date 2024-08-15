package org.solareflare.project.BankSystemMangement.controllers;

import org.solareflare.project.BankSystemMangement.entities.User;
import org.solareflare.project.BankSystemMangement.dto.SignUpRequest;
import org.solareflare.project.BankSystemMangement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserService userService;

    @PostMapping("/addUser")
    public ResponseEntity<User> addUser(@RequestBody SignUpRequest sign) {
        try {
            User user = User.builder()
                    .firstName(sign.getFirstName())
                    .email(sign.getEmail())
                    .lastName(sign.getLastName())
                    .password(sign.getPassword())
                    .build();
            User savedUser = userService.addUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUserDetails(@RequestBody SignUpRequest sign) {
        try {
            User user = User.builder()
                    .firstName(sign.getFirstName())
                    .email(sign.getEmail())
                    .lastName(sign.getLastName())
                    .password(sign.getPassword())
                    .build();
            User updatedUser = userService.updateUser(user);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/idNumber/{idNumber}")
    public ResponseEntity<User> getUserByIdNumber(@PathVariable String idNumber) {
        try {
            User user = userService.findUserByIdNumber(idNumber);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@RequestParam("email") String email) {
        try {
            User user = userService.findUserByEmail(email);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
