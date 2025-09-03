package learn.book_wyrm.domain;

import learn.book_wyrm.models.AppUser;
import learn.book_wyrm.security.AppUserService;
import learn.book_wyrm.data.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AppUserServiceTest {

    @Autowired
    private AppUserService userService;

    @MockBean
    private AppUserRepository appUserRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;


    @Test
    void shouldAddUser() {
        AppUser appUser = new AppUser(0, "testuser", "test@example.com", "password123", false,LocalDate.now());
        AppUser mockAppUser = new AppUser(1, "testuser", "test@example.com", "password123", false,LocalDate.now());

        when(appUserRepository.add(any(AppUser.class))).thenReturn(mockAppUser);
        System.out.println(appUser.toString());
        Result<AppUser> result = userService.add(appUser);
        System.out.println(result.getMessages());

        assertEquals(ResultType.SUCCESS, result.getType());
        assertEquals(mockAppUser, result.getPayload());
    }

    @Test
    void shouldNotAddUserWhenInvalid() {
        AppUser appUser = makeUser();
        Result<AppUser> result = userService.add(appUser);
        System.out.println(result.getMessages());
        assertEquals(ResultType.INVALID, result.getType());

        appUser.setUsername("validuser");
        result = userService.add(appUser);
        System.out.println(result.getMessages());

        assertEquals(ResultType.INVALID, result.getType());

        appUser.setEmail("valid@example.com");
        appUser.setPassword("password");
        result = userService.add(appUser);
        System.out.println(result.getMessages());

        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldUpdateUser() {
        AppUser appUser = new AppUser(1, "user1", "user1@example.com", "password1", false,LocalDate.now());

        when(appUserRepository.update(any(AppUser.class))).thenReturn(true);

        Result<AppUser> result = userService.update(appUser);
        System.out.println(result.getMessages());

        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotUpdateUserWhenNotFound() {
        AppUser appUser = new AppUser(99, "existinguser", "password123", "newuser@test.com", false, LocalDate.now());

        when(appUserRepository.update(any(AppUser.class))).thenReturn(false);

        Result<AppUser> result = userService.update(appUser);
        System.out.println(result.getMessages());
        assertEquals(ResultType.NOT_FOUND, result.getType());
    }

    @Test
    void shouldNotUpdateUserWhenInvalid() {
        AppUser appUser = new AppUser(0, "existinguser", "password123", "newuser@test.com", false, LocalDate.now());
        Result<AppUser> result = userService.update(appUser);
        assertEquals(ResultType.INVALID, result.getType());

        appUser.setUsername("validuser");
        result = userService.update(appUser);
        assertEquals(ResultType.INVALID, result.getType());

        appUser.setEmail("valid@example.com");
        appUser.setPassword("password");
        result = userService.update(appUser);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldFindAllUsers() {
        AppUser appUser1 = new AppUser(0, "existinguser", "password123", "newuser@test.com", false, LocalDate.now());
        AppUser appUser2 = new AppUser(1, "existinguser", "password1234", "existinguser@example.com", false,LocalDate.now());

        List<AppUser> appUsers = new ArrayList<>();
        appUsers.add(appUser1);
        appUsers.add(appUser2);

        when(appUserRepository.findAll()).thenReturn(appUsers);

        List<AppUser> result = userService.findAll();
        assertEquals(2, result.size());
        assertTrue(result.contains(appUser1));
        assertTrue(result.contains(appUser2));
    }

    @Test
    void shouldFindUserById() {
        AppUser appUser = new AppUser(1, "user1", "user1@example.com", "password1", false,LocalDate.now());

        when(appUserRepository.findById(1)).thenReturn(appUser);

        AppUser result = userService.findById(1);
        assertEquals(appUser, result);
    }

    @Test
    void shouldFindUserByUsername() {
        AppUser appUser = new AppUser(1, "user1", "user1@example.com", "password1", false,LocalDate.now());

        when(appUserRepository.findByUsername("user1")).thenReturn(appUser);

        AppUser result = userService.findByUsername("user1");
        assertEquals(appUser, result);
    }

    @Test
    void shouldFindUsersByAdminStatus() {
        AppUser appUser1 = new AppUser(1, "user1", "user1@example.com", "password1", false,LocalDate.now());
        AppUser appUser2 = new AppUser(2, "user2", "user2@example.com", "password2", true,LocalDate.now());

        List<AppUser> appUsers = new ArrayList<>();
        appUsers.add(appUser2);

        when(appUserRepository.findByAdminStatus(true)).thenReturn(appUsers);

        List<AppUser> result = userService.findByAdminStatus(true);
        assertEquals(1, result.size());
        assertTrue(result.contains(appUser2));
    }

    @Test
    void shouldNotAddUserWithNonUniqueUsername() {
        AppUser appUser = new AppUser(0, "existinguser", "password123", "newuser@test.com", false, LocalDate.now());
        AppUser existingAppUser = new AppUser(1, "existinguser", "password1234", "existinguser@example.com", false,LocalDate.now());

        when(appUserRepository.findByUsername("existinguser")).thenReturn(existingAppUser);

        Result<AppUser> result = userService.add(appUser);
        assertEquals(ResultType.INVALID, result.getType());
        System.out.println(result.getMessages());
        assertTrue(result.getMessages().contains("username is not unique"));
    }

    @Test
    void shouldNotAddUserWithNonUniqueEmail() {
        AppUser appUser = new AppUser(0, "existinguser", "password123", "newuser@test.com", false, LocalDate.now());
        AppUser existingAppUser = new AppUser(1, "existinguser", "password1234", "newuser@test.com", false,LocalDate.now());

        when(appUserRepository.findAll()).thenReturn(List.of(existingAppUser));

        Result<AppUser> result = userService.add(appUser);
        Result<AppUser> res2 = userService.add(existingAppUser);

        System.out.println(res2.getMessages());
        assertEquals(ResultType.INVALID, res2.getType());
        System.out.println(result.getMessages());

        assertTrue(result.getMessages().contains("email is not unique"));
    }
    private AppUser makeUser(){
        AppUser user = new AppUser();
        user.setAppUserId(0);
        user.setUsername(null);
        user.setEmail(null);
        user.setPassword(null);
        user.setIsAdmin(false);
        user.setCreatedDate(LocalDate.now());
        return user;

    }
}
