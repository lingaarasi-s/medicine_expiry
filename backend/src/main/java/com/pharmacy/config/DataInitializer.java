package com.pharmacy.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.pharmacy.model.Role;
import com.pharmacy.model.User;
import com.pharmacy.repository.RoleRepository;
import com.pharmacy.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create default roles if they don't exist
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            Role adminRole = new Role("ROLE_ADMIN", "Administrator with full access");
            roleRepository.save(adminRole);
        }

        if (roleRepository.findByName("ROLE_PHARMACIST").isEmpty()) {
            Role pharmacistRole = new Role("ROLE_PHARMACIST", "Pharmacist with medicine and billing access");
            roleRepository.save(pharmacistRole);
        }

        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            Role userRole = new Role("ROLE_USER", "Basic user with limited access");
            roleRepository.save(userRole);
        }

        // Create default admin user if it doesn't exist
        if (userRepository.findByUsername("admin").isEmpty()) {
            User adminUser = new User("admin", "admin@pharmacy.com", passwordEncoder.encode("admin123"), "System Administrator");
            adminUser.setPhone("1234567890");
            adminUser.setActive(true);

            Set<Role> roles = new HashSet<>();
            roleRepository.findByName("ROLE_ADMIN").ifPresent(roles::add);
            adminUser.setRoles(roles);

            userRepository.save(adminUser);
            System.out.println("Default admin user created:");
            System.out.println("Username: admin");
            System.out.println("Password: admin123");
        }

        // Create default pharmacist user
        if (userRepository.findByUsername("pharmacist").isEmpty()) {
            User pharmacistUser = new User("pharmacist", "pharmacist@pharmacy.com", passwordEncoder.encode("pharm123"), "Default Pharmacist");
            pharmacistUser.setPhone("0987654321");
            pharmacistUser.setActive(true);

            Set<Role> roles = new HashSet<>();
            roleRepository.findByName("ROLE_PHARMACIST").ifPresent(roles::add);
            pharmacistUser.setRoles(roles);

            userRepository.save(pharmacistUser);
            System.out.println("Default pharmacist user created:");
            System.out.println("Username: pharmacist");
            System.out.println("Password: pharm123");
        }
    }
}