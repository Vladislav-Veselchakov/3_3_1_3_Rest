package web.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web.model.Role;
import web.model.User;
import web.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserPageController {
    private UserService userService;

    public UserPageController(UserService service) {
        this.userService = service;
    }

    @GetMapping(value = "")
    public String UserPage(Authentication auth, ModelMap model) {
        User user = (User) auth.getPrincipal();
        List<String> messages = new ArrayList<>();
        messages.add(String.format("Hello user \"%s!\"", user.getFirstName()));
        messages.add("Your id: " + user.getId());
        messages.add("First name: " + user.getFirstName());
        messages.add("Last name: " + user.getLastName());
        messages.add("E-mail: " + user.getEmail());
        messages.add("Added/updated: " + user.getModified());
        StringBuilder sb = new StringBuilder();
        for (Role role: user.getRoles()) {
            sb.append(role.getName()).append(", ");
        }
        messages.add("Roles: " + sb.toString());
        model.addAttribute("messages", messages);
        return "userPage";
    }
}