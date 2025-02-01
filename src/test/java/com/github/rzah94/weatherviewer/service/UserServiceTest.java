package com.github.rzah94.weatherviewer.service;

import com.github.rzah94.weatherviewer.dto.RegistrationUserDto;
import com.github.rzah94.weatherviewer.entity.User;
import com.github.rzah94.weatherviewer.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    void setup() {
        this.user = new User();
        user.setLogin("testUsername");
        user.setPassword("testPassword");
    }

    @Test
    void loadUserByUsername_UserExists_ReturnsCustomUserDetailsDto() {

        when(userRepository.findByLogin(this.user.getLogin())).thenReturn(Optional.of(user));

        var userDetails = userService.loadUserByUsername(this.user.getLogin());

        assertNotNull(userDetails);
        assertEquals(this.user.getLogin(), userDetails.getUsername());
        assertEquals(this.user.getPassword(), userDetails.getPassword());
        verify(userRepository).findByLogin(this.user.getLogin());
    }

    @Test
    void loadUserByUsername_Fail_ThrowsUsernameNotFoundException() {

        var wrongLogin = "wrongLogin";

        when(userRepository.findByLogin(wrongLogin)).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(wrongLogin));

        verify(userRepository, times(1)).findByLogin(wrongLogin);
    }

    @Test
    void save_UserDoesNotExist_ReturnsSavedUser() {

        var registrationUserDto = RegistrationUserDto.builder()
                .username(this.user.getLogin())
                .password(this.user.getPassword())
                .build();

        when(passwordEncoder.encode(this.user.getPassword())).thenReturn("encodedPassword");

        // Создаем объект User, который будет сохранен
        User userToSave = new User();
        userToSave.setLogin(this.user.getLogin());
        userToSave.setPassword("encodedPassword");

        when(userRepository.save(any(User.class))).thenReturn(userToSave);

        var actRegisterUserDto = userService.save(registrationUserDto);

        assertEquals(this.user.getLogin(), actRegisterUserDto.getUsername());
        assertEquals("encodedPassword", actRegisterUserDto.getPassword());
        verify(passwordEncoder).encode(this.user.getPassword());
        verify(userRepository).save(any(User.class));
    }
}