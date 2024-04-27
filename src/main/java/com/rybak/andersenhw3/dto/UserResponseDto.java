package com.rybak.andersenhw3.dto;

import com.rybak.andersenhw3.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private UUID id;
    private String name;
    private String email;
    private Role role;

}
