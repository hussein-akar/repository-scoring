# Repository Scoring

The GitHub Repository Search API is a powerful tool for exploring GitHub repositories based on
specific criteria, such as programming language and creation date. This README provides a
comprehensive guide on how to use and contribute to the project.

## Features:

- **Search Criteria:** Users can filter repositories by language (mandatory) and creation date (
  optional).
- **Sorting:** Results are always ordered by stars count in descending order.
- **Popularity Score:** Each repository's popularity score is calculated based on stars count, forks
  count, and the recency of updates.
- **Swagger UI:** Access the API documentation and interact with the endpoints using Swagger UI at
  localhost:8080/swagger-ui/html.

## Prerequisites:

- Generate a token on GitHub (Settings > Developer settings > Personal access tokens)

## Setup:

1. Clone the repository: git clone https://github.com/hussein-akar/repository-scoring.git
2. Navigate to the project directory: `cd repository-scoring`
3. Export GitHub token: `export GITHUB_TOKEN=your_github_token_value`
4. Build and run the project: `mvn spring-boot:run`

## Usage:

1. Access the Swagger UI at http://localhost:8080/webjars/swagger-ui/index.html
2. Use the `/api/{version}/search/repositories` endpoint to search GitHub repositories.
3. Specify the language as a mandatory parameter and optionally the creation date.
4. Explore the results, which are sorted by stars count in descending order.
5. Each repository's popularity score is displayed, calculated based on stars count, forks count,
   and recency of update.

## Future Enhancements:

1. Implement user authentication for personalized features.
2. Introduce additional search filters for more granular control.
3. Enhance the visualization of repository data for better insights.
4. Allow searching different platforms like GitLab and Bitbucket.

## License:

This project is licensed under the MIT License. See
the [LICENSE](https://www.apache.org/licenses/LICENSE-2.0) file for details.