package com.example.ordersapi.user.service.impl;

import com.example.ordersapi.user.api.dto.UserDto;
import com.example.ordersapi.user.entity.User;
import com.example.ordersapi.user.exception.EmailAlreadyInUseException;
import com.example.ordersapi.user.mapper.UserMapper;
import com.example.ordersapi.user.repository.UserRepository;
import com.example.ordersapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User registerUser(UserDto userDto) throws EmailAlreadyInUseException {
        try {
            User user = userMapper.userDtoToUser(userDto);
            User savedUser = userRepository.saveAndFlush(user);

            return savedUser;
        } catch (DataIntegrityViolationException ex) {
            throw new EmailAlreadyInUseException(userDto.getEmail());
        }
    }

}
