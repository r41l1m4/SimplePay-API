package dev.ironia.simplepay.api.repositories;

import dev.ironia.simplepay.api.domain.user.User;
import dev.ironia.simplepay.api.domain.user.UserType;
import dev.ironia.simplepay.api.dtos.UserDto;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should get user successfully from DB")
    void findUserByDocumentSucceeded() {
        String document = "267354";
        UserDto userDto = new UserDto(
                "John",
                "Doe",
                document,
                new BigDecimal("20.0"),
                "john@doe.com",
                "johndoe",
                UserType.COMMON);
        this.createUser(userDto);

        Optional<User> foundedUser = this.userRepository.findUserByDocument(document);

        assertThat(foundedUser.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should not get user from DB when user does not exist")
    void findUserByDocumentFailed() {
        String document = "267354";
        Optional<User> foundedUser = this.userRepository.findUserByDocument(document);

        assertThat(foundedUser.isEmpty()).isTrue();
    }

    private User createUser(UserDto userDto) {
        User user = new User(userDto);

        this.entityManager.persist(user);
        return user;
    }
}