package org.cayman.repository.admin;


import org.cayman.model.admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer>{
    Admin findByLogin(String login);
}
