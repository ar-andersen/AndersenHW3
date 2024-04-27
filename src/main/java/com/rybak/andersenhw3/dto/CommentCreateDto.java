package com.rybak.andersenhw3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateDto {

    private String message;
    private String userId;

}
