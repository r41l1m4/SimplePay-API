package dev.ironia.simplepay.api.domain.transaction;

import dev.ironia.simplepay.api.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "transactions")
@Table(name = "transactions")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
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

    private LocalDateTime timestamp;
}
