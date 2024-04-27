package com.rybak.andersenhw3.mapper;

import com.rybak.andersenhw3.dto.UserRegisterDto;
import com.rybak.andersenhw3.dto.UserResponseDto;
import com.rybak.andersenhw3.entity.Role;
import com.rybak.andersenhw3.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    void toUser_ShouldReturnUser() {
        String name = "name";
        String email = "email@gmail.com";
        String password = "password";
        Role role = Role.DEV;
        User expected = new User(null, name, email, password, role);
        UserRegisterDto userRegisterDto = new UserRegisterDto(name, email, password, role);

        User actual = userMapper.toUser(userRegisterDto);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void toUserResponseDto_ShouldReturnUserResponseDto() {
        String name = "name";
        String email = "email@gmail.com";
        UUID id = UUID.randomUUID();
        Role role = Role.DEV;
        User user = new User(id, name, email, null, role);
        UserResponseDto expected = new UserResponseDto(id, name, email, role);

        UserResponseDto actual = userMapper.toUserResponseDto(user);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void toUserResponseDtoList_ShouldReturntoUserResponseDtoList() {
        String name = "name";
        String email = "email@gmail.com";
        UUID id = UUID.randomUUID();
        Role role = Role.DEV;
        User user = new User(id, name, email, null, role);
        List<User> users = List.of(user);
        UserResponseDto userResponseDto = new UserResponseDto(id, name, email, role);
        List<UserResponseDto> expected = List.of(userResponseDto);

        List<UserResponseDto> actual = userMapper.toUserResponseDtoList(users);

        Assertions.assertEquals(expected, actual);
    }

}