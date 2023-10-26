package dev.ironia.simplepay.api.dtos;

import java.math.BigDecimal;

public record TransactionDto(BigDecimal value, Long sentFromId, Long sentToId) {
}
