package com.example.cloudBalance.service;

import com.example.cloudBalance.dto.*;
import com.example.cloudBalance.entity.User;
import com.example.cloudBalance.exception.ResourceAlreadyExistsException;
import com.example.cloudBalance.exception.ResourceNotFoundException;
import com.example.cloudBalance.repository.mysql.UserRepository;
import com.example.cloudBalance.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public JwtResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        String token = jwtUtil.generateToken(userDetails);
        String userName = user.getFirstName() + " " + user.getLastName();
        String email = userDetails.getUsername();

        String role = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(null);

        return new JwtResponse("Bearer", token, userName, email, role);
    }

    public User register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("User already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        return userRepository.save(user);
    }

    public User updateUser(UpdateUserRequest request) {
        String email = request.getEmail();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        if(!Objects.equals(user.getFirstName(), request.getFirstName())) {
            user.setFirstName(request.getFirstName());
        }

        if(!Objects.equals(user.getLastName(), request.getLastName())) {
            user.setLastName(request.getLastName());
        }

        if(!Objects.equals(user.getRole(), request.getRole())) {
            user.setRole(request.getRole());
        }

        return userRepository.save(user);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'READONLY')")
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return mapToUserResponseDto(users);
    }

    private List<UserResponse> mapToUserResponseDto(List<User> users) {
        List<UserResponse> result = new ArrayList<>();

        for(User user: users) {
            UserResponse userResponse = new UserResponse();
            userResponse.setId(user.getId());
            userResponse.setFirstName(user.getFirstName());
            userResponse.setLastName(user.getLastName());
            userResponse.setEmail(user.getEmail());
            userResponse.setRole(user.getRole());

            result.add(userResponse);
        }
        return result;
    }
}
