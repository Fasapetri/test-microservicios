package com.example.plazoleta.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SmsMessage {

    private String clientPhone;

    private String message;
}
