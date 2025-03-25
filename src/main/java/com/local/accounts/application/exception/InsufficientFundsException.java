package com.local.accounts.application.exception;

public class InsufficientFundsException extends UseCaseException {
  public InsufficientFundsException(String message) {
    super(message);
  }
}
