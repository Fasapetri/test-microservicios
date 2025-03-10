package com.example.plazoleta.domain.spi;

import com.example.plazoleta.domain.model.SmsMessage;

public interface IMensajeriaSmsPersistencePort {

    void sendSms(SmsMessage smsMessage);

}
