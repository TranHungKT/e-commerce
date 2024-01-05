package com.ecommerce.admin.user;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import com.ecommerce.common.entity.Role;
import com.ecommerce.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
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
}
