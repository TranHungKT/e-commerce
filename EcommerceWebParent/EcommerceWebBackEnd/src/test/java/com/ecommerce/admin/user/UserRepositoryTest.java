package com.ecommerce.admin.user;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.ecommerce.common.entity.Role;
import com.ecommerce.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateUserWithOneRole(){
        User userTranHung = new User("hungtran@gmail.com", "hung2024", "Hung", "Tran");
        Role roleAdmin = entityManager.find(Role.class, 1);

        userTranHung.addRole(roleAdmin);
        User savedUser =  userRepository.save(userTranHung);
        assertNotEquals(savedUser.getId(), 0);

    }

    @Test
    public void testCreateNewUserWithTwoRole(){
        User userNgocAnh = new User("ngocanh@gmail.com", "hung2024", "Ngoc", "Anh");
        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);

        userNgocAnh.addRole(roleEditor);
        userNgocAnh.addRole(roleAssistant);

        User savedUser =  userRepository.save(userNgocAnh);
        assertNotEquals(savedUser.getId(), 0);
    }

    @Test
    public void testGetUserByEmail(){
        String email = "abc@def.com";
        User user = userRepository.findUserByEmail(email);
        assertNull(user);

        String email2 = "hungtran@gmail.com";
        User user2 = userRepository.findUserByEmail(email2);
        assertNotNull(user2);
    }

    @Test
    public void testListFirstPage(){
        int pageNumber = 0;
        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = userRepository.findAll(pageable);

        List<User> listUsers = page.getContent();
        listUsers.forEach(System.out::println);
    }
}
