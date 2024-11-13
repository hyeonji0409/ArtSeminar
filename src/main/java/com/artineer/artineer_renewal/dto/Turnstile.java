package com.artineer.artineer_renewal.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Turnstile {
    private String secretKey;
    private String response;
    private String remoteip;
}
