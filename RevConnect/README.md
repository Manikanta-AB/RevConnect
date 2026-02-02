# RevConnect

## Prerequisites
- **Java**: JDK 21 or higher.
- **Maven**: For building and running the project.
- **MySQL**: Database server running on `localhost:3306`.

## Database Setup
1. Ensure your MySQL server is running.
2. The application expects the following credentials by default:
   - **URL**: `jdbc:mysql://localhost:3306/revconnect`
   - **User**: `root`
   - **Password**: `Hello26@`
   
   *If your MySQL configuration is different, please update `src/main/java/config/DBConnection.java`.*

3. Initialize the database schema:
   Run the provided `setup.sql` script in your MySQL client to create the database and tables.
   ```bash
   mysql -u root -p < setup.sql
   ```
   (Enter your password when prompted)

## How to Run

1. Open a terminal in the project root directory.
2. Build the project:
   ```bash
   mvn clean install
   ```
3. Run the application:
   ```bash
   mvn exec:java
   ```

## Troubleshooting
- **Database Connection Error**: Verify MySQL is running and credentials in `DBConnection.java` match your setup.
- **Compilation Error**: Ensure you are using JDK 21. Check via `java -version`.
