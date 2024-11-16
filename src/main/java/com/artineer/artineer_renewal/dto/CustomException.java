package com.artineer.artineer_renewal.dto;

import org.springframework.security.access.AccessDeniedException;

public class CustomException extends AccessDeniedException {
    public String message;
    public CustomException(String message) {
        super(message);
        this.message = message;
    }
}
