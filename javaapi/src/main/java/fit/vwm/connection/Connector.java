package fit.vwm.connection;
import javax.ejb.Singleton;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Singleton
public class Connector {
    private static Connection con = null;

//    jedina metoda kterou muzu dostat connection
    public static Connection getConnection() {
        try {
            if (con == null || con.isClosed()){
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/market","root","hesloheslo");
        }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return con;
    }

//    metoda pro zavreni connection
    public static void closeConnection() throws SQLException{
        con.close();
    }
}
