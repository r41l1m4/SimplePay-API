package dev.ironia.simplepay.api.repositories;

import dev.ironia.simplepay.api.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
