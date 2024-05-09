CREATE TABLE tasks
(
    id          UUID PRIMARY KEY,
    title       VARCHAR(255),
    description VARCHAR(255),
    status      VARCHAR(20),
    assignee_id UUID REFERENCES users (id) ON UPDATE CASCADE ON DELETE SET NULL,
    reporter_id UUID REFERENCES users (id) ON UPDATE CASCADE ON DELETE SET NULL,
    project_id  UUID REFERENCES projects (id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE comments
(
    id      UUID PRIMARY KEY,
    title   VARCHAR(255),
    user_id UUID REFERENCES users (id) ON UPDATE CASCADE ON DELETE SET NULL,
    task_id UUID REFERENCES tasks (id) ON UPDATE CASCADE ON DELETE CASCADE
);