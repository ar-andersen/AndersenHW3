CREATE TABLE projects
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(255),
    description VARCHAR(255)
);

CREATE TABLE projects_users
(
    user_id UUID REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE,
    project_id UUID REFERENCES projects (id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT projects_users_pkey PRIMARY KEY (user_id, project_id)
)