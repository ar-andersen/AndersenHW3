package com.rybak.andersenhw3.storage;

import com.rybak.andersenhw3.entity.Comment;
import com.rybak.andersenhw3.entity.Project;
import com.rybak.andersenhw3.entity.Task;
import com.rybak.andersenhw3.entity.User;

import java.util.ArrayList;
import java.util.List;

public class GlobalStorage {

    public static List<User> users = new ArrayList<>();
    public static List<Project> projects = new ArrayList<>();
    public static List<Comment> comments = new ArrayList<>();
    public static List<Task> tasks = new ArrayList<>();

}
