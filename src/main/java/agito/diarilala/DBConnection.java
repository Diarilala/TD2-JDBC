package agito.diarilala;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor

public class DBConnection {
    public Connection getDBConnection() throws SQLException{
        try {
            Dotenv dotenv = Dotenv.load();
            String url = dotenv.get("JDBC_URL");
            String user = dotenv.get("USERNAME");
            String password = dotenv.get("PASSWORD");
            if(url == null || user == null || password == null){
                throw new SQLException("DB connection has not been initialized");
            }
            return DriverManager.getConnection(url, user, password);
        }
        catch (SQLException e){
            throw new RuntimeException("Database connection failed");
        }
    }
    public void attemptCloseConnection(Connection connection){
        try{
            if(connection != null){
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database connection failed to close");
        }
    }
}
