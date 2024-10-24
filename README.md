# Development Guide

Please 🥺 read this guide before starting development.

## Table of Contents

- [Development Guide](#development-guide)
    - [Table of Contents](#table-of-contents)
    - [Prerequisites](#prerequisites)
    - [Getting Started](#getting-started)
    - [Project Structure](#project-structure)
    - [Development Workflow](#development-workflow)
        - [Branch Naming](#branch-naming)
        - [Commit Message](#commit-message)
        - [Pull Request](#pull-request)
    - [Code Style](#code-style)

## Prerequisites

The project requires the following tools to be installed on your system: Git, IntelliJ IDEA, Docker.

## Getting Started

1. Clone the repository directly from IntelliJ IDEA:
   2. File
   3. New
   4. Project from Version Control
   5. Git
   6. Enter the repository URL: `https://github.com/denis-vp/pawpal-backend.git`


2. Create a `.env` file in the root directory and add the following environment variables:

   ```dotenv
    NOTHING_JUST_YET
    ```

## Project Structure

Please 🥺 maintain a tidy project structure.

## Development Workflow

### Branch Naming

Please 🥺 follow the naming convention below when creating branches:

- `feature/feature-name`
- `bugfix/bug-name`
- `refactor/refactor-name`
- `chore/chore-name`
- `docs/docs-name`

### Commit Message

Please 🥺 make your commit messages imperative, i.e not past tense.  
Example: `Add health record filtering` instead of `Added health record filtering`.

### Pull Request

Pushing directly to the `main` branch is not allowed.  
Please pull and merge the latest changes from the `main` branch before creating a pull request.

## Code Style

Please 🥺 follow the code style guide below:

- Use camelCase for variable names.
- Use PascalCase for component names.
