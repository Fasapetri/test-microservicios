package com.example.pedidos.infraestructure.adapter;

import com.example.pedidos.domain.spi.ISmsServicePort;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SmsAdapter implements ISmsServicePort {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String outhToken;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    @Override
    public Mono<Void> sendSms(String numclient, String message) {
        return Mono.fromRunnable(() -> {
            Twilio.init(accountSid, outhToken);
            Message.creator(
                    new PhoneNumber(numclient),
                    new PhoneNumber(twilioPhoneNumber),
                    message
            ).create();
        }).then();
    }
}
