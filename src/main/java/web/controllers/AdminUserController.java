package web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

@Controller
@RequestMapping("/admin")
public class AdminUserController {
    private final UserService userService;
    private final RoleService roleService;
    public AdminUserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

//    @GetMapping(value = "/addUser")
//    public String addPage(ModelMap model) {
//        User usr = new User();
//        model.addAttribute("user", usr);
//        return "addUser";
//    }

    @PostMapping(value = "/addUser",  produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public Map<String, Object> addUser(@RequestBody User user, ModelMap model) {
        Set<Role> roles = user.getRoles();
        user.setRoles(null);
        try {
            userService.add(user);
        } catch (Exception ex) {
            ex.printStackTrace();
            Map<String, Object> lvRespon = new HashMap<>();
            lvRespon.put("VL_Users", ex.getStackTrace());
            return lvRespon;
        }
        user.setRoles(roles);
        userService.setCreated(user, new GregorianCalendar().getTime());
        userService.setModified(user, new GregorianCalendar().getTime());
        userService.update(user);
        Map<String, Object> lvRespon = new HashMap<>();
        lvRespon.put("VL_response", "VL: changed - ok!");
        lvRespon.put("VL_Users", userService.getUsers());
        return lvRespon;
    }

    @GetMapping(value = "/edit",  produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public Map<String, Object> editPage(@RequestParam Long id, ModelMap model) {
        User user = userService.getUserById(id);
        List<Role> roles = roleService.getRolesWithCheck(user);
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);

        Map<String, Object> lvRespon = new HashMap<>();
        lvRespon.put("user", user);
        lvRespon.put("roles", roles);
        return lvRespon;

    }

/** VL: original from 3-3-1-1:
    @PostMapping(value = "/editUser")
    String editUser(@RequestBody User user, ModelMap model) {
        int aaa = 111;
        userService.setModified(user, new GregorianCalendar().getTime());
        userService.update(user);
        return "redirect:/admin";
    }
*/

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

/** рабочий метод (возвращает ResponseEntity просто) с ответом List<user>
@PostMapping(value = "/editUser", produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity editUser(@RequestBody User user, ModelMap model) {
    int aaa = 111;
    userService.setModified(user, new GregorianCalendar().getTime());
    userService.update(user);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of(
            "VL_key-1", "VL: value-1",
            "VL_userList", userService.getUsers()));
}
*/


/** рабочий меторд (возвращает Map<String, String>) с пом. аннотаций @ResponseBody, @ResponseStatus(code = HttpStatus.OK), @PostMapping(value = "/editUser", produces = MediaType.APPLICATION_JSON_VALUE)
 @PostMapping(value = "/editUser", produces = MediaType.APPLICATION_JSON_VALUE)
 @ResponseBody
 @ResponseStatus(code = HttpStatus.OK)
 public Map<String, String> editUser(@RequestBody User user, ModelMap model) {
 int aaa = 111;
 userService.setModified(user, new GregorianCalendar().getTime());
 userService.update(user);
 // return "redirect:/admin";
 Map<String, String> lvRespon = new HashMap<>();
 lvRespon.put("VL_response", "VL: changed - ok!");
 return lvRespon;
 }

*/

//  рабочий метод (возвращает Map<String, Object>)
    @PostMapping(value = "/editUser", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public Map<String, Object> editUser(@RequestBody User user, ModelMap model) {
        userService.setModified(user, new GregorianCalendar().getTime());
        userService.update(user);
        Map<String, Object> lvRespon = new HashMap<>();
        lvRespon.put("VL_response", "VL: changed - ok!");
        lvRespon.put("VL_Users", userService.getUsers());
        return lvRespon; // не используется. Для тестовых целей
    }

    @GetMapping(value = "/delete")
    public String DeleteUser(@RequestParam Long id, RedirectAttributes attr, ModelMap model) {
        userService.deleteUser(id);
        DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        attr.addFlashAttribute("result001", "User deleted at " + df.format((new GregorianCalendar()).getTime()));
        return "redirect:/admin";
    }
}
