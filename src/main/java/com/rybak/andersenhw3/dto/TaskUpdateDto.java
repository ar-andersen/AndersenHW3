package com.rybak.andersenhw3.dto;

import com.rybak.andersenhw3.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskUpdateDto {

    private String title;
    private String description;
    private Status status;
    private String assigneeId;
    private String reporterId;

}
