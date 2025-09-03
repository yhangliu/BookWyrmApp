package learn.book_wyrm.controllers;

import learn.book_wyrm.models.AppUser;
import learn.book_wyrm.security.AppUserService;
import learn.book_wyrm.security.JwtConverter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtConverter converter;
    private final AppUserService appUserService;

    public AuthController(AuthenticationManager authenticationManager, JwtConverter converter, AppUserService appUserService) {
        this.authenticationManager = authenticationManager;
        this.converter = converter;
        this.appUserService = appUserService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, String>> authenticate(@RequestBody Map<String, String> credentials) {

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(credentials.get("username"), credentials.get("password"));
        System.out.println(authToken);

            Authentication authentication = authenticationManager.authenticate(authToken);

            if (authentication.isAuthenticated()) {
                String jwtToken = converter.getTokenFromUser((User) authentication.getPrincipal());

                HashMap<String, String> map = new HashMap<>();
                map.put("jwt_token", jwtToken);

                return new ResponseEntity<>(map, HttpStatus.OK);
            }



        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/register")
    public ResponseEntity<?> createAccount(@RequestBody Map<String, String> credentials) {
        AppUser appUser = null;

        try {
            String username = credentials.get("username");
            String password = credentials.get("password");
            String email = credentials.get("email");

            appUser = appUserService.create(username, password, email);
        } catch (ValidationException ex) {
            return new ResponseEntity<>(List.of(ex.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (DuplicateKeyException ex) {
            return new ResponseEntity<>(List.of("The provided username or email already exists"), HttpStatus.BAD_REQUEST);
        }

        HashMap<String, Integer> map = new HashMap<>();
        map.put("appUserId", appUser.getAppUserId());

        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody Map<String, String> updates) {
        try {
            if (!adminAuthenticated(updates)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }


            String username = updates.get("username");
            String password = updates.get("password");
            String email = updates.get("email");
            boolean isAdmin = Boolean.parseBoolean(updates.get("isAdmin"));
            int userId = Integer.parseInt(updates.get("userId"));

            AppUser user = appUserService.findById(userId);

            AppUser updatedUser = appUserService.update(new AppUser(userId, username, password, email, isAdmin, user.getCreatedDate())).getPayload();

            if (updatedUser != null) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("appUserId", updatedUser.getAppUserId());
                return new ResponseEntity<>(map, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ValidationException ex) {
            return new ResponseEntity<>(List.of(ex.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (DuplicateKeyException ex) {
            return new ResponseEntity<>(List.of("The provided username or email already exists"), HttpStatus.BAD_REQUEST);
        } catch (NumberFormatException ex) {
            return new ResponseEntity<>(List.of("Invalid userId"), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody Map<String, String> credentials) {
        try {
            if (!adminAuthenticated(credentials)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            int userIdToDelete = Integer.parseInt(credentials.get("userId"));

            // Delete the user by ID
            boolean isDeleted = appUserService.deleteById(userIdToDelete);
            if (isDeleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (AuthenticationException | NumberFormatException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private boolean adminAuthenticated(Map<String, String> body) {
        if (!body.containsKey("adminUsername") || !body.containsKey("adminPassword")) return false;
        String adminUsername = body.get("adminUsername");
        String adminPassword = body.get("adminPassword");
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(adminUsername, adminPassword);
        Authentication authentication = authenticationManager.authenticate(authToken);
        if (!authentication.isAuthenticated()) {
            return false;
        }
        User authenticatedUser = (User) authentication.getPrincipal();
        AppUser adminUser = appUserService.findByUsername(authenticatedUser.getUsername());
        return adminUser != null && adminUser.getIsAdmin();
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(@RequestParam Map<String, String> body) {
        boolean isAdmin = adminAuthenticated(body);

        List<AppUser> users = appUserService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id, @RequestParam Map<String, String> body) {
        AppUser user = appUserService.findById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        boolean isAdmin = adminAuthenticated(body);


        return new ResponseEntity<>( HttpStatus.OK);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username, @RequestParam Map<String, String> body) {
        AppUser user = appUserService.findByUsername(username);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        boolean isAdmin = adminAuthenticated(body);

        return new ResponseEntity<>( HttpStatus.OK);
    }


}