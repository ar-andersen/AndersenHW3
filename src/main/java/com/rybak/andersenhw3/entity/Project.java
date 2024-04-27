package com.rybak.andersenhw3.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    private UUID id;
    private String name;
    private String description;
    private List<User> team = new ArrayList<>();
    private List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        tasks.add(task);
    }

    public void addUser(User user) {
        if (team == null) {
            team = new ArrayList<>();
        }
        team.add(user);
    }

}
