package web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import web.model.Role;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminUserRestController {
    private final UserService userService;
    private final RoleService roleService;
    public AdminUserRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "/getUsersTable")
    public ResponseEntity<List<User>> getUsersTable() {
        final List<User> users = userService.getUsers();

        return users != null &&  !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/addUser")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        Set<Role> roles = user.getRoles();
        user.setRoles(null);
        try {
            userService.add(user);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        user.setRoles(roles);
        userService.setCreated(user, new GregorianCalendar().getTime());
        userService.setModified(user, new GregorianCalendar().getTime());
        userService.update(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/edit")
    public ResponseEntity<Object> editPage(@RequestParam Long id) {
        User user = userService.getUserById(id);
        List<Role> roles = roleService.getRolesWithCheck(user);
        Map<String, Object> mUserRoles = new HashMap<>();
        mUserRoles.put("user", user);
        mUserRoles.put("roles", roles);
        return user != null
                ? new ResponseEntity<>(mUserRoles, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


/** рабочий метод (возвращает ResponseEntity<Object>) с ответом в стиле json:
    @PostMapping(value = "/editUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> editUser(@RequestBody User user, ModelMap model) {
        int aaa = 111;
        userService.setModified(user, new GregorianCalendar().getTime());
        userService.update(user);
        // return "redirect:/admin";
        Map<String, String> lvRespon = new HashMap<>();
        lvRespon.put("response", "changed - ok!");
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "key-1", "value-1",
                "key-2", "value-2",
                "key-3", "value-3"));
    }
*/



    @PostMapping(value = "/editUser")
    public ResponseEntity<?> editUser(@RequestBody User user) {
        userService.setModified(user, new GregorianCalendar().getTime());
        userService.update(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/delete")
    public ResponseEntity<?> DeleteUser(@RequestParam Long id) {
        userService.deleteUser(id);
        DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
