package dev.ironia.simplepay.api.dtos;

import dev.ironia.simplepay.api.domain.user.UserType;

import java.math.BigDecimal;

public record UserDto(String firstName, String lastName, String document, BigDecimal balance, String email, String password, UserType userType) {
}
