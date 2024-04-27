package com.rybak.andersenhw3.util;

import java.util.regex.Pattern;

public class RegexUtil {

    public static final String PROJECT_BASE_URL = "/projects";
    public static final String TASK_BASE_URL = "/tasks";
    public static final String COMMENT_BASE_URL = "/comments";
    public static final String USERS_BASE_URL = "/users";
    public static final String UUID_REGEX = "/([a-f0-9\\\\-]+)";
    public static final String TASKS_REGEX = UUID_REGEX + TASK_BASE_URL;
    public static final String USERS_REGEX = UUID_REGEX + USERS_BASE_URL;
    public static final String TASKS_WITH_ID_REGEX = UUID_REGEX + TASK_BASE_URL + UUID_REGEX;
    public static final String COMMENTS_REGEX = UUID_REGEX + COMMENT_BASE_URL;
    public static final String PROJECT_REGEX = PROJECT_BASE_URL + UUID_REGEX;
    public static final String PROJECT_WITH_ID_AND_TASK_REGEX = PROJECT_BASE_URL + TASKS_REGEX;
    public static final String PROJECT_WITH_ID_AND_USER_REGEX = PROJECT_BASE_URL + USERS_REGEX;
    public static final String TASK_WITH_ID_AND_COMMENT_REGEX = TASK_BASE_URL + UUID_REGEX + COMMENT_BASE_URL;
    public static final Pattern UUID_PATTERN = Pattern.compile(UUID_REGEX);
    public static final Pattern TASKS_PATTERN = Pattern.compile(TASKS_REGEX);
    public static final Pattern USERS_PATTERN = Pattern.compile(USERS_REGEX);
    public static final Pattern TASKS_WITH_ID_PATTERN = Pattern.compile(TASKS_WITH_ID_REGEX);
    public static final Pattern COMMENTS_PATTERN = Pattern.compile(COMMENTS_REGEX);
    public static final Pattern PROJECT_PATTERN = Pattern.compile(PROJECT_REGEX);
    public static final Pattern PROJECT_WITH_ID_AND_TASK_PATTERN = Pattern.compile(PROJECT_WITH_ID_AND_TASK_REGEX);
    public static final Pattern TASK_WITH_ID_AND_COMMENT_PATTERN = Pattern.compile(TASK_WITH_ID_AND_COMMENT_REGEX);
    public static final Pattern PROJECT_WITH_ID_AND_USER_PATTERN = Pattern.compile(PROJECT_WITH_ID_AND_USER_REGEX);

    private RegexUtil() {
        throw new UnsupportedOperationException();
    }

}
