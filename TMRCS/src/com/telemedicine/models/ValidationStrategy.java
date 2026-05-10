package com.telemedicine.models;

public interface ValidationStrategy {
    boolean validate(String value);
    String getErrorMessage();
}

class EmailValidationStrategy implements ValidationStrategy {
    private static final String EMAIL_PATTERN = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
    private String errorMessage = "";

    @Override
    public boolean validate(String value) {
        if (value == null || value.trim().isEmpty()) {
            errorMessage = "Email cannot be empty.";
            return false;
        }
        if (!value.trim().matches(EMAIL_PATTERN)) {
            errorMessage = "Invalid email format: " + value;
            return false;
        }
        return true;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}

class PhoneValidationStrategy implements ValidationStrategy {
    private static final String PHONE_PATTERN = "^\\+?(?=(?:\\D*\\d){10,15}\\D*$)[\\d\\s\\-()]+$";
    private String errorMessage = "";

    @Override
    public boolean validate(String value) {
        if (value == null || value.trim().isEmpty()) {
            errorMessage = "Phone number cannot be empty.";
            return false;
        }
        if (!value.trim().matches(PHONE_PATTERN)) {
            errorMessage = "Invalid phone number format. Must be 10-15 digits.";
            return false;
        }
        return true;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}

class NameValidationStrategy implements ValidationStrategy {
    private String errorMessage = "";

    @Override
    public boolean validate(String value) {
        if (value == null || value.trim().isEmpty()) {
            errorMessage = "Name cannot be empty.";
            return false;
        }
        if (value.trim().length() < 2) {
            errorMessage = "Name must be at least 2 characters.";
            return false;
        }
        return true;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}

class Validator {
    private ValidationStrategy strategy;
    private String lastError = "";

    public Validator(ValidationStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(ValidationStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean validate(String value) {
        boolean result = strategy.validate(value);
        if (!result) {
            lastError = strategy.getErrorMessage();
        }
        return result;
    }

    public String getLastError() {
        return lastError;
    }
}