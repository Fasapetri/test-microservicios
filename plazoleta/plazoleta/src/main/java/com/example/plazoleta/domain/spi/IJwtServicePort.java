package com.example.plazoleta.domain.spi;

import com.example.plazoleta.domain.model.User;

public interface IJwtServicePort {

    User validateToken(String token);
}
