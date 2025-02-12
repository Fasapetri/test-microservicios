package com.example.users.application.mapper;

import com.example.users.application.dto.AuthRequest;
import com.example.users.application.dto.AuthResponse;
import com.example.users.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
unmappedTargetPolicy = ReportingPolicy.IGNORE,
unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AuthMapper {

    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

    User authRequestToUser(AuthRequest authRequest);

    AuthRequest userToAuthRequest(User user, String token);

    default AuthResponse authRequestToAuthResponse(AuthRequest authRequest, String token){
        return AuthResponse.builder()
                .email(authRequest.getEmail())
                .token(token)
                .build();
    }
}
