package com.example.plazoleta.domain.spi;

import com.example.plazoleta.domain.model.User;

public interface IUserClientPort {

    User getUserById(Long userId);

    boolean existsUserById(Long findUserId);
}
