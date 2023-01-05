import com.example.java_luka_matsaberidze.FlightAssistant;
import com.example.java_luka_matsaberidze.FlightData;
import com.example.java_luka_matsaberidze.JDBCUtils;
import org.junit.jupiter.api.*;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class FlightAssistantMethodsTests {
    private static FlightAssistant flightAssistant;

    @BeforeAll
    public static void setUp() {
        System.out.println("Before All");
        flightAssistant = new FlightAssistant();
    }

    @BeforeEach
    public void beforeMethod() {
        System.out.println("Before methods");
    }

    @DisplayName("creating window")
    @Test
    public void CreateWindow()
    {
       // flightAssistant.Launch();
        System.out.println("Before methods");
    }

    @DisplayName("creating Table")
    @Test
    public void CreateTable()
    {
        System.out.println("creating table");
        Assertions.assertDoesNotThrow(()->
                JDBCUtils.createTable());
        System.out.println("Table created");
    }
    @DisplayName("adding data to sql")
    @Test
    public void InsertData()
    {
        System.out.println("adding data");
        FlightData testData = new FlightData("test1","zugdidi",455,LocalDate.now(),25.5 );
        Assertions.assertDoesNotThrow(()->
                JDBCUtils.InsertDataToDatabase(testData));
        System.out.println("data inserted");
    }

    @DisplayName("Getting data from Table")
    @Test
    public void GetTableData()
    {
        System.out.println("retrieving data");
        Assertions.assertDoesNotThrow(()->
                JDBCUtils.GetSourceCities());
        System.out.println("got data");
    }

    @DisplayName("Get cities")
    @Test
    public void GetCities()
    {
        flightAssistant.GetCities();
        System.out.println("buttons actions set");
    }


    @AfterEach
    public void afterMethod() {
        System.out.println("After");
    }

    @AfterAll
    public static void destroy() {
        JDBCUtils.Delete("test1");
        System.out.println("After All");
    }
}
