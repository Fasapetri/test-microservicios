package com.example.plazoleta.infraestructure.adapter;

import com.example.plazoleta.domain.model.SmsMessage;
import com.example.plazoleta.domain.spi.IMensajeriaSmsPersistencePort;
import com.example.plazoleta.infraestructure.client.MensajeriaSmsFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MensajeriaSmsClientAdapter implements IMensajeriaSmsPersistencePort {

    private final MensajeriaSmsFeignClient mensajeriaSmsFeignClient;
    @Override
    public void sendSms(SmsMessage smsMessage) {
        mensajeriaSmsFeignClient.sendSms(smsMessage);
    }
}
