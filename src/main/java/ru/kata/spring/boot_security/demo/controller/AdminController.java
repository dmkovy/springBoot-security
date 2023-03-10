package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public String printAllUsers(Model model, Principal principal) {
        model.addAttribute("userByEmail", userService.findByEmail(principal.getName()));
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping(value = "/edit/{id}")
    public String showEditUser(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        List<Role> roles = roleService.findAllRoles();
        model.addAttribute("allRoles", roles);
        return "edit";
    }

    @PatchMapping(value = "/edit")
    public String editUser(@ModelAttribute("user") User user) {
        userService.updateUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping(value = "/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        List<Role> roles = roleService.findAllRoles();
        model.addAttribute("allRoles", roles);
        return "new";
    }

    @PostMapping(value = "/save")
    public String saveUser(@ModelAttribute("user") User user, Model model) {
        userService.saveUser(user);
        model.addAttribute("roles", user.getRoles());
        return "redirect:/admin/users";
    }

    @DeleteMapping(value = "/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.removeUserById(id);
        return "redirect:/admin/users";
    }
}