package com.rybak.andersenhw3.mapper;

import com.rybak.andersenhw3.dto.ProjectCreateDto;
import com.rybak.andersenhw3.dto.ProjectResponseDto;
import com.rybak.andersenhw3.entity.Project;

public class ProjectMapper {

    private final UserMapper userMapper;
    private final TaskMapper taskMapper;

    public ProjectMapper(UserMapper userMapper, TaskMapper taskMapper) {
        this.userMapper = userMapper;
        this.taskMapper = taskMapper;
    }

    public ProjectResponseDto toProjectResponseDto(Project source) {
        ProjectResponseDto target = new ProjectResponseDto();
        target.setId(source.getId());
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setTeam(userMapper.toUserResponseDtoList(source.getTeam()));
        target.setTasks(taskMapper.toTaskResponseDtoList(source.getTasks()));

        return target;
    }

    public Project toProject(ProjectCreateDto source) {
        Project target = new Project();
        target.setName(source.getName());
        target.setDescription(source.getDescription());

        return target;
    }

}
