package com.github.rzah94.weatherviewer.service;

import com.github.rzah94.weatherviewer.dto.CustomUserDetailsDto;
import com.github.rzah94.weatherviewer.dto.RegistrationUserDto;
import com.github.rzah94.weatherviewer.entity.User;
import com.github.rzah94.weatherviewer.exception.UserAlreadyExistsException;
import com.github.rzah94.weatherviewer.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
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
    public CustomUserDetailsDto loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByLogin(username)
                .map(user -> new CustomUserDetailsDto(
                        user.getLogin(),
                        user.getPassword(),
                        new ArrayList<>())
                )
                .orElseThrow(() ->
                        new UsernameNotFoundException("User " + username + " not found")
                );
    }

    public RegistrationUserDto save(RegistrationUserDto registerUserDto) throws UserAlreadyExistsException {
        var user = User.builder()
                .login(registerUserDto.getUsername())
                .password(passwordEncoder.encode(registerUserDto.getPassword()))
                .build();
        try {
            var savedUser = userRepository.save(user);

            return RegistrationUserDto.builder()
                    .username(savedUser.getLogin())
                    .password(savedUser.getPassword())
                    .build();

        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException("User " + registerUserDto.getUsername() + " already exists");
        }

    }
}
