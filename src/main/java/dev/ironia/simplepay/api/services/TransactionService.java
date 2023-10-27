package dev.ironia.simplepay.api.services;

import dev.ironia.simplepay.api.domain.transaction.Transaction;
import dev.ironia.simplepay.api.domain.user.User;
import dev.ironia.simplepay.api.dtos.TransactionDto;
import dev.ironia.simplepay.api.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@AllArgsConstructor
public class TransactionService {

    private UserService userService;
    private TransactionRepository transactionRepository;
    private RestTemplate restTemplate;
    private NotificationService notificationService;

    public Transaction createTransaction(TransactionDto transactionDto) throws Exception {
        User sentFrom = userService.findUserById(transactionDto.sentFromId());
        User sentTo = userService.findUserById(transactionDto.sentToId());

        userService.validateTransaction(sentFrom, transactionDto.value());

        boolean isAuthorized = authorizeTransaction(sentFrom, transactionDto.value());
        if(!isAuthorized)
            throw new Exception("Transação não autorizada.");

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDto.value());
        transaction.setSentFrom(sentFrom);
        transaction.setSentTo(sentTo);
        transaction.setTimestamp(LocalDateTime.now());

        sentFrom.setBalance(sentFrom.getBalance().subtract(transactionDto.value()));
        sentTo.setBalance(sentTo.getBalance().add(transactionDto.value()));

        transactionRepository.save(transaction);
        userService.save(sentFrom);
        userService.save(sentTo);
        
        this.notificationService.sendNotification(
                sentFrom,
                "R$ " + transaction.getAmount() + " enviado para " + sentTo.getFirstName() + " " + sentTo.getLastName() + "."
        );
        this.notificationService.sendNotification(
                sentTo,
                "R$ " + transaction.getAmount() + " recebido de " + sentFrom.getFirstName() + " " + sentFrom.getLastName() + "."
        );

        return transaction;
    }

    public boolean authorizeTransaction(User sentFrom, BigDecimal value) {
        ResponseEntity<Map> authResponse = restTemplate.getForEntity("https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6", Map.class);

        if(authResponse.getStatusCode() == HttpStatus.OK) {
            String message = (String) authResponse.getBody().get("message");
            return "Autorizado".equalsIgnoreCase(message);
        }else return false;
    }
}
