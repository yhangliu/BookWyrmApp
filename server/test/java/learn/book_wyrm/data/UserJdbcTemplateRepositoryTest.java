package learn.book_wyrm.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserJdbcTemplateRepositoryTest {

    @Autowired
    UserJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldAdd() {
        User user = makeUser();
        User result = repository.add(user);
        assertNotNull(result);
        assertTrue(result.getId() > 0);

        // Attempting to add the same user should throw an exception due to unique constraints
        assertThrows(DataAccessException.class, () -> repository.add(user));
    }

    @Test
    void shouldFindAll() {
        List<User> users = repository.findAll();
        assertNotNull(users);
        assertTrue(!users.isEmpty());
    }

    @Test
    void shouldFindById() {
        User user = repository.findById(1);
        assertNotNull(user);
        assertEquals("alice_jones", user.getUsername());
    }

    @Test
    void shouldFindByUsername() {
        User user = repository.findByUsername("dave_clark");
        assertNotNull(user);
        assertEquals("dave.clark@example.com", user.getEmail());
    }

    @Test
    void shouldFindByAdminStatus() {
        List<User> admins = repository.findByAdminStatus(true);
        assertNotNull(admins);
        assertFalse(admins.isEmpty());
    }

    @Test
    void shouldUpdate() {
        User user = makeUser();
        user.setId(3);
        user.setUsername("updatedUsername");
        user.setEmail("email@email.com");
        assertTrue(repository.update(user));

        User updatedUser = repository.findById(3);
        assertEquals("updatedUsername", updatedUser.getUsername());
    }

    @Test
    void shouldDelete() {
        assertTrue(repository.deleteById(1));
        assertNull(repository.findById(1));
    }

    User makeUser() {
        User user = new User();
        user.setUsername("newUser");
        user.setEmail("newuser@example.com");
        user.setPassword("password");
        user.setAdmin(false);
        user.setCreatedAt(LocalDate.now());
        return user;
    }
}