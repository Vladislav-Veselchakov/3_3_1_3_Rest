package web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web.model.Role;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Controller
public class AdminPageController {
    @PersistenceContext
    EntityManager entityManager;

    private UserService userService;
    private RoleService roleService;

    public AdminPageController(UserService service, RoleService roleService) {
        this.userService = service;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public String AdminPage(Model model) {
        List<User> users = userService.getUsers();
        model.addAttribute("users", users);
        List<Role> roles = roleService.getRoles();
        model.addAttribute("roles", roles);
        model.addAttribute("result001", "result001");

        List<Object> userRole = entityManager.createNativeQuery(
        """
            SELECT u.name as user, ur.User_id, ur.Role_id, r.name as role
            FROM user_role as ur
            LEFT JOIN users as u
                ON ur.User_id = u.id
            LEFT JOIN roles as r\s
                ON ur.Role_id = r.id
            """).getResultList();
        model.addAttribute("userRole", userRole);

        return "adminPage";
    }
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

}
