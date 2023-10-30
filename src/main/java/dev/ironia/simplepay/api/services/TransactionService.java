package dev.ironia.simplepay.api.services;

import dev.ironia.simplepay.api.domain.transaction.Transaction;
import dev.ironia.simplepay.api.domain.user.User;
import dev.ironia.simplepay.api.dtos.TransactionDto;
import dev.ironia.simplepay.api.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class TransactionService {

    private UserService userService;
    private TransactionRepository transactionRepository;
    private AuthorizationService authorizationService;
    private NotificationService notificationService;

    public Transaction createTransaction(TransactionDto transactionDto) throws Exception {
        User sentFrom = userService.findUserById(transactionDto.sentFromId());
        User sentTo = userService.findUserById(transactionDto.sentToId());

        userService.validateTransaction(sentFrom, transactionDto.value());

        boolean isAuthorized = authorizationService.authorizeTransaction(sentFrom, transactionDto.value());
//        boolean isAuthorized = true;
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
}
