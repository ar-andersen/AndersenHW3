package com.rybak.andersenhw3.mapper;

import com.rybak.andersenhw3.dto.ProjectCreateDto;
import com.rybak.andersenhw3.dto.ProjectResponseDto;
import com.rybak.andersenhw3.dto.TaskResponseDto;
import com.rybak.andersenhw3.dto.UserResponseDto;
import com.rybak.andersenhw3.entity.Project;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class ProjectMapperTest {

    @Mock
    private UserMapper userMapper;
    @Mock
    private TaskMapper taskMapper;
    @InjectMocks
    private ProjectMapper projectMapper;

    @Test
    void toProject_ShouldReturnProject() {
        String name = "name";
        String description = "description";
        Project expected = new Project(null, name, description, Collections.emptyList(), Collections.emptyList());
        ProjectCreateDto projectCreateDto = new ProjectCreateDto(name, description);

        Project actual = projectMapper.toProject(projectCreateDto);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void toProjectResponseDto_ShouldReturnProjectResponseDto() {
        UUID projectId = UUID.randomUUID();
        String name = "name";
        String description = "description";
        Project project = new Project(projectId, name, description, Collections.emptyList(), Collections.emptyList());
        List<UserResponseDto> userResponseDtoList = Collections.emptyList();
        List<TaskResponseDto> taskResponseDtoList = Collections.emptyList();
        ProjectResponseDto expected = new ProjectResponseDto(
                projectId, name, description, userResponseDtoList, taskResponseDtoList);
        Mockito.when(userMapper.toUserResponseDtoList(Collections.emptyList())).thenReturn(userResponseDtoList);
        Mockito.when(taskMapper.toTaskResponseDtoList(Collections.emptyList())).thenReturn(taskResponseDtoList);

        ProjectResponseDto actual = projectMapper.toProjectResponseDto(project);

        Assertions.assertEquals(expected, actual);
    }

}