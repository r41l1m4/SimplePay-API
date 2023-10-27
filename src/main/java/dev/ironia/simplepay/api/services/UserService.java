package dev.ironia.simplepay.api.services;

import dev.ironia.simplepay.api.domain.user.User;
import dev.ironia.simplepay.api.domain.user.UserType;
import dev.ironia.simplepay.api.dtos.UserDto;
import dev.ironia.simplepay.api.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public void validateTransaction(User sentFrom, BigDecimal amount) throws Exception{
        if(sentFrom.getUserType() == UserType.MERCHANT)
            throw new Exception("Usuário do tipo lojista não está autorizado a realizar esta transação.");

        if(sentFrom.getBalance().compareTo(amount) < 0)
            throw new Exception("Saldo suficiente.");
    }

    public User findUserById(Long id) throws Exception{
        return userRepository.findUserById(id)
                .orElseThrow(() -> new Exception("Usuário não encontrado."));
    }

    public void save(User user) {
        this.userRepository.save((user));
    }

    public User createUser(UserDto userDto) {
        User user = new User(userDto);
        this.save(user);
        return user;
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }
}
