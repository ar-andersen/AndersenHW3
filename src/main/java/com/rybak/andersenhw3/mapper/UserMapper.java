package com.rybak.andersenhw3.mapper;

import com.rybak.andersenhw3.dto.UserRegisterDto;
import com.rybak.andersenhw3.dto.UserResponseDto;
import com.rybak.andersenhw3.entity.User;

import java.util.List;

public class UserMapper {

    public User toUser(UserRegisterDto source) {
        User target = new User();
        target.setPassword(source.getPassword());
        target.setName(source.getName());
        target.setEmail(source.getEmail());
        target.setRole(source.getRole());

        return target;
    }

    public UserResponseDto toUserResponseDto(User source) {
        UserResponseDto target = new UserResponseDto();
        target.setId(source.getId());
        target.setEmail(source.getEmail());
        target.setRole(source.getRole());
        target.setName(source.getName());

        return target;
    }

    public List<UserResponseDto> toUserResponseDtoList(List<User> source) {
        return source.stream()
                .map(this::toUserResponseDto)
                .toList();
    }

}
