import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.sql.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class Test {

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("Adding a fruit and a vegetable that is not in the database to the list of products")
    void AddingFruitNotInDatabase(boolean isFruit) {
        ProductList.ProductInfo expected = new ProductList.ProductInfo(isFruit);
        assertFalse(productList.searchWordInTable(expected.name));  // проверить, что добавляемый продует отсутствует
        productList.addProduct(expected);
        driver.navigate().refresh();
        ProductList.ProductInfo actual = new ProductList.ProductInfo(productList.getLastProduct());
        assertAll(() -> assertEquals(expected.name, actual.name),
                () -> assertEquals(expected.isFruit, actual.isFruit),
                () -> assertEquals(expected.isExotic, actual.isExotic));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("Adding a fruit and a vegetable that is not in the database WITH SQL")
    void AddingFruitNotInDatabaseSQL(boolean isFruit) throws SQLException {
        ProductList.ProductInfo expected = new ProductList.ProductInfo(isFruit);
        assertEquals(0, JDBC.countFoodName(expected.name)); // проверить, что добавляемый продует отсутствует
        productList.addProduct(expected);
        ProductList.ProductInfo actual = JDBC.getProductInfoFromDB(expected.name);
        assertAll(() -> assertEquals(expected.name, actual.name),
                () -> assertEquals(expected.isFruit, actual.isFruit),
                () -> assertEquals(expected.isExotic, actual.isExotic));
    }

    ProductList productList = new ProductList();
    public static WebDriver driver;

    @BeforeAll
    static void setup() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void openSite() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        productList.init(driver);
        driver.get("http://localhost:8080/");
        driver.manage().window().maximize();
        productList.openProductList();
    }

    @AfterEach
    void closing() {
        driver.close();
    }

    @AfterAll
    static void teardown() {
        driver.quit();
    }
}
