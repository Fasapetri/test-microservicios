package com.example.users.infraestructure.output.jpa.adapter;

import com.example.users.domain.model.User;
import com.example.users.domain.spi.IUserPersistencePort;
import com.example.users.infraestructure.exception.UserEntityException;
import com.example.users.infraestructure.exception.UserEntityExceptionType;
import com.example.users.infraestructure.output.jpa.entity.UserEntity;
import com.example.users.infraestructure.output.jpa.mapper.UserEntityMapper;
import com.example.users.infraestructure.output.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserJpaAdapter implements IUserPersistencePort {

    private final IUserRepository iUserRepository;
    private final UserEntityMapper userEntityMapper;

    @Override
    public User saveUser(User user) {
        if(iUserRepository.findByEmail(user.getEmail()).isPresent()){
            throw new UserEntityException(UserEntityExceptionType.USER_ALREADY_EXISTS);
        }
        UserEntity savedUserEntity = iUserRepository.save(userEntityMapper.toUserEntity(user));
        return userEntityMapper.toUser(savedUserEntity);
    }

    @Override
    public User findByEmailUser(String email) {
        UserEntity user = iUserRepository.findByEmail(email).orElseThrow(() -> new UserEntityException(UserEntityExceptionType.USER_NOT_FOUND));
        return userEntityMapper.toUser(user);
    }

    @Override
    public User findByIdUser(Long id) {
        UserEntity user = iUserRepository.findById(id).orElseThrow(() -> new UserEntityException(UserEntityExceptionType.USER_NOT_FOUND));
        return userEntityMapper.toUser(user);
    }

    @Override
    public User updateUser(User user) {
        UserEntity updateUserEntity = iUserRepository.save(userEntityMapper.toUserEntity(user));
        return userEntityMapper.toUser(updateUserEntity);
    }

    @Override
    public void deleteUser(Long userId) {
        iUserRepository.deleteById(userId);
    }

    @Override
    public List<User> getAllUser() {
        List<UserEntity> listUser = iUserRepository.findAll();
        if(listUser.isEmpty()){
            throw new UserEntityException(UserEntityExceptionType.USER_NOT_DATA);
        }
        return userEntityMapper.toUserList(listUser);
    }
}
