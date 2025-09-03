package learn.book_wyrm.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AppUser extends User {

    private static final String AUTHORITY_PREFIX = "ROLE_";

    private int appUserId;
    private String email;
    private boolean isAdmin;
    private LocalDate createdDate;
    private String password;
    private String username;
    public AppUser(){
        super("username", "password", true,
                true, true, true,
                convertRoleToAuthorities(false));
    }
    public AppUser(int appUserId, String username, String password, String email,
                   boolean isAdmin, LocalDate date) {
        super(username, password, true,
                true, true, true,
                convertRoleToAuthorities(isAdmin));
        this.appUserId = appUserId;
        this.email = email;
        this.username=username;
        this.password=password;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(LocalDate date) {
        this.createdDate = date;
    }

    public boolean getIsAdmin() {
        return this.isAdmin;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String s) {
        this.email = s;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String pass) {
        this.password = pass;
    }

    private static List<GrantedAuthority> convertRoleToAuthorities(boolean isAdmin) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        String role = isAdmin ? "ADMIN" : "USER";
        authorities.add(new SimpleGrantedAuthority(AUTHORITY_PREFIX + role));
        return authorities;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "appUserId=" + appUserId +
                ", email='" + email + '\'' +
                ", isAdmin=" + isAdmin +
                ", createdDate=" + createdDate +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
