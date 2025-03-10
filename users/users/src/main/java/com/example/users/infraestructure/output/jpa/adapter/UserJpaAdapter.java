package com.example.users.infraestructure.output.jpa.adapter;

import com.example.users.domain.model.User;
import com.example.users.domain.spi.IUserPersistencePort;
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
        UserEntity mapperUserEntity = userEntityMapper.toUserEntity(user);
        UserEntity savedUser = iUserRepository.save(mapperUserEntity);
        return userEntityMapper.toUser(savedUser);
    }

    @Override
    public User findByEmailUser(String email) {
        UserEntity user = iUserRepository.findByEmail(email).orElse(null);
        return userEntityMapper.toUser(user);
    }

    @Override
    public User findByIdUser(Long id) {
        UserEntity user = iUserRepository.findById(id).orElse(null);
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
        return userEntityMapper.toUserList(iUserRepository.findAll());
    }

    @Override
    public boolean existsUserById(Long findUserId) {
        return iUserRepository.existsById(findUserId);
    }
}
