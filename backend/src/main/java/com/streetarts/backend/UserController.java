package com.streetarts.backend;

import com.streetarts.backend.dto.AuthResponse;
import com.streetarts.backend.dto.UpdateUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable("id") Integer id,
            @RequestBody UpdateUserRequest request
    ) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Користувача не знайдено"));

            validateUpdate(request, id);

            user.setName(request.name.trim());
            user.setSurname(request.surname.trim());
            user.setPhone(request.phone.trim());
            user.setUsername(request.username.trim());
            user.setEmail(request.email.trim().toLowerCase());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            user.setBirthDate(LocalDate.parse(request.birthDate, formatter));

            User savedUser = userRepository.save(user);

            return ResponseEntity.ok(new AuthResponse(savedUser));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    private void validateUpdate(UpdateUserRequest request, Integer userId) {
        if (request.name == null || !request.name.trim().matches("^[A-Za-zА-Яа-яІіЇїЄє'\\- ]{2,}$")) {
            throw new RuntimeException("Ім’я має містити лише літери");
        }

        if (request.surname == null || !request.surname.trim().matches("^[A-Za-zА-Яа-яІіЇїЄє'\\- ]{2,}$")) {
            throw new RuntimeException("Прізвище має містити лише літери");
        }

        if (request.phone == null || !request.phone.trim().matches("^\\+\\d{10,15}$")) {
            throw new RuntimeException("Телефон має починатися з + та містити 10–15 цифр");
        }

        if (request.username == null || request.username.trim().length() < 2) {
            throw new RuntimeException("Нікнейм має містити мінімум 2 символи");
        }

        if (request.email == null || !request.email.trim().matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            throw new RuntimeException("Введіть коректний email");
        }

        if (userRepository.existsByEmailIgnoreCaseAndUserIdNot(request.email.trim(), userId)) {
            throw new RuntimeException("Користувач з такою поштою вже існує");
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate birthDate = LocalDate.parse(request.birthDate, formatter);

            if (birthDate.isAfter(LocalDate.now())) {
                throw new RuntimeException("Дата народження не може бути в майбутньому");
            }

            if (birthDate.isAfter(LocalDate.now().minusYears(10))) {
                throw new RuntimeException("Мінімальний вік — 10 років");
            }

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Дата народження має бути у форматі 31.01.2000");
        }
    }
}