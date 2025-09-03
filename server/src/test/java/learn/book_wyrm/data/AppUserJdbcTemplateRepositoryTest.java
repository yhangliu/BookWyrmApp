package learn.book_wyrm.data;

import learn.book_wyrm.models.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppUserJdbcTemplateRepositoryTest {

    @Autowired
    AppUserJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldAdd() {
        AppUser appUser = new AppUser(0, "testuser", "password1234", "test@example.com", false,LocalDate.now());
        AppUser result = repository.add(appUser);
        assertNotNull(result);
        assertTrue(result.getAppUserId() > 0);

        // Attempting to add the same user should throw an exception due to unique constraints
        assertThrows(DataAccessException.class, () -> repository.add(appUser));
    }

    @Test
    void shouldFindAll() {
        List<AppUser> appUsers = repository.findAll();
        assertNotNull(appUsers);
        appUsers.forEach(u-> System.out.println(u.getUsername()+" "+u.getAppUserId()));
        assertTrue(!appUsers.isEmpty());
    }

    @Test
    void shouldFindById() {
        AppUser appUser = repository.findById(1);
        assertNotNull(appUser);
        assertEquals("alice_jones", appUser.getUsername());
    }

    @Test
    void shouldFindByUsername() {
        AppUser appUser = repository.findByUsername("dave_clark");
        assertNotNull(appUser);
        assertEquals("dave.clark@example.com", appUser.getEmail());
    }

    @Test
    void shouldFindByAdminStatus() {
        List<AppUser> admins = repository.findByAdminStatus(true);
        assertNotNull(admins);
        assertFalse(admins.isEmpty());
    }

    @Test
    void shouldUpdate() {

        AppUser appUser = repository.findById(1);
        System.out.println(appUser.getUsername());
        appUser.setUsername("TESTNAME");
        appUser.setEmail("TESTEMAIL@@MAIL.com");
        assertTrue(repository.update(appUser));
        System.out.println(repository.findById(1).getUsername());
        assertEquals("TESTNAME", repository.findById(1).getUsername());
    }

    @Test
    void shouldDelete() {
        assertTrue(repository.deleteById(3));
        assertNull(repository.findById(3));
    }


}