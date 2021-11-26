package web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.model.Role;
import web.model.User;
import web.service.RoleService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminRoleRestController {
    private RoleService roleService;
    public AdminRoleRestController(RoleService service) {
        this.roleService = service;
    }

    @PersistenceContext
    EntityManager entityManager;

    @GetMapping(value = "/addRole")
    String addRolePage(ModelMap model) {
        Role role = new Role();
        model.addAttribute("role", role);
        return "addRole";
    }

    @PostMapping(value = "/addRole")
    public String addRole(@ModelAttribute("name") String name, ModelMap model)  {
        roleService.add(new Role(name));
        return "redirect:/admin";
    }

    @GetMapping(value = "/deleteRole")
    public String deleteRole(@RequestParam long id, RedirectAttributes attr, ModelMap model) {
        roleService.deleteRole(id);
        DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        attr.addFlashAttribute("result002", "Role deleted at " + df.format((new GregorianCalendar()).getTime()));
        return "redirect:/admin";
    }

    @GetMapping(value = "/editRole")
    public String editRole(@RequestParam long id, RedirectAttributes attr, ModelMap model) {
        Role role = roleService.getRoleById(id);
        model.addAttribute("role", role);
        return "editRole";
    }

    @PostMapping(value = "/editRole")
    String editUser(@ModelAttribute("role") Role role, ModelMap model) {
        roleService.update(role);
        return "redirect:/admin";
    }

    @GetMapping(value = "/getUserRole")
    public ResponseEntity<List<Object>> getUserRole() {
        List<Object> userRole = entityManager.createNativeQuery(
                """
                    SELECT u.name as user, ur.User_id, ur.Role_id, r.name as role
                    FROM user_role as ur
                    LEFT JOIN users as u
                        ON ur.User_id = u.id
                    LEFT JOIN roles as r\s
                        ON ur.Role_id = r.id
                    """).getResultList();
        return userRole != null &&  !userRole.isEmpty()
                ? new ResponseEntity<>(userRole, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);

        //return "adminPage";
    }

}


