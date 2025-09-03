package learn.book_wyrm.data;

import learn.book_wyrm.models.AppUser;

import java.util.List;

public interface AppUserRepository {
    List<AppUser> findAll();
    AppUser findById(int id);
    AppUser findByUsername(String username);
    List<AppUser> findByAdminStatus(boolean isAdmin);
    AppUser add(AppUser appUser);
    boolean update(AppUser appUser);
    boolean deleteById(int id);
}