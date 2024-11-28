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
#### **1. Setting up the environment and database (0.7 - 1 day)**  
   - Install and configure Tomcat and PostgreSQL/MySQL.  
   - Implement the database schema.  
   - Establish a connection between the application and the database using JDBC.  

#### **2. Project structure setup (1 days)**  
   - Create a Maven project and define dependencies.  
   - Set up the basic folder structure for Servlets and other components.  

#### **3. User authentication (1.3 days)**  
   - Implement registration and login functionality.  
   - Configure session management for users.  

#### **4. TMDB API integration (1 - 1.3 days)**  
   - Set up communication with the TMDB API for searching movies.  
   - Implement JSON parsing for API responses.  

#### **5. Watchlists (1.5 - 2 days)**  
   - Create functionality for adding movies to "Want to Watch" and "Watched" lists.  
   - Display the lists on the user dashboard.  

#### **6. Ratings and reviews (2 - 3 days)**  
   - Implement CRUD operations for user reviews and ratings.  
   - Aggregate ratings and display reviews on the movie pages.  

#### **7. Movie details page (1 - 1.5 days)**  
   - Design a page for displaying movie information, including reviews and ratings.  

#### **8. Optional features (if time permits)**  
   - **Recommendations (2 - 3 days)**: Implement a simple algorithm to suggest movies based on user activity.  
   - **Playlists (1 days)**: Allow users to create and manage custom playlists.  
   - **Random Movie Picker (1 day)**: Add a feature to display a random movie.  

#### **9. Testing and debugging (2 - 3 days)**  
   - Perform thorough testing of all application components.  
   - Fix any identified bugs and refine functionality.  

#### **10. Finalizing the project (1 day)**  
   - Prepare project documentation and package the application for deployment.
