package com.rybak.andersenhw3.mapper;

import com.rybak.andersenhw3.dto.TaskCreateDto;
import com.rybak.andersenhw3.dto.TaskResponseDto;
import com.rybak.andersenhw3.entity.Task;

import java.util.List;

public class TaskMapper {

    private final CommentMapper commentMapper;

    public TaskMapper(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    public Task toTask(TaskCreateDto source) {
        Task target = new Task();
        target.setTitle(source.getTitle());
        target.setDescription(source.getDescription());

        return target;
    }

    public TaskResponseDto toTaskResponseDto(Task source) {
        TaskResponseDto target = new TaskResponseDto();
        target.setId(source.getId());
        target.setStatus(source.getStatus());
        target.setDescription(source.getDescription());
        target.setTitle(source.getTitle());
        target.setAssignee(source.getAssignee());
        target.setReporter(source.getReporter());
        target.setComments(commentMapper.toCommentResponseDtoList(source.getComments()));

        return target;
    }

    public List<TaskResponseDto> toTaskResponseDtoList(List<Task> source) {
        return source.stream()
                .map(this::toTaskResponseDto)
                .toList();
    }

}
