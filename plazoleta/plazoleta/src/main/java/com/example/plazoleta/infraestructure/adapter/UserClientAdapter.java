package com.example.plazoleta.infraestructure.adapter;

import com.example.plazoleta.application.dto.UserResponse;
import com.example.plazoleta.domain.model.User;
import com.example.plazoleta.domain.spi.IUserClientPort;
import com.example.plazoleta.infraestructure.client.UserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserClientAdapter implements IUserClientPort {

    private final UserFeignClient userFeignClient;

    @Override
    public User getUserById(Long findUserId) {
        UserResponse foundUser =  userFeignClient.findById(findUserId);
        return new User(foundUser.getId(), foundUser.getEmail(), foundUser.getRol());

    }
}
