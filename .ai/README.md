# LoomCraft Admin AI Knowledge Base

This folder serves as the central intelligence hub for the LoomCraft Admin Android project.

## Structure

- **`knowledge/`**: Contains core project documentation, architecture decisions, and domain information.
- **`tasks/`**: Task tracking system.
    - `planned/`: Backlog of tasks to be worked on.
    - `current/`: Tasks currently in progress.
    - `completed/`: Historical record of finished tasks.
- **`resources/`**: Technical assets like DB schemas, API specs, and SQL dumps.
- **`task-inbox.md`**: The entry point for new requirements. Add your thoughts here, and I will process them into structured tasks.

## Workflow

1. **User** adds a request to `task-inbox.md`.
2. **AI** converts the request into a detailed markdown file in `tasks/planned/`.
3. When work starts, the task file is moved to `tasks/current/`.
4. Upon completion, the task file is moved to `tasks/completed/`.
