package com.example.testSystem.controller;

import com.example.testSystem.entity.User;
import com.example.testSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // allow frontend to talk backend
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public String registerUser(@RequestBody User user){
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            return "Error : Email already exists";
        }
        userRepository.save(user);
        return "User successfully registered";
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody Map<String, String> loginData){
        String email = loginData.get("email");
        String password = loginData.get("password");
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent() && user.get().getPassword().equals(password)){
            return "Login successful ! Welcome " + user.get().getName();
        }
        return "Invalid Email or Password";
    }

}
