package com.rybak.andersenhw3.mapper;

import com.rybak.andersenhw3.dto.CommentResponseDto;
import com.rybak.andersenhw3.dto.TaskCreateDto;
import com.rybak.andersenhw3.dto.TaskResponseDto;
import com.rybak.andersenhw3.entity.Comment;
import com.rybak.andersenhw3.entity.Status;
import com.rybak.andersenhw3.entity.Task;
import com.rybak.andersenhw3.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class TaskMapperTest {

    @Mock
    private CommentMapper commentMapper;
    @InjectMocks
    private TaskMapper taskMapper;

    @Test
    void toTask_ShouldReturnTask() {
        String title = "title";
        String description = "description";
        String reporterId = "reporterId";
        Task expected = new Task(null, title, description, null, null, null, null, null);
        TaskCreateDto taskCreateDto = new TaskCreateDto(title, description, reporterId);

        Task actual = taskMapper.toTask(taskCreateDto);

        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
    }

    @Test
    void toTaskResponseDto_ShouldReturnTaskResponseDto() {
        UUID taskId = UUID.randomUUID();
        Status status = Status.TO_DO;
        User assignee = new User();
        User reporter = new User();
        List<Comment> comments = new ArrayList<>();
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        String title = "title";
        String description = "description";
        Task task = new Task(taskId, title, description, status, assignee, reporter, null, comments);
        TaskResponseDto expected = new TaskResponseDto(taskId, title, description, status, assignee, reporter,
                commentResponseDtoList);
        Mockito.when(commentMapper.toCommentResponseDtoList(comments)).thenReturn(commentResponseDtoList);

        TaskResponseDto actual = taskMapper.toTaskResponseDto(task);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void toTaskResponseDtoList_ShouldReturnTaskResponseDtoList() {
        UUID taskId = UUID.randomUUID();
        Status status = Status.TO_DO;
        User assignee = new User();
        User reporter = new User();
        List<Comment> comments = new ArrayList<>();
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        String title = "title";
        String description = "description";
        Task task = new Task(taskId, title, description, status, assignee, reporter, null, comments);
        List<Task> tasks = List.of(task);
        TaskResponseDto taskResponseDto = new TaskResponseDto(taskId, title, description, status, assignee, reporter,
                commentResponseDtoList);
        List<TaskResponseDto> expected = List.of(taskResponseDto);
        Mockito.when(commentMapper.toCommentResponseDtoList(comments)).thenReturn(commentResponseDtoList);

        List<TaskResponseDto> actual = taskMapper.toTaskResponseDtoList(tasks);

        Assertions.assertEquals(expected, actual);
    }

}