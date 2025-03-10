package com.example.pedidos.infraestructure.adapter;

import com.example.pedidos.domain.spi.ISmsServicePort;
import com.example.pedidos.infraestructure.constants.SecurityContextAdapterConstants;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SmsAdapter implements ISmsServicePort {

    @Value(SecurityContextAdapterConstants.VALUE_ACCOUNT_SID_TWILIO)
    private String accountSid;

    @Value(SecurityContextAdapterConstants.VALUE_OUTH_TOKEN_TWILIO)
    private String outhToken;

    @Value(SecurityContextAdapterConstants.VALUE_NUMBER_TWILIO)
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
