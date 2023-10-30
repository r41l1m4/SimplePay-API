package dev.ironia.simplepay.api.services;

import dev.ironia.simplepay.api.domain.user.User;
import dev.ironia.simplepay.api.domain.user.UserType;
import dev.ironia.simplepay.api.dtos.TransactionDto;
import dev.ironia.simplepay.api.repositories.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private NotificationService notificationService;

    @Autowired
    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create transaction successfully when everything's OK")
    void createTransactionSucceeded() throws Exception {

        User sentFrom = new User(1L, "John", "Doe", "12345", "john.doe@doe.com", "johndoe", new BigDecimal(10), UserType.COMMON);
        User sentTo = new User(2L, "Jane", "Doe", "12345", "jane.doe@doe.com", "janedoe", new BigDecimal(10), UserType.COMMON);

        when(userService.findUserById(1L)).thenReturn(sentFrom);
        when(userService.findUserById(2L)).thenReturn(sentTo);

        when(authorizationService.authorizeTransaction(any(), any())).thenReturn(true);

        TransactionDto request = new TransactionDto(new BigDecimal(10), 1L, 2L);
        transactionService.createTransaction(request);

        verify(transactionRepository, times(1)).save(any());

        sentFrom.setBalance(new BigDecimal(0));
        verify(userService, times(1)).save(sentFrom);

        sentTo.setBalance(new BigDecimal(20));
        verify(userService, times(1)).save(sentTo);

        verify(notificationService, times(1)).sendNotification(sentFrom, "R$ 10 enviado para " + sentTo.getFirstName() + " " + sentTo.getLastName() + ".");
        verify(notificationService, times(1)).sendNotification(sentTo, "R$ 10 recebido de " + sentFrom.getFirstName() + " " + sentFrom.getLastName() + ".");
    }

    @Test
    @DisplayName("Should threw exception when transaction's not allowed")
    void createTransactionUnauthorized() throws Exception {

        User sentFrom = new User(1L, "John", "Doe", "12345", "john.doe@doe.com", "johndoe", new BigDecimal(10), UserType.COMMON);
        User sentTo = new User(2L, "Jane", "Doe", "12345", "jane.doe@doe.com", "janedoe", new BigDecimal(10), UserType.COMMON);

        when(userService.findUserById(1L)).thenReturn(sentFrom);
        when(userService.findUserById(2L)).thenReturn(sentTo);

        when(authorizationService.authorizeTransaction(any(), any())).thenReturn(false);

        Exception thrown = Assertions.assertThrows(
                Exception.class,
                () -> {
                    TransactionDto request = new TransactionDto(new BigDecimal(10), 1L, 2L);
                    transactionService.createTransaction(request);
        });

        Assertions.assertEquals("Transação não autorizada.", thrown.getMessage());
    }
}