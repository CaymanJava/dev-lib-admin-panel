package org.cayman.web.controllers;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.cayman.model.admin.Admin;
import org.cayman.model.admin.Role;
import org.cayman.service.AdminService;
import org.cayman.utils.LoggedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@RequestMapping(value = "admins")
@Controller
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @SneakyThrows
    public String getAll(Model model){
        model.addAttribute("adminList", adminService.getAll());
        model.addAttribute("adminName", LoggedUser.getName());
        log.info("Get all admins.");
        return "admins";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addAdminOrGuest(Model model,
                                  @RequestParam("admin") boolean admin,
                                  @RequestParam("login") String login,
                                  @RequestParam("password") String password) {
        Role role = admin ? Role.ROLE_ADMIN : Role.ROLE_GUEST;
        adminService.save(Admin
                .builder()
                .login(login)
                .password(password)
                .role(role)
                .enabled(true)
                .registered(LocalDateTime.now())
                .build());
        log.info("Add new " + (admin ? Role.ROLE_ADMIN.name() : Role.ROLE_GUEST.name()));
        return "redirect:/admins";
    }

    @RequestMapping(value = "status", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String changeStatus(@RequestParam("id") int id) {
        Admin admin = adminService.getById(id);
        admin.setEnabled(!admin.isEnabled());
        adminService.save(admin);
        log.info("Change status for admin with id " + id);
        return "redirect:/admins";
    }

    @RequestMapping(value = "role", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String changeRole(@RequestParam("id") int id) {
        Admin admin = adminService.getById(id);
        admin.setRole(admin.getRole() == Role.ROLE_GUEST ? Role.ROLE_ADMIN : Role.ROLE_GUEST);
        adminService.save(admin);
        log.info("Change role for admin with id " + id);
        return "redirect:/admins";
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String edit(@RequestParam("editAdminId") int id,
                       @RequestParam("editAdminLogin") String login,
                       @RequestParam("editAdminPassword") String password){
        Admin admin = adminService.getById(id);
        admin.setLogin(login);
        admin.setPassword(password);
        adminService.save(admin);
        log.info("Edit admin " + admin);
        return "redirect:/admins";
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@RequestParam("id") int id) {
        adminService.delete(id);
        log.info("Delete admin with id " + id);
        return "redirect:/admins";
    }
}
