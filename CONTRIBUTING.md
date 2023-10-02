# Contributing

Thank you for your interest in contributing to `java-tree-sitter`!
We appreciate your efforts to make this project better.
Before you start, please take a moment to read and understand the following guidelines.

## Getting Started

1. Fork the repository and clone your fork locally.
2. Ensure the local development prerequisites are met, as outlined in the [README](README.md).
3. Create a new branch for your contribution.
   The branch name should follow the format of: `label/short-name`.
   For more information on valid labels, see the [full list](https://github.com/seart-group/java-tree-sitter/labels).
4. Test your changes thoroughly via the build system: `mvn clean package`.
   If new features are being added, they **must** be accompanied by tests.
5. Push your changes to your fork: `git push origin label/short-name`.
6. Open a pull request (PR) against the `master` branch of this repository.

## Pull Request Process

Ensure your PR description explains the purpose of your changes and provides context.
Be sure to reference related issues by using [linking keywords](https://docs.github.com/en/issues/tracking-your-work-with-issues/linking-a-pull-request-to-an-issue). 
Ensure your PR includes only relevant changes.
Large changes should be broken into smaller, manageable PRs.
Be prepared to make adjustments based on feedback received both by the automated actions and reviewers.

## Style Guide

We use an automated [CheckStyle action](.github/workflows/checkstyle.yml) to enforce the project code style.
To get a rough idea of the style we employ, refer to the [CheckStyle configuration](checkstyle.xml).

## License

By contributing to this project, you agree that your contributions will be licensed under the [project license](LICENSE).
