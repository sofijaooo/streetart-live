package com.streetarts.backend;


import com.streetarts.backend.dto.AuthResponse;
import com.streetarts.backend.dto.LoginRequest;
import com.streetarts.backend.dto.RegisterRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository, ArtistRepository artistRepository) {
        this.userRepository = userRepository;
        this.artistRepository = artistRepository;
    }

    public AuthResponse register(RegisterRequest request) {
        validateRegister(request);

        if (userRepository.existsByEmailIgnoreCase(request.email)) {
            throw new RuntimeException("Користувач з такою поштою вже існує");
        }

        UserRole role = "artist".equals(request.role) ? UserRole.artist : UserRole.user;

        User user = new User();
        user.setUsername(request.username.trim());
        user.setEmail(request.email.trim().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(request.password));
        user.setRole(role);

        User savedUser = userRepository.save(user);

        if (role == UserRole.artist) {
            Artists artist = new Artists();
            artist.setUserId(savedUser.getId());
            artist.setNickname(request.artistNickname.trim());
            artist.setGenre(GenreRole.valueOf(request.artistGenre));
            artist.setCity(request.artistCity.trim());
            artist.setAbout(request.artistAbout.trim());
            artist.setAvatar_url("");

            artistRepository.save(artist);
        }

        return new AuthResponse(savedUser);
    }

    public AuthResponse login(LoginRequest request) {
        if (request.email == null || request.email.isBlank()) {
            throw new RuntimeException("Введіть email");
        }

        if (request.password == null || request.password.isBlank()) {
            throw new RuntimeException("Введіть пароль");
        }

        User user = userRepository.findByEmailIgnoreCase(request.email.trim())
                .orElseThrow(() -> new RuntimeException("Користувача з такою поштою не знайдено"));

        if (!passwordEncoder.matches(request.password, user.getPasswordHash())) {
            throw new RuntimeException("Невірний пароль");
        }

        if ("admin".equals(request.role) && user.getRole() != UserRole.admin) {
            throw new RuntimeException("Ця пошта не прив’язана до адміністратора");
        }

        return new AuthResponse(user);
    }

    private void validateRegister(RegisterRequest request) {
        if (request.username == null || request.username.trim().length() < 2) {
            throw new RuntimeException("Ім’я має містити мінімум 2 символи");
        }

        if (request.email == null || !request.email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            throw new RuntimeException("Введіть коректний email");
        }

        if (request.password == null || request.password.length() < 6) {
            throw new RuntimeException("Пароль має містити мінімум 6 символів");
        }

        if ("artist".equals(request.role)) {
            if (request.artistNickname == null || request.artistNickname.isBlank()) {
                throw new RuntimeException("Введіть псевдонім митця");
            }

            if (request.artistGenre == null || request.artistGenre.isBlank()) {
                throw new RuntimeException("Оберіть жанр");
            }

            if (request.artistCity == null || request.artistCity.isBlank()) {
                throw new RuntimeException("Введіть місто");
            }

            if (request.artistAbout == null || request.artistAbout.isBlank()) {
                throw new RuntimeException("Додайте короткий опис творчості");
            }
        }
    }
}