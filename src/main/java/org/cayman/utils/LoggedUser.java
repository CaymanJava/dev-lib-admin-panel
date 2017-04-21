package org.cayman.utils;

import org.cayman.model.admin.Admin;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.EnumSet;

public class LoggedUser extends org.springframework.security.core.userdetails.User {
    private final Admin admin;

    public LoggedUser(Admin admin){
        super(admin.getLogin(), admin.getPassword(), admin.isEnabled(),
                true, true, true, EnumSet.of(admin.getRole()));
        this.admin = admin;
    }

    public static LoggedUser get() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object user = auth.getPrincipal();
        return (user instanceof LoggedUser) ? (LoggedUser) user : null;
    }

    public static int id() {
        return get().admin.getId();
    }

    public static String getName(){
        return get() != null ? get().getUsername() : "ADMIN";
    }

    @Override
    public String toString() {
        return admin != null ? admin.toString() : "ADMIN";
    }
}
