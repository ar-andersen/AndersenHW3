# Task Manager Service

TaskManager is an application designed to help users efficiently organize,
track, and collaborate on various tasks and projects. 
With its robust features, Task Manager streamlines task management, enabling users to stay productive and focused on their goals.

Key Features:

- Task Creation: Users can easily create new tasks by providing a title, description, and any necessary details or instructions.
- Task Tracking: TaskManager allows users to view a comprehensive list of their tasks.
- Task Management: Users have full control over their tasks, with the ability to modify task statuses (e.g., mark as completed), edit details, and add comments or updates as needed.

Whether you're managing personal projects, coordinating team efforts, or tracking work assignments, 
Task Manager provides the tools you need to stay organized, productive, and on top of your tasks. In other words, this application 
is a low-level JIRA simulation.

## API

Available endpoints:
- /auth/register: SignUp in application. Request body: 
```
{
    "name": string,
    "email": string,
    "password": string,
    "role": string
}
```
- /auth/login: SignIn in application. Request body:
```
{
    "email": string,
    "password": string
}
```
- /projects (POST): Create project. Request body:
```
{
    "name": string,
    "description": string
}
```
- /projects (GET): Get all projects.
- /projects/{projectId} (GET): Get project by id
- /projects/{projectId} (DELETE): Delete project by id.
- /projects/{projectId}/tasks (POST): Create task in project. Request body:
```
{
    "title": string,
    "description": string,
    "reporterId": string
}
```
- /projects/{projectId}/tasks (GET): Get all tasks by project id.
- /projects/{projectId}/tasks (PUT): Update task by project id. Request body:
```
{
    "title": string,
    "description": string,
    "reporterId": string,
    "assigneeId": string,
    "status": string
}
```
- /projects/{projectId}/users (POST): Assign user to a project. Request body:
```
{
    "userId": string
}
```
- /projects/{projectId}/users (DELETE): Remove user from project team.
- /users (GET): Get all users.
- /users/{userId} (GET): Get user by id.
- /users/{userId} (DELETE): Delete user by id.
- /tasks/{taskId}/comments (POST): Create comment in task. Request body:
```
{
    "message": string,
    "userId": string
}
```
Request parameters: projectId - required
- /tasks/{taskId}/comments (GET): Get all comments by task id. Request parameters: projectId - required
- /tasks/{taskId} (DELETE): Delete task by id.

Constraints:
- status: TO_DO, IN_PROGRESS, RESOLVED, IN_TESTING, DONE
- role: PM, DEV, QA, DM
- create and delete project can only DM (Delivery Manager)
- you should be authorized to run make requests to all endpoints except signIn and signUp. 


## Run Local

#### Docker
Instruction to run application via docker 
1) Clone the project
```
git clone https://github.com/ar-andersen/AndersenHW3
```
2) go to project folder
3) package the project
```
mvn package
```
4) build docker image
```
docker build -t andersenhw3:latest .
```
5) run docker image
```
docker run -p 8080:8080 andersenhw3:latest
```
Simplify instruction by running application in docker
1) If you're bored to run all commands in instruction above, you can just run docker image from dockerhub repository:
```
docker run -p 8080:8080 alexanderrybak/andersenhw3:latest
```

#### Test API

For test API fast, you can import postman collection with all endpoints. 
Filename: Task Manager.postman_collection.json in root of the repository.

