package com.github.rzah94.weatherviewer.service;

import com.github.rzah94.weatherviewer.dto.RegisterUserDto;
import com.github.rzah94.weatherviewer.entity.User;
import com.github.rzah94.weatherviewer.exception.UserAlreadyExistsException;
import com.github.rzah94.weatherviewer.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        return userRepository.findByLogin(username)
                .map(u -> new org.springframework.security.core.userdetails.User(
                        u.getLogin(),
                        u.getPassword(),
                        new ArrayList<>()
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
    }

    public boolean userExists(String username) {
        return userRepository.existsByLogin(username);
    }

    public void save(RegisterUserDto registerUserDto) {
        var user = new User();
        user.setLogin(registerUserDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));

        try {
            userRepository.save(user);
        } catch(DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException("User " + registerUserDto.getUsername() + " already exists");
        }

    }
}
