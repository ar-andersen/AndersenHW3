package com.rybak.andersenhw3.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private UUID id;
    private String message;
    private User user;

}
