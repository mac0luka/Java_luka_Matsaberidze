package com.example.java_luka_matsaberidze;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
public class JDBCUtils {
    private  static final String JDBC_DRIVER="com.mysql.jdbc.Driver";
    private  static final String DATABASEURL="jdbc:mysql://localhost:3306/products";
    private static final String USERNAME="root1";
    private static final String PASSWORD="Pa$$w0rd";

    private static String tableName="FlightData";
    public static void createTable()
    {
        try
        {
            Connection connection = DriverManager.getConnection(DATABASEURL,USERNAME,PASSWORD);
            Statement statement = connection.createStatement();
//            String dropIfExistsSqlString="DROP TABLE IF EXISTS "+ tableName;
//            statement.execute(dropIfExistsSqlString);

            String createSqlString = "CREATE TABLE IF NOT EXISTS " + tableName +" " +
                    "(id INTEGER NOT NULL AUTO_INCREMENT," +
                    " flightSource VARCHAR(255)," +
                    " flightDestination VARCHAR(255),"+
                    " passengerCount INTEGER,"+
                    " flightPrice DOUBLE," +
                    "flightTime DATE,"+
                    " PRIMARY KEY ( id ))";

            statement.executeUpdate(createSqlString);
            System.out.println("Created Table");
        }
        catch (SQLException e)
        {
            System.out.println("couldn't connect to database");
            e.printStackTrace();
        }
    }

    public static void InsertDataToDatabase(FlightData flightData)
    {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DATABASEURL,USERNAME,PASSWORD);
            Statement statement = connection.createStatement();

            String sqlString = String.format("INSERT INTO %s(flightSource,flightDestination,passengerCount,flightPrice,flightTime)"
                    + " values (?, ?, ?, ?,?)",tableName);

            PreparedStatement preparedStmt = connection.prepareStatement(sqlString);

            preparedStmt.setString (1, flightData.getSource());
            preparedStmt.setString(2,flightData.getDestination());
            preparedStmt.setInt(3,flightData.getPlacesCount());
            preparedStmt.setDouble(4,flightData.getPrice());
            Date date = Date.valueOf(flightData.getFlightDate());
            preparedStmt.setDate(5,date);
            preparedStmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> GetSourceCities()
    {
        List<String> sourceCitiesLIst = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DATABASEURL,USERNAME,PASSWORD);
            Statement statement = connection.createStatement();
            String sql = "SELECT flightSource  from " + tableName;
            ResultSet  rs = statement.executeQuery(sql);
            while (rs.next())
            {
                if(rs.getString("flightSource")!=null) {
                    sourceCitiesLIst.add(rs.getString("flightSource"));
                }
            }
        return sourceCitiesLIst;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void Delete(String name)
    {
        try {
            Connection connection = DriverManager.getConnection(DATABASEURL,USERNAME,PASSWORD);
            Statement statement = connection.createStatement();
            String checkIfExists = "DELETE FROM " +tableName+" WHERE flightSource = ? ";
            PreparedStatement pstmt = null;
            pstmt = connection.prepareStatement(checkIfExists);
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Couldnt Delete data");
            e.printStackTrace();
        }
    }
}
