package learn.book_wyrm.security;

import learn.book_wyrm.data.AppUserRepository;
import learn.book_wyrm.domain.Result;
import learn.book_wyrm.domain.ResultType;
import learn.book_wyrm.domain.Validations;
import learn.book_wyrm.models.AppUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.List;

@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<AppUser> findAll() {
        return appUserRepository.findAll();
    }

    public AppUser findById(int id) {
        return appUserRepository.findById(id);
    }

    public AppUser findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    public List<AppUser> findByAdminStatus(boolean isAdmin) {
        return appUserRepository.findByAdminStatus(isAdmin);
    }

    public Result<AppUser> add(AppUser appUser) {
        Result<AppUser> result = validate(appUser);
        if (!result.isSuccess()) {
            return result;
        }
        if (appUser.getAppUserId() != 0) {
            result.addMessage("userId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(appUser.getPassword());
        appUser.setPassword(hashedPassword);

        appUser = appUserRepository.add(appUser);
        result.setPayload(appUser);
        return result;
    }

    public Result<AppUser> update(AppUser appUser) {
        Result<AppUser> result = validate(appUser);
        if (!result.isSuccess()) {
            return result;
        }
        if (appUser.getAppUserId() <= 0) {
            result.addMessage("userId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (appUser.getPassword() != null && !appUser.getPassword().isEmpty()) {
            // Hash the password before updating
            String hashedPassword = passwordEncoder.encode(appUser.getPassword());
            appUser.setPassword(hashedPassword);
        }

        if (!appUserRepository.update(appUser)) {
            String msg = String.format("userId: %s not found", appUser.getAppUserId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int userId) {
        return appUserRepository.deleteById(userId);
    }

    private Result<AppUser> validate(AppUser appUser) {
        Result<AppUser> result = new Result<>();
        if (appUser == null) {
            result.addMessage("user cannot be null", ResultType.INVALID);
            return result;
        }
        if (Validations.isNullOrBlank(appUser.getUsername())) {
            result.addMessage("username is required", ResultType.INVALID);
        }
        if (Validations.isNullOrBlank(appUser.getEmail())) {
            result.addMessage("email is required", ResultType.INVALID);
        }
        if (Validations.isNullOrBlank(appUser.getPassword())) {
            result.addMessage("password is required", ResultType.INVALID);
        }

        AppUser appUserWithUsername = appUserRepository.findByUsername(appUser.getUsername());
        if (appUserWithUsername != null && appUserWithUsername.getAppUserId() != appUser.getAppUserId()) {
            result.addMessage("username is not unique", ResultType.INVALID);
        }

        List<AppUser> allAppUsers = appUserRepository.findAll();
        boolean emailExists = allAppUsers.stream()
                .filter(u -> u.getAppUserId() != appUser.getAppUserId()) // exclude current user if updating
                .map(AppUser::getEmail)
                .anyMatch(email -> email.equals(appUser.getEmail()));

        if (emailExists) {
            result.addMessage("email is not unique", ResultType.INVALID);
        }

        return result;
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new ValidationException("password must be at least 8 characters");
        }

        int digits = 0;
        int letters = 0;
        int others = 0;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                digits++;
            } else if (Character.isLetter(c)) {
                letters++;
            } else {
                others++;
            }
        }

        if (digits == 0 || letters == 0 || others == 0) {
            throw new ValidationException("password must contain a digit, a letter, and a non-digit/non-letter");
        }
    }

    public AppUser create(String username, String password, String email) {
        validate(username);
        validatePassword(password);

        password = passwordEncoder.encode(password);

        AppUser appUser = new AppUser(0, username, password, email,false, LocalDate.now());

        return appUserRepository.add(appUser);
    }

    private void validate(String username) {
        if (username == null || username.isBlank()) {
            throw new ValidationException("username is required");
        }

        if (username.length() > 50) {
            throw new ValidationException("username must be less than 50 characters");
        }
    }
}