package org.cayman.service;


import lombok.extern.slf4j.Slf4j;
import org.cayman.model.admin.Admin;
import org.cayman.repository.admin.AdminRepository;
import org.cayman.utils.LoggedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AdminService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public List<Admin> getAll(){
        return adminRepository.findAll();
    }

    public Admin getById(int id) {
        return adminRepository.findOne(id);
    }

    public Admin save(Admin admin) {
        return adminRepository.save(admin);
    }

    public Admin update(Admin admin) {
        return adminRepository.save(admin);
    }

    public void delete(int id) {
        adminRepository.delete(id);
    }

    public Admin getByLogin(String login) {
        return adminRepository.findByLogin(login.toLowerCase());
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByLogin(login.toLowerCase());
        if (admin == null) {
            throw new UsernameNotFoundException("Admin with login '" + login + "' is not found");
        }
        return new LoggedUser(admin);
    }
}
