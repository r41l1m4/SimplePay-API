package dev.ironia.simplepay.api.domain.transaction;

import dev.ironia.simplepay.api.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity(name = "transactions")
@Table(name = "transactions")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "sentFrom_id")
    private User sentFrom;

    @ManyToOne
    @JoinColumn(name = "sentTo_id")
    private User sentTo;

    private LocalDate timestamp;
}
