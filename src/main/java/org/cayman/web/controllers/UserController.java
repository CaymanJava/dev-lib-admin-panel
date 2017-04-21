package org.cayman.web.controllers;


import lombok.extern.slf4j.Slf4j;
import org.cayman.model.user.User;
import org.cayman.service.UserService;
import org.cayman.utils.LoggedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("users")
@Controller
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getAll(Model model){
        model.addAttribute("adminName", LoggedUser.getName());
        model.addAttribute("userList", userService.getAll());
        log.info("Get all users.");
        return "users";
    }

    @RequestMapping(value = "status", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String changeStatus(@RequestParam("id") int id) {
        User user = userService.getById(id);
        user.setEnabled(!user.isEnabled());
        userService.update(user);
        log.info("Change status for user with id = " + id);
        return "redirect:/users";
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editUser(@RequestParam("editUserId") int id,
                           @RequestParam("editUserEmail") String email,
                           @RequestParam("editUserPassword") String password) {
        User user = userService.getById(id);
        user.setEmail(email);
        user.setPassword(password);
        userService.update(user);
        log.info("Edit user " + user);
        return "redirect:/users";
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@RequestParam("id") int id) {
        userService.delete(id);
        log.info("Delete user with id " + id);
        return "redirect:/users";
    }
}
