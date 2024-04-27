package com.rybak.andersenhw3.dto;

import com.rybak.andersenhw3.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {

    private String name;
    private String email;
    private String password;
    private Role role;

}
