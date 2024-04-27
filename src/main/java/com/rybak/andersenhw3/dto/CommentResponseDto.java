package com.rybak.andersenhw3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {

    private UUID id;
    private String message;
    private UserResponseDto user;

}
