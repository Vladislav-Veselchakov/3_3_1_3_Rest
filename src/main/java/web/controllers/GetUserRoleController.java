package web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.service.RoleService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RestController
@RequestMapping("/admin1")
public class GetUserRoleController {
    private RoleService roleService;
    @PersistenceContext
    EntityManager entityManager;

    public GetUserRoleController(RoleService service) {
        this.roleService = service;
    }

    @GetMapping(value = "/getUserRole1")
    public ResponseEntity<Object> getUserRole(@RequestParam long id, RedirectAttributes attr, ModelMap model) {
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
    }
}
