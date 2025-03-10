package com.example.plazoleta.infraestructure.client;

import com.example.plazoleta.domain.model.SmsMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "mensajeria", url = "http://localhost:8084/api/mensajeria")
public interface MensajeriaSmsFeignClient {

    @PostMapping("/send")
    void sendSms(@RequestBody SmsMessage smsMessage);
}
