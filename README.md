MovieClub is a dynamic web application crafted using Java Spring, Thymeleaf, and Spring Security. This platform offers users an immersive experience to discover, register, log in, and rate movies effortlessly.

## Key Features

- **Technology Stack:** Developed with Java Spring, Thymeleaf, and Spring Security.
- **Profiles:** Supports both development (`dev`) and production (`prod`) profiles.
- **Database Setup:**
   - **Development:** Utilizes the H2 database for convenient development and testing.
   - **Production:** Integrates with MySQL for robust and scalable data management.
- **Data Management:** Employs Liquibase for effective database versioning and schema management.
- **Enhanced Functionality:** Introduces movie editing functionality, allowing users to update movie details.

## Getting Started

1. Clone the repository: `git clone https://github.com/SayaS3/FilmHub.git`
2. Navigate to the project directory: `cd MovieClub`
3. Choose the appropriate profile:
   - Development: `./mvnw spring-boot:run -Dspring.profiles.active=dev`
   - Production: `./mvnw spring-boot:run -Dspring.profiles.active=prod`

## Database Configuration

- **Development:** The application is configured to use the H2 database for quick setup.
- **Production:** MySQL is the recommended database for production use. Update `application-prod.yml` with your MySQL configuration.

## Existing Features

- **Movie Rating:** Users can rate movies based on their experience.
- **Display Movies and Genres:** Users can view a list of movies and genres.
- **Admin Panel:** Admins can manage genres and movies, including addition, editing, and deletion.
- **Comments:** Logged-in users can post comments on movies.
- **Comments System:** Users can comment on movies, and administrators have the ability to manage comments and apply shadow bans.
- **Movie Search:** A search feature to find specific movies.
- **Error Reporting:** Users can report errors in movie descriptions.
- **Public User Profile:** View public profiles of users, including their rated movies.

## Planned Features

- **Public User Profile:** View public profiles of users, including their rated and commented movies.

- **User Administration:** Administrators can manage users and block accounts if necessary.

- **Edit User Profile:** Users can edit their profile information, such as changing email addresses or passwords.

- **Pagination:** Introduce pagination for better navigation, especially with a large number of movies.

- **Account Activation:** Activate user accounts through email confirmation after registration.

- **User-Submitted Movies:** Allow users to submit new movies, with approval by editors or administrators.


## Getting Started

To run the FilmHub application locally, follow these steps:

1. Clone the repository:

   ```bash
   git clone https://github.com/SayaS3/filmhub.git
