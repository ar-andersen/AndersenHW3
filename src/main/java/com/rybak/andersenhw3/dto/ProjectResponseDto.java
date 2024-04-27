package com.rybak.andersenhw3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponseDto {

    private UUID id;
    private String name;
    private String description;
    private List<UserResponseDto> team = new ArrayList<>();
    private List<TaskResponseDto> tasks = new ArrayList<>();

}
