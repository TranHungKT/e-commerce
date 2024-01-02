package com.ecommerce.admin.user;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.ecommerce.common.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testCreateFirstRole(){
        Role roleAdmin = new Role();
        roleAdmin.setName("Admin");
        roleAdmin.setDescription("manage everything");

        Role savedRole = roleRepository.save(roleAdmin);
        assertNotEquals(savedRole.getId(), 0);
    }

    @Test
    public void testCreateRestRole(){
        Role roleSalesperson = new Role();
        roleSalesperson.setName("Salesperson");
        roleSalesperson.setDescription("manage product price, " +
                "customers, shipping, orders and sales report");

        Role roleEditor = new Role();
        roleEditor.setName("Editor");
        roleEditor.setDescription("manage categories, brands, " +
                "products, articles and menus");

        Role roleShipper = new Role();
        roleShipper.setName("Shipper");
        roleShipper.setDescription("view products, view orders, " +
                "and update order status");

        Role roleAssistant = new Role();
        roleAssistant.setName("Assistant");
        roleAssistant.setDescription("manage questions and reviews");

        roleRepository.saveAll(List.of(roleSalesperson, roleEditor, roleShipper, roleAssistant));

    }
}
