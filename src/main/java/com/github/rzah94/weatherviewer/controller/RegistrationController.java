package com.github.rzah94.weatherviewer.controller;

import com.github.rzah94.weatherviewer.dto.RegistrationUserDto;
import com.github.rzah94.weatherviewer.exception.UserAlreadyExistsException;
import com.github.rzah94.weatherviewer.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String register(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            return "redirect:/";
        }

        model.addAttribute("user", new RegistrationUserDto());
        return "register";
    }

    @PostMapping
    public String processRegister(@Valid @ModelAttribute("user") RegistrationUserDto registerUserDto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            this.userService.save(registerUserDto);
        } catch (UserAlreadyExistsException e) {
            bindingResult.rejectValue("username", "error.username", "Пользователь с таким именем уже существует");
            return "register";
        }

        return "redirect:/login";
        // 1Rt@tyutyutyutyutyu
    }
}
