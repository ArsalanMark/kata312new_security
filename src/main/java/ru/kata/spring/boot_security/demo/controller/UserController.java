package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserValidator userValidator;

    @GetMapping(value="/user")
    public String userPage(Principal principal, Model model) {
        User user = userRepository.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "user";
    }
    @GetMapping(value = "/admin")
    public String adminPage(Model model) {
        List<User> userList = userRepository.findAll();
        model.addAttribute("users", userList);
        return "admin";
    }
    @GetMapping(value="/add")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        return "add-user";
    }
    @PostMapping(value="/add")
    public String saveUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "add-user";
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }
    @PostMapping(value="/save")
    public String saveUser(@ModelAttribute("user") @Valid User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }
    @PostMapping(value="/edit")
    public String editUser(@RequestParam("id") int id, Model model) {
        User user = userRepository.getById((long)id);
        model.addAttribute("user", user);
        return "edit-user";
    }
    @PostMapping(value="/delete")
    public String delete(@RequestParam("id") int id) {
        userRepository.deleteById((long)id);
        return "redirect:/admin";
    }
}
