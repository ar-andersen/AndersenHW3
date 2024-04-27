package com.rybak.andersenhw3.dto;

import com.rybak.andersenhw3.entity.Status;
import com.rybak.andersenhw3.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDto {

    private UUID id;
    private String title;
    private String description;
    private Status status;
    private User assignee;
    private User reporter;
    private List<CommentResponseDto> comments = new ArrayList<>();

}
