![Film_serch_engion_frames (1)](https://github.com/user-attachments/assets/29a9d9de-a92f-4469-973d-ed526a17c968)
![Film_serch_engion-2024-11-27_22-15](https://github.com/user-attachments/assets/4951b85d-bf8d-49b3-ab01-97fea82fa1de)

### MovieFinder: A Java EE-Based Movie Discovery Application

MovieFinder is a lightweight web application designed to help users discover movies, rate them, write reviews, and maintain personalized watchlists. The application serves as a platform where users can explore films using an external API, organize their movie preferences, and share their opinions through reviews. The project is built with core Java EE technologies, JDBC, and Tomcat, ensuring simplicity and scalability without relying on additional frameworks like Spring.

### Features:
1. **Movie Search via TMDB API**  
   - Seamless integration with the TMDB API for fetching real-time data about movies, including titles, posters, genres, and descriptions.

2. **User Authentication**  
   - Secure registration and login functionality, utilizing session-based authentication to manage user-specific data.

3. **Personalized Watchlists**  
   - Users can maintain "Want to Watch" and "Watched" lists to keep track of movies they are interested in and those they've already seen.

4. **Ratings and Reviews**  
   - Users can submit ratings and reviews for movies, which are stored and displayed on the respective movie pages. Ratings are aggregated to calculate an average score.

5. **Detailed Movie Pages**  
   - Display comprehensive movie information, including descriptions, genres, crew details, user ratings, and reviews.

6. **Playlists (Optional)**  
   - Create and share custom playlists of movies with other users.

7. **Movie Recommendations (Optional)**  
   - Simple recommendation system suggesting movies based on users’ previous ratings and watched genres.

8. **Random Movie Picker (Optional)**  
   - A fun feature allowing users to discover a random movie from their watchlists or recommendations.

### Technology Stack:
- **Backend**: Java EE (Servlets, JSP), JDBC for database interaction.
- **Frontend**: Basic HTML/CSS for UI, focusing on functionality over aesthetics.
- **Database**: PostgreSQL for persistent storage of user data, movie information, and reviews.
- **API Integration**: TMDB API for fetching movie data.
- **Application Server**: Apache Tomcat for hosting the Java EE application.
- **JSON Parsing**: Libraries like Gson or Jackson for processing API responses.

### Development Plan:
#### **1. Setting up the environment and database (29.11)**  
   - Install and configure Tomcat and PostgreSQL/MySQL.  
   - Implement the database schema.  
   - Establish a connection between the application and the database using JDBC.  

#### **2. Project structure setup (30.11)**  
   - Create a Maven project and define dependencies.  
   - Set up the basic folder structure for Servlets and other components.  

### 3. **User authentication (1.12)**  
- **User registration**:  
  - Create a registration form (HTML + CSS).  
  - Implement data validation (both client-side and server-side).  
  - Save user data in the database with password hashing (e.g., using `BCrypt`).  
- **User login**:  
  - Create a login form.  
  - Implement user authentication with password verification.  
  - Handle invalid login/password cases.  
- **Session management**:  
  - Configure `HttpSession` to store current user information.  
  - Add logic for logging out users.  

### 4. **TMDB API integration (2.12 - 3.12)**  
- **API connection**:  
  - Register on TMDB and obtain an API key.  
  - Develop a utility class for sending HTTP requests to the TMDB API.  
- **Movie search**:  
  - Add functionality for searching movies by keywords.  
  - Implement JSON response parsing (using `Gson` or `Jackson`).  
- **Request caching (optional)**:  
  - Implement caching for storing results of popular queries.  
  - Set up a timer to clear the cache at regular intervals.  

### 5. **Watchlists (5.12)**  
- **List creation**:  
  - Develop database tables to store user watchlists.  
  - Implement DAO classes to add and remove movies from watchlists.  
- **Adding movies to watchlists**:  
  - Add "Want to Watch" and "Watched" buttons on the movie page.  
  - Configure request handling through servlets.  
- **Displaying watchlists**:  
  - Create a user dashboard page.  
  - Implement logic for displaying "Want to Watch" and "Watched" movies on the dashboard.  

### 6. **Ratings and reviews (8.12)**  
- **Movie ratings**:  
  - Create a form for submitting ratings (stars/slider).  
  - Save ratings in the database.  
  - Display the average rating on the movie page.  
- **Reviews**:  
  - Add a form for submitting reviews.  
  - Implement CRUD operations:  
    - Create, edit, delete reviews.  
  - Display all reviews on the movie page.  

### 7. **Movie details page (9.12 - 10.12)**  
- **Core movie information**:  
  - Display movie title, poster, description, and genres.  
- **Integration with reviews and ratings**:  
  - Add a section for user reviews.  
  - Display the average and individual user ratings.  
- **API integration**:  
  - Use TMDB API data for additional information (e.g., trailers, cast).  
- **Page design**:  
  - Style the page elements with CSS.  

### 8. **Creating an attractive interface (12.12)**  
- Connect a CSS framework (e.g., Bootstrap or TailwindCSS).  
- Develop:  
  - A homepage with search functionality.  
  - Intuitive navigation between pages.  
  - Dark/light theme support (optional).  

### 9. **Testing and debugging (15.12)**  
- Test all components:  
  - Unit tests for DAO and service classes.  
  - Integration tests for database and API interactions.  
- Test the user interface:  
  - Check all forms and navigation flows.  
  - Fix display issues and errors.  
- Bug tracking:  
  - Create a list of identified issues.  
  - Fix critical and minor bugs.  

### 10. **Finalizing the project (16.12)**  
- **Documentation**:  
  - Write a description of application functionality.  
  - Provide installation and deployment instructions.  
- **Project packaging**:  
  - Package the project into a WAR file.  
  - Deploy it to Tomcat and test thoroughly.  

### 11. **Optional features**  
- **Recommendations (2-3 days)**:  
  - Implement a basic recommendation algorithm (e.g., based on popular movies or genres).  
- **Playlists (1 - 2 day)**:  
  - Add functionality for creating and managing custom playlists with filters.  
- **Random Movie Picker (1 day)**:  
  - Add a button to select a random movie from the user's lists.  


задачи 
пределать дб(спросить как мне лучше хранить данные(id api или всё в дб))(а что если в TMDB изменятся id)
блокировку дб 
фильмографию актёров тогда можно добавить вторую поисковую строку(поиск по актёрам)
обновление дб(популар филмс или просто подтягивать всё из апи а зранить только списки)
проверять данные с api на вшивость 
добавление тегов жанров критиками

