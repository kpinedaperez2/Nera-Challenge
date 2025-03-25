package com.local.accounts.application.exception;

public class AccountNotFoundException extends UseCaseException {
  public AccountNotFoundException(String message) {
    super(message);
  }
}
